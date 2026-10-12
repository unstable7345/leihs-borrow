import React, { useEffect, useState } from 'react'
import {
  startOfDay,
  addMonths,
  parseISO,
  isValid,
  isBefore,
  eachDayOfInterval,
  addDays,
  isSameDay,
  format,
  formatISO,
  addMinutes,
  differenceInMinutes
} from 'date-fns'
import { enGB as defaultDateLocale } from 'date-fns/locale'
import { translate as t } from '../lib/translate'
import Let from '../lib/Let'
import Section from '../components/Section'
import MinusPlusControl from '../components/MinusPlusControl'
import DateRangePicker from '../components/DateRangePicker'
import Warning from '../components/Warning'
import InfoMessage from '../components/InfoMessage'
import orderPanelPropTypes from './OrderPanelPropTypes'
import cx from 'classnames'

const noop = () => {}

const OrderPanel = ({
  modelData,
  profileName,
  //
  now,
  maxDateTotal,
  maxDateLoaded,
  onCalendarNavigate = noop,
  //
  initialStartDate,
  initialEndDate,
  onDatesChange = noop,
  //
  initialQuantity = 1,
  onQuantityChange = noop,
  //
  inventoryPools,
  initialInventoryPoolId,
  onInventoryPoolChange = noop,
  //
  initialShowDayQuants = false,
  onShowDayQuantsChange = noop,
  //
  initialTimeSlot,
  initialHourly = true,
  onTimeSlotChange = noop,
  //
  periodAvailability,
  onPeriodChange = noop,
  //
  onValidate = noop,
  onSubmit = noop,
  locale,
  dateLocale,
  txt = {}
}) => {
  const { label } = txt

  const currentTime = now ? now : new Date()
  const today = startOfDay(currentTime)
  const maxDate = maxDateTotal ? startOfDay(maxDateTotal) : addMonths(today, 20 * 12)

  const [quantity, setQuantity] = useState(initialQuantity)
  const [selectedPoolId, setSelectedPoolId] = useState(initialInventoryPoolId || inventoryPools?.[0]?.id || 'NO_POOLS')
  // In a pool with reservations by the hour: 'hours' or 'day' (whole days)
  const [hourlyMode, setHourlyMode] = useState(initialTimeSlot || initialHourly ? 'hours' : 'day')
  // Time of pick up (on the start date) and of return (on the end date), "HH:mm"
  const [times, setTimes] = useState(
    initialTimeSlot ? { startTime: hhmm(initialTimeSlot.startTime), endTime: hhmm(initialTimeSlot.endTime) } : null
  )
  const [selectedRange, setSelectedRange] = useState(() => {
    const startDate = initialStartDate ? startOfDay(initialStartDate) : today
    const endDate = initialEndDate ? startOfDay(initialEndDate) : addDays(today, 1)
    return { startDate, endDate }
  })

  const findPool = id => inventoryPools.find(x => x.id === id)
  // The times of a reservation by the hour, null for whole days
  const activeTimes = (poolId = selectedPoolId, mode = hourlyMode, t = times) =>
    findPool(poolId)?.hourlyReservations && mode === 'hours' ? t : null

  // Default times when changing to a pool by the hour
  useEffect(() => {
    const pool = findPool(selectedPoolId)
    if (pool?.hourlyReservations && hourlyMode === 'hours' && !times) {
      const defaults = defaultTimes(pool, selectedRange.startDate, currentTime)
      setTimes(defaults)
      onTimeSlotChange({ ...stateForCallbacks(), timeSlot: defaults })
    }
  }, [selectedPoolId, hourlyMode, times, inventoryPools])

  // The availability for exactly the chosen period is asked for separately
  useEffect(() => {
    const t = activeTimes()
    if (t) {
      onPeriodChange(stateForCallbacks())
    }
  }, [selectedPoolId, hourlyMode, times, selectedRange])

  // State depending on the input states (e.g. validation result)
  const [dependentState, setDependentState] = useState()
  useEffect(() => {
    // Make sure the selected pool is in list (otherwise fill-in a surrogate)
    const poolFromList = inventoryPools.find(x => x.id === selectedPoolId)
    const selectedPool = poolFromList || {
      id: selectedPoolId,
      name: t(txt.validate, 'unknown-pool', locale),
      isSurrogate: true
    }
    const selectablePools = poolFromList ? inventoryPools : [selectedPool, ...inventoryPools]

    // Get availability data for selected pool
    const { availability } = modelData
    const poolAvailability = (() => {
      const tmp = availability.find(x => x.inventoryPool.id === selectedPool.id)
      if (!tmp) {
        return { inventoryPool: selectedPool, dates: [] }
      }
      return {
        ...tmp,
        dates: tmp.dates.map(x => ({
          ...x,
          parsedDate: parseISO(x.date)
        }))
      }
    })()

    // Times by the hour (null for whole days). The availability by day is
    // asked for the times on each day, which tells nothing about a period
    // over night (e.g. 16:00 to 10:00 the next day).
    const selectedTimes = activeTimes(selectedPool.id)
    const overNight = !!selectedTimes && selectedTimes.startTime >= selectedTimes.endTime

    // Extract data for DateRangePicker
    const { disabledDates, disabledStartDates, disabledEndDates } = getDateRangePickerConstraints(
      poolAvailability,
      today,
      quantity,
      overNight
    )
    const maxQuantityByDay = getMaxQuantityByDay(poolAvailability)

    // Validation
    const validationResult = validate(selectedPool, poolAvailability, selectedTimes)

    setDependentState({
      selectablePools,
      selectedPool,
      selectedTimes,
      poolAvailability,
      disabledDates,
      disabledStartDates,
      disabledEndDates,
      maxQuantityByDay,
      validationResult
    })

    onValidate(validationResult.isValid)
  }, [
    quantity,
    selectedPoolId,
    hourlyMode,
    times,
    periodAvailability,
    selectedRange,
    modelData,
    maxDateTotal,
    maxDateLoaded,
    inventoryPools,
    locale
  ])

  // Validation
  function validate(selectedPool, poolAvailability, selectedTimes) {
    const poolError = validatePool(selectedPool, locale, txt.validate)
    if (poolError) {
      return { poolError }
    }
    if (selectedPool.hourlyReservations) {
      const hoursError = validateHours(selectedPool, selectedRange, selectedTimes, currentTime, locale, txt.validate)
      if (hoursError) {
        return { dateRangeErrors: [hoursError] }
      }
    }
    const overNight = !!selectedTimes && selectedTimes.startTime >= selectedTimes.endTime
    const dateRangeErrors = validateDateRange(
      selectedRange,
      today,
      maxDate,
      poolAvailability,
      quantity,
      locale,
      dateLocale,
      txt.validate,
      overNight
    )
    if (dateRangeErrors && dateRangeErrors.length > 0) {
      return { dateRangeErrors: [...dateRangeErrors] }
    }
    // The availability for exactly the chosen period (when known)
    if (selectedTimes && periodAvailability && isSamePeriod(periodAvailability, stateForCallbacks())) {
      if (periodAvailability.quantity < quantity) {
        return { dateRangeErrors: [t(txt.validate, 'quantity-not-available-in-period', locale)] }
      }
    }
    return { isValid: true }
  }

  const [showDayQuants, setShowDayQuants] = useState(initialShowDayQuants)

  // Event handlers

  function submit(e) {
    e.preventDefault()
    const validationResult = validate(
      dependentState.selectedPool,
      dependentState.poolAvailability,
      dependentState.selectedTimes
    )
    if (validationResult.isValid) {
      onSubmit(stateForCallbacks())
    }
  }

  function changeQuantity(number) {
    setQuantity(number)
    onQuantityChange({ ...stateForCallbacks(), quantity: number })
  }

  function changeInventoryPool(e) {
    const id = e.target.value
    setSelectedPoolId(id)
    // The hours are specific to the pool (the defaults follow, see above)
    setHourlyMode('hours')
    setTimes(null)
    onInventoryPoolChange({ ...stateForCallbacks(), poolId: id, timeSlot: null })
    if (activeTimes()) {
      onTimeSlotChange({ ...stateForCallbacks(), poolId: id, timeSlot: null })
    }
  }

  function changeHourlyMode(e) {
    const mode = e.target.value
    const newTimes =
      mode === 'hours' ? times || defaultTimes(findPool(selectedPoolId), selectedRange.startDate, currentTime) : times
    setHourlyMode(mode)
    setTimes(newTimes)
    onTimeSlotChange({ ...stateForCallbacks(), timeSlot: activeTimes(selectedPoolId, mode, newTimes) })
  }

  function changeTime(key, value) {
    const newTimes = { ...times, [key]: value }
    setTimes(newTimes)
    onTimeSlotChange({ ...stateForCallbacks(), timeSlot: newTimes })
  }

  function changeDateRange(range) {
    setSelectedRange(range)
    onDatesChange({ ...stateForCallbacks(), ...range })
  }

  const stateForCallbacks = () => ({
    startDate: selectedRange.startDate,
    endDate: selectedRange.endDate,
    quantity,
    poolId: selectedPoolId,
    timeSlot: activeTimes()
  })

  function handleCalendarNavigate(newDate) {
    onCalendarNavigate({ date: newDate })
  }

  function changeShowDayQuants(e) {
    setShowDayQuants(e.target.checked)
    onShowDayQuantsChange(e.target.checked)
  }

  if (!dependentState) {
    return null
  }
  const {
    selectablePools,
    selectedPool,
    disabledDates,
    disabledStartDates,
    disabledEndDates,
    maxQuantityByDay,
    validationResult
  } = dependentState

  function renderDay(day) {
    const isoDate = formatISO(day, { representation: 'date' })
    const nofAvailable = showDayQuants && day >= today ? maxQuantityByDay[isoDate] : undefined
    const showQuantity = nofAvailable !== undefined
    return (
      <>
        <span className={cx('opcal__day-num', { 'opcal__day-num--with-quantity': showQuantity })}>
          {format(day, 'd')}
        </span>
        {showQuantity && <div className="opcal__day-quantity">{nofAvailable}</div>}
      </>
    )
  }

  return (
    <form onSubmit={submit} noValidate className="was-validated" autoComplete="off" id="order-dialog-form">
      <div className="d-grid gap-4">
        <Section>
          <div className="fw-bold">{modelData.name}</div>
        </Section>
        {profileName && (
          <Section title={t(label, 'user-delegation', locale)}>
            <div className="fw-bold">{profileName}</div>
          </Section>
        )}
        <Section title={t(label, 'pool', locale)}>
          <label htmlFor="pool-id" className="visually-hidden">
            {t(label, 'pool', locale)}
          </label>
          <select
            name="pool-id"
            id="pool-id"
            value={selectedPoolId}
            onChange={changeInventoryPool}
            className="form-select"
          >
            {selectablePools.map(({ id, name, totalReservableQuantity }) => (
              <option key={id} value={id}>
                {totalReservableQuantity
                  ? t(label, 'pool-max-amount', locale, { pool: name, amount: totalReservableQuantity })
                  : name}
              </option>
            ))}
          </select>
          {validationResult.poolError ? (
            <Warning className="mt-2">{validationResult.poolError}</Warning>
          ) : (
            <InfoMessage className="mt-2">
              {t(label, 'pool-max-amount-info', locale, { amount: selectedPool.totalReservableQuantity })}
            </InfoMessage>
          )}
        </Section>

        {!validationResult.poolError && (
          <Let title={t(label, 'timespan', locale)}>
            {({ title }) => (
              <div className="d-grid gap-4">
                <Section title={t(label, 'quantity', locale)}>
                  <label htmlFor="quantity" className="visually-hidden">
                    {t(label, 'quantity', locale)}
                  </label>
                  <MinusPlusControl
                    name="quantity"
                    id="quantity"
                    value={quantity}
                    onChange={changeQuantity}
                    min={1}
                    minusLabel={`${t(label, 'minus', locale)} 1`}
                    plusLabel={`${t(label, 'plus', locale)} 1`}
                  />
                </Section>
                {selectedPool.hourlyReservations && (
                  <Section title={t(label, 'reservation-type', locale)}>
                    <label htmlFor="reservation-type" className="visually-hidden">
                      {t(label, 'reservation-type', locale)}
                    </label>
                    <select
                      name="reservation-type"
                      id="reservation-type"
                      value={hourlyMode}
                      onChange={changeHourlyMode}
                      className="form-select"
                    >
                      <option value="hours">{t(label, 'by-hours', locale)}</option>
                      <option value="day">{t(label, 'whole-day', locale)}</option>
                    </select>
                    {hourlyMode === 'hours' && times && (
                      <div className="row g-3 mt-0">
                        <div className="col-6">
                          <label htmlFor="pickup-time" className="form-label">
                            {t(label, 'pickup-time', locale)}
                          </label>
                          <select
                            name="pickup-time"
                            id="pickup-time"
                            value={times.startTime}
                            onChange={e => changeTime('startTime', e.target.value)}
                            className="form-select"
                          >
                            {poolSteps(selectedPool)
                              .slice(0, -1)
                              .map(x => (
                                <option key={x} value={x}>
                                  {x}
                                </option>
                              ))}
                          </select>
                        </div>
                        <div className="col-6">
                          <label htmlFor="return-time" className="form-label">
                            {t(label, 'return-time', locale)}
                          </label>
                          <select
                            name="return-time"
                            id="return-time"
                            value={times.endTime}
                            onChange={e => changeTime('endTime', e.target.value)}
                            className="form-select"
                          >
                            {poolSteps(selectedPool).map(x => (
                              <option key={x} value={x}>
                                {x}
                              </option>
                            ))}
                          </select>
                        </div>
                      </div>
                    )}
                    <InfoMessage className="mt-2">
                      {hourlyMode === 'hours'
                        ? t(label, 'by-hours-info', locale)
                        : t(label, 'whole-day-hours-info', locale, {
                            startTime: hhmm(selectedPool.hourlyReservationsStartTime),
                            endTime: hhmm(selectedPool.hourlyReservationsEndTime)
                          })}{' '}
                      {selectedPool.maxReservationHours
                        ? t(label, 'max-hours-info', locale, { hours: selectedPool.maxReservationHours })
                        : t(label, 'no-max-hours-info', locale)}
                    </InfoMessage>
                  </Section>
                )}
                <Section title={title}>
                  <fieldset>
                    <legend className="visually-hidden">{title}</legend>
                    <DateRangePicker
                      selectedRange={selectedRange}
                      onChange={changeDateRange}
                      onCalendarNavigate={handleCalendarNavigate}
                      maxDateLoaded={maxDateLoaded}
                      now={today}
                      minDate={today}
                      maxDate={maxDate}
                      disabledDates={disabledDates}
                      disabledStartDates={disabledStartDates}
                      disabledEndDates={disabledEndDates}
                      locale={dateLocale || defaultDateLocale}
                      txt={{
                        from: t(label, 'from', locale),
                        until: t(label, 'until', locale),
                        placeholderFrom: t(label, 'undefined', locale),
                        placeholderUntil: t(label, 'undefined', locale)
                      }}
                      className={cx(validationResult.dateRangeErrors ? 'invalid-date-range' : '')}
                      dayButtonClass={cx('opcal__day')}
                      dayContentRenderer={renderDay}
                    />
                  </fieldset>
                  {validationResult.dateRangeErrors &&
                    validationResult.dateRangeErrors.map((msg, i) => (
                      <React.Fragment key={i}>
                        <Warning className="mt-2">{msg}</Warning>
                      </React.Fragment>
                    ))}
                  <div className="mt-3">
                    <div className="form-check form-switch d-inline-block">
                      <input
                        type="checkbox"
                        className="form-check-input"
                        id="show-day-quants"
                        checked={showDayQuants}
                        onChange={changeShowDayQuants}
                      />
                      <label className="form-check-label" htmlFor="show-day-quants">
                        {t(label, 'show-day-quants', locale)}
                      </label>
                    </div>
                  </div>
                </Section>
              </div>
            )}
          </Let>
        )}
      </div>
    </form>
  )
}

OrderPanel.displayName = 'OrderPanel'
OrderPanel.propTypes = orderPanelPropTypes
export default OrderPanel

const STEP_MINUTES = 30

// "HH:mm" of a time ("HH:mm" or "HH:mm:ss")
function hhmm(time) {
  return time ? time.slice(0, 5) : time
}

function toMinutes(time) {
  const [hours, minutes] = time.split(':').map(Number)
  return hours * 60 + minutes
}

function fromMinutes(minutes) {
  return `${String(Math.floor(minutes / 60)).padStart(2, '0')}:${String(minutes % 60).padStart(2, '0')}`
}

function atTime(day, time) {
  const [hours, minutes] = time.split(':').map(Number)
  return new Date(day.getFullYear(), day.getMonth(), day.getDate(), hours, minutes)
}

// The times of a pool by the hour, from its opening to its closing in steps of 30 minutes
function poolSteps(pool) {
  const from = Math.ceil(toMinutes(hhmm(pool.hourlyReservationsStartTime)) / STEP_MINUTES) * STEP_MINUTES
  const to = toMinutes(hhmm(pool.hourlyReservationsEndTime))
  const steps = []
  for (let minutes = from; minutes <= to; minutes += STEP_MINUTES) {
    steps.push(fromMinutes(minutes))
  }
  return steps
}

// The first time of pick up still possible on the day, and one hour later
function defaultTimes(pool, day, currentTime) {
  const steps = poolSteps(pool)
  const pickups = steps.slice(0, -1)
  const startTime = pickups.find(x => addMinutes(atTime(day, x), STEP_MINUTES) > currentTime) || pickups[0]
  const endTime = fromMinutes(Math.min(toMinutes(startTime) + 60, toMinutes(steps[steps.length - 1])))
  return { startTime, endTime }
}

function isSamePeriod(a, b) {
  return (
    a.poolId === b.poolId &&
    isSameDay(a.startDate, b.startDate) &&
    isSameDay(a.endDate, b.endDate) &&
    hhmm(a.startTime) === b.timeSlot?.startTime &&
    hhmm(a.endTime) === b.timeSlot?.endTime
  )
}

// Rules of a pool by the hour (times null for whole days, from opening to closing)
function validateHours(pool, { startDate, endDate }, times, currentTime, locale, txt) {
  if (!isValid(startDate) || !isValid(endDate)) {
    return
  }
  const opening = hhmm(pool.hourlyReservationsStartTime)
  const closing = hhmm(pool.hourlyReservationsEndTime)
  const start = atTime(startDate, times ? times.startTime : opening)
  const end = atTime(endDate, times ? times.endTime : closing)
  if (times && !isBefore(start, end)) {
    return t(txt, 'return-before-pickup', locale)
  }
  if (
    times ? !isBefore(currentTime, addMinutes(start, STEP_MINUTES)) : !isBefore(currentTime, atTime(startDate, closing))
  ) {
    return t(txt, times ? 'pickup-time-over' : 'pickup-day-over', locale)
  }
  const maxHours = pool.maxReservationHours
  if (maxHours && differenceInMinutes(end, start) > maxHours * 60) {
    return t(txt, 'max-reservation-hours', locale, { hours: maxHours })
  }
}

function getDateRangePickerConstraints(poolAvailability, today, wantedQuantity, ignoreQuantity = false) {
  const { dates } = poolAvailability
  const getDates = filter => [...dates.filter(filter).map(x => x.parsedDate)]
  return {
    disabledDates: ignoreQuantity ? [] : getDates(x => x.quantity < wantedQuantity && x.parsedDate >= today),
    disabledStartDates: getDates(x => x.startDateRestrictions && x.startDateRestrictions.length > 0),
    disabledEndDates: getDates(x => x.endDateRestrictions && x.startDateRestrictions.length > 0)
  }
}

function getMaxQuantityByDay(poolAvailability) {
  return Object.fromEntries(
    poolAvailability.dates.map(x => [formatISO(x.parsedDate, { representation: 'date' }), x.quantity])
  )
}

function getByDay(dateList, date) {
  return dateList.find(x => isSameDay(x.parsedDate, date))
}

function validatePool(inventoryPool, locale, txt) {
  if (inventoryPool.userHasNoAccess) {
    return t(txt, 'no-pool-access', locale)
  }
  if (inventoryPool.userIsSuspended) {
    return t(txt, 'pool-suspension', locale)
  }
  if (!inventoryPool.totalReservableQuantity) {
    return t(txt, 'item-not-available-in-pool', locale)
  }
  if (inventoryPool.isSurrogate) {
    return t(txt, 'unknown-pool', locale)
  }
  if (inventoryPool.hourlyReservations && inventoryPool.isMemberOfAGroup === false) {
    return t(txt, 'not-member-of-a-group', locale)
  }
}

export const validateDateRange = (
  selectedRange,
  today,
  maxDate,
  poolAvailability,
  wantedQuantity,
  locale,
  dateLocale,
  txt,
  // e.g. for a period by the hour over night, whose availability is not known by day
  ignoreQuantity = false
) => {
  const { startDate, endDate } = selectedRange
  const { dates, inventoryPool } = poolAvailability
  const { reservationAdvanceDays, maximumReservationDuration, holidays = [] } = inventoryPool

  const basicValidityMessage = (() => {
    // Ensure that a valid quantity is given (the quantity field also has its own validator, so this is an exceptional case)
    wantedQuantity = parseInt(wantedQuantity, 10)
    if (Number.isNaN(wantedQuantity) || wantedQuantity < 1) {
      return t(txt, 'missing-quantity', locale)
    }

    // Formal validity of dates (DateRangePicker guarantees for that, so this is an exceptional case)
    if (!isValid(startDate)) {
      return t(txt, 'invalid-start-date', locale)
    }
    if (!isValid(endDate)) {
      return t(txt, 'invalid-end-date', locale)
    }
    if (isBefore(endDate, startDate)) {
      return t(txt, 'start-after-end', locale)
    }
  })()

  if (basicValidityMessage) {
    return [basicValidityMessage]
  }

  // Start date
  const isOneDayPeriod = isSameDay(startDate, endDate)

  const startDateMessage = (() => {
    // Future-only
    if (startDate < today) {
      return t(txt, 'start-date-in-past', locale)
    }

    // Closed pool
    const txtPoolClosed = isOneDayPeriod ? 'pool-closed-at-start-and-end-date' : 'pool-closed-at-start-date'
    const startDateInfo = getByDay(dates, startDate)
    if (startDateInfo) {
      const isRestrictedBy = r => startDateInfo.startDateRestrictions && startDateInfo.startDateRestrictions.includes(r)
      if (isRestrictedBy('HOLIDAY')) {
        const holidayName = holidays.find(
          holiday =>
            startDate >= startOfDay(parseISO(holiday.startDate)) && startDate <= startOfDay(parseISO(holiday.endDate))
        )?.name
        return t(txt, txtPoolClosed, locale, { startDate }) + (holidayName ? ' (' + holidayName + ')' : '')
      } else if (isRestrictedBy('NON_WORKDAY')) {
        const dayName = format(startDate, 'EEEE', { locale: dateLocale })
        return (
          t(txt, txtPoolClosed, locale, { startDate }) +
          (dayName ? ' (' + t(txt, 'closed-on-day-of-week', locale, { dayName }) + ')' : '')
        )
      } else if (isRestrictedBy('VISITS_CAPACITY_REACHED')) {
        return t(txt, txtPoolClosed, locale, { startDate }) + t(txt, 'pool-closed-max-visits', locale)
      } else if (isRestrictedBy('BEFORE_EARLIEST_POSSIBLE_PICK_UP_DATE')) {
        // (This case should have been prevented by the future-only rule above)
        return t(txt, 'start-date-not-before', locale, { days: reservationAdvanceDays })
      }
    }
  })()

  const endDateMessage = (() => {
    // Max date
    if (endDate > maxDate) {
      return t(txt, 'end-date-too-late', locale, { maxDate })
    }

    // Closed pool
    if (isOneDayPeriod) {
      // (don't repeat the startDateMessage for the same day)
      return
    }
    if (endDate < today) {
      // (report issues only for non-past dates)
      return
    }
    const endDateInfo = getByDay(dates, endDate)
    if (endDateInfo) {
      const isRestrictedBy = r => endDateInfo.endDateRestrictions && endDateInfo.endDateRestrictions.includes(r)
      if (isRestrictedBy('HOLIDAY')) {
        const holidayName = holidays.find(
          h => endDate >= startOfDay(parseISO(h.startDate)) && endDate <= startOfDay(parseISO(h.endDate))
        )?.name
        return t(txt, 'pool-closed-at-end-date', locale, { endDate }) + (holidayName ? ' (' + holidayName + ')' : '')
      } else if (isRestrictedBy('NON_WORKDAY')) {
        const dayName = format(endDate, 'EEEE', { locale: dateLocale })
        return (
          t(txt, 'pool-closed-at-end-date', locale, { endDate }) +
          (dayName ? ' (' + t(txt, 'closed-on-day-of-week', locale, { dayName }) + ')' : '')
        )
      } else if (isRestrictedBy('VISITS_CAPACITY_REACHED')) {
        return t(txt, 'pool-closed-at-end-date', locale, { endDate }) + t(txt, 'pool-closed-max-visits', locale)
      }
    }
  })()

  // Available quantity
  const availabilityMessage = (() => {
    if (ignoreQuantity) {
      return
    }
    const noAvailDates = [
      ...eachDayOfInterval({ start: startDate, end: endDate }).filter(d => {
        if (d < today) {
          // (report issues only for non-past dates)
          return false
        }
        const dateInfo = getByDay(dates, d)
        return dateInfo && dateInfo.quantity < wantedQuantity
      })
    ]
    if (noAvailDates.length > 0) {
      if (noAvailDates.length === 1) {
        const startDate = noAvailDates[0]
        return t(txt, 'quantity-to-large-at-day', locale, { startDate })
      } else {
        return t(txt, 'quantity-to-large-in-range', locale, {})
      }
    }
  })()

  // Max reservation time
  const maximumReservationDurationMessage = (() => {
    if (maximumReservationDuration) {
      const maxEndDate = addDays(startDate, maximumReservationDuration - 1)
      if (endDate > maxEndDate) {
        return t(txt, 'maximum-reservation-duration', locale, { days: maximumReservationDuration })
      }
    }
  })()

  return [
    ...[startDateMessage, endDateMessage, availabilityMessage, maximumReservationDurationMessage].filter(x => !!x)
  ]
}

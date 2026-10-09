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
  formatISO
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
  onTimeSlotChange = noop,
  //
  onValidate = noop,
  onSubmit = noop,
  locale,
  dateLocale,
  txt = {}
}) => {
  const { label } = txt

  const today = startOfDay(now ? now : new Date())
  const maxDate = maxDateTotal ? startOfDay(maxDateTotal) : addMonths(today, 20 * 12)

  const [quantity, setQuantity] = useState(initialQuantity)
  const [selectedPoolId, setSelectedPoolId] = useState(initialInventoryPoolId || inventoryPools?.[0]?.id || 'NO_POOLS')
  // Time slots are identified by their time window (stable across the pools' data reloads)
  const [selectedTimeSlotKey, setSelectedTimeSlotKey] = useState(timeSlotKey(initialTimeSlot))
  const [selectedRange, setSelectedRange] = useState(() => {
    const startDate = initialStartDate ? startOfDay(initialStartDate) : today
    const endDate = initialEndDate ? startOfDay(initialEndDate) : addDays(today, 1)
    // A time slot is always booked for a single day
    return initialTimeSlot ? { startDate, endDate: startDate } : { startDate, endDate }
  })

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

    // Extract data for DateRangePicker
    const { disabledDates, disabledStartDates, disabledEndDates } = getDateRangePickerConstraints(
      poolAvailability,
      today,
      quantity
    )
    const maxQuantityByDay = getMaxQuantityByDay(poolAvailability)

    // Time slots of the selected pool (empty when it only offers whole days)
    const timeSlots = selectedPool.timeSlots || []
    const selectedTimeSlot = timeSlots.find(x => timeSlotKey(x) === selectedTimeSlotKey)

    // Validation
    const validationResult = validate(selectedPool, poolAvailability, selectedTimeSlot)

    setDependentState({
      selectablePools,
      selectedPool,
      timeSlots,
      selectedTimeSlot,
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
    selectedTimeSlotKey,
    selectedRange,
    modelData,
    maxDateTotal,
    maxDateLoaded,
    inventoryPools,
    locale
  ])

  // Validation
  function validate(selectedPool, poolAvailability, selectedTimeSlot) {
    const poolError = validatePool(selectedPool, locale, txt.validate)
    if (poolError) {
      return { poolError }
    }
    if (selectedTimeSlot && !isSameDay(selectedRange.startDate, selectedRange.endDate)) {
      return { dateRangeErrors: [t(txt.validate, 'time-slot-single-day', locale)] }
    }
    const dateRangeErrors = validateDateRange(
      selectedRange,
      today,
      maxDate,
      poolAvailability,
      quantity,
      locale,
      dateLocale,
      txt.validate
    )
    if (dateRangeErrors && dateRangeErrors.length > 0) {
      return { dateRangeErrors: [...dateRangeErrors] }
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
      dependentState.selectedTimeSlot
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
    // The time slots are specific to the pool
    setSelectedTimeSlotKey('')
    onInventoryPoolChange({ ...stateForCallbacks(), poolId: id, timeSlot: null })
    if (selectedTimeSlotKey) {
      onTimeSlotChange({ ...stateForCallbacks(), poolId: id, timeSlot: null })
    }
  }

  function changeTimeSlot(e) {
    const key = e.target.value
    const timeSlot = dependentState.timeSlots.find(x => timeSlotKey(x) === key) || null
    setSelectedTimeSlotKey(key)
    // A time slot is always booked for a single day
    const range = timeSlot ? { startDate: selectedRange.startDate, endDate: selectedRange.startDate } : selectedRange
    setSelectedRange(range)
    onTimeSlotChange({ ...stateForCallbacks(), ...range, timeSlot })
  }

  function changeDateRange(range) {
    if (selectedTimeSlotKey && !isSameDay(range.startDate, range.endDate)) {
      // Collapse to the newly picked day
      const day = isSameDay(range.startDate, selectedRange.startDate) ? range.endDate : range.startDate
      range = { startDate: day, endDate: day }
    }
    setSelectedRange(range)
    onDatesChange({ ...stateForCallbacks(), ...range })
  }

  const stateForCallbacks = () => ({
    startDate: selectedRange.startDate,
    endDate: selectedRange.endDate,
    quantity,
    poolId: selectedPoolId,
    timeSlot: dependentState?.selectedTimeSlot || null
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
    timeSlots,
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
                  <MinusPlusControl name="quantity" id="quantity" value={quantity} onChange={changeQuantity} min={1} />
                </Section>
                {timeSlots.length > 0 && (
                  <Section title={t(label, 'time-slot', locale)}>
                    <label htmlFor="time-slot-id" className="visually-hidden">
                      {t(label, 'time-slot', locale)}
                    </label>
                    <select
                      name="time-slot-id"
                      id="time-slot-id"
                      value={selectedTimeSlotKey}
                      onChange={changeTimeSlot}
                      className="form-select"
                    >
                      <option value="">{t(label, 'whole-day', locale)}</option>
                      {timeSlots.map(({ id, name, startTime, endTime }) => (
                        <option key={id} value={timeSlotKey({ startTime, endTime })}>
                          {t(label, 'time-slot-option', locale, { name, startTime, endTime })}
                        </option>
                      ))}
                    </select>
                    <InfoMessage className="mt-2">
                      {selectedTimeSlotKey ? t(label, 'time-slot-info', locale) : t(label, 'whole-day-info', locale)}
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

function timeSlotKey(timeSlot) {
  return timeSlot && timeSlot.startTime ? `${timeSlot.startTime}-${timeSlot.endTime}` : ''
}

function getDateRangePickerConstraints(poolAvailability, today, wantedQuantity) {
  const { dates } = poolAvailability
  const getDates = filter => [...dates.filter(filter).map(x => x.parsedDate)]
  return {
    disabledDates: getDates(x => x.quantity < wantedQuantity && x.parsedDate >= today),
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
}

export const validateDateRange = (
  selectedRange,
  today,
  maxDate,
  poolAvailability,
  wantedQuantity,
  locale,
  dateLocale,
  txt
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

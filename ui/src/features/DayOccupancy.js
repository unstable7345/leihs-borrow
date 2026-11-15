import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

// The occupancy of a day in a pool with reservations by the hour: one cell
// per step of 30 minutes, free or occupied (fewer items left than wanted),
// with the chosen times marked. A free cell can be tapped to pick the times.
const DayOccupancy = ({
  dayLabel,
  steps,
  selection,
  wantedQuantity = 1,
  isPast = () => false,
  onPickStep,
  onPreviousDay,
  onNextDay,
  canGoBack = true,
  canGoForward = true,
  txt
}) => {
  const hours = steps ? steps.filter(s => s.startTime.endsWith(':00')) : []
  const isSelected = step => !!selection && step.startTime >= selection.startTime && step.endTime <= selection.endTime

  return (
    <div className="day-occupancy">
      <div className="day-occupancy__header">
        <button
          type="button"
          className="day-occupancy__nav"
          onClick={onPreviousDay}
          disabled={!canGoBack}
          aria-label={txt.previousDay}
          title={txt.previousDay}
        >
          ‹
        </button>
        <div className="day-occupancy__title">{txt.title(dayLabel)}</div>
        <button
          type="button"
          className="day-occupancy__nav"
          onClick={onNextDay}
          disabled={!canGoForward}
          aria-label={txt.nextDay}
          title={txt.nextDay}
        >
          ›
        </button>
      </div>

      {!steps ? (
        <div className="day-occupancy__loading">{txt.loading}</div>
      ) : (
        <>
          <div className="day-occupancy__bar" style={{ gridTemplateColumns: `repeat(${steps.length}, 1fr)` }}>
            {steps.map((step, i) => {
              const past = isPast(step)
              const busy = step.quantity < wantedQuantity
              const selected = isSelected(step)
              const state = busy ? txt.busy : txt.free
              const amount = !busy ? ` (${txt.available(step.quantity)})` : ''
              return (
                <button
                  key={step.startTime}
                  type="button"
                  className={cx('day-occupancy__step', {
                    'day-occupancy__step--busy': busy,
                    'day-occupancy__step--past': past && !busy,
                    'day-occupancy__step--selected': selected,
                    'day-occupancy__step--selected-start': selected && (i === 0 || !isSelected(steps[i - 1])),
                    'day-occupancy__step--selected-end':
                      selected && (i === steps.length - 1 || !isSelected(steps[i + 1])),
                    'day-occupancy__step--hour': step.startTime.endsWith(':00')
                  })}
                  disabled={busy || past}
                  onClick={() => onPickStep(i)}
                  aria-label={`${step.startTime}–${step.endTime}: ${state}${amount}`}
                  aria-pressed={selected}
                  title={`${step.startTime}–${step.endTime}: ${state}${amount}`}
                />
              )
            })}
          </div>
          <div className="day-occupancy__scale" style={{ gridTemplateColumns: `repeat(${steps.length}, 1fr)` }}>
            {steps.map((step, i) =>
              step.startTime.endsWith(':00') ? (
                <span key={step.startTime} style={{ gridColumn: `${i + 1} / span 2` }}>
                  {hours.length > 8 && hours.indexOf(step) % 2 === 1 ? '' : step.startTime.slice(0, 2)}
                </span>
              ) : null
            )}
          </div>
          <div className="day-occupancy__legend">
            <span>
              <i className="day-occupancy__swatch" /> {txt.free}
            </span>
            <span>
              <i className="day-occupancy__swatch day-occupancy__swatch--busy" /> {txt.busy}
            </span>
            <span>
              <i className="day-occupancy__swatch day-occupancy__swatch--selected" /> {txt.selected}
            </span>
          </div>
        </>
      )}
    </div>
  )
}

DayOccupancy.propTypes = {
  dayLabel: PropTypes.string.isRequired,
  steps: PropTypes.arrayOf(
    PropTypes.shape({
      startTime: PropTypes.string.isRequired,
      endTime: PropTypes.string.isRequired,
      quantity: PropTypes.number.isRequired
    })
  ),
  selection: PropTypes.shape({ startTime: PropTypes.string, endTime: PropTypes.string }),
  wantedQuantity: PropTypes.number,
  isPast: PropTypes.func,
  onPickStep: PropTypes.func.isRequired,
  onPreviousDay: PropTypes.func.isRequired,
  onNextDay: PropTypes.func.isRequired,
  canGoBack: PropTypes.bool,
  canGoForward: PropTypes.bool,
  txt: PropTypes.object.isRequired
}

export default DayOccupancy

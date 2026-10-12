import PropTypes from 'prop-types'

const dateInfoPropType = PropTypes.shape({
  date: PropTypes.string.isRequired,
  quantity: PropTypes.number.isRequired,
  endDateRestrictions: PropTypes.any,
  startDateRestrictions: PropTypes.any
})

const modelDataPropType = PropTypes.shape({
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  availability: PropTypes.arrayOf(
    PropTypes.shape({
      inventoryPool: PropTypes.shape({
        id: PropTypes.string.isRequired,
        reservationAdvanceDays: PropTypes.number,
        maximumReservationDuration: PropTypes.number
      }).isRequired,
      dates: PropTypes.arrayOf(dateInfoPropType).isRequired
    })
  ).isRequired
})

const OrderPanelPropTypes = {
  /** availabilty and visits info from API */
  modelData: modelDataPropType.isRequired,
  /** Current profile (user personal or delegation) */
  profileName: PropTypes.string,

  /** current date (defaults to system time). Defines the min selectable date, the default date selection and the initially shown month  */
  now: PropTypes.instanceOf(Date),
  /** latest date that can be selected or navigated to, defaults to `now` + 20 years */
  maxDateTotal: PropTypes.instanceOf(Date),
  /** Date until which the underlying data was loaded (availability, holidays etc).
   * Directly wired to the equally named prop of `DateRangePicker` (see there for more info) */
  maxDateLoaded: PropTypes.instanceOf(Date).isRequired,
  /** callback when the shown month is changed using the calendar navigation (argument: `{date}`) */
  onCalendarNavigate: PropTypes.func,

  /** start date that is initially selected (defaults to today) */
  initialStartDate: PropTypes.instanceOf(Date),
  /** end date that is initially selected (defaults to today) */
  initialEndDate: PropTypes.instanceOf(Date),
  /** callback when the selected date range is changed (defaults to noop) */
  onDatesChange: PropTypes.func,

  /** wanted quantity that is initially selected (defaults to 1) */
  initialQuantity: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  /** callback when the wanted quantity is changed */
  onQuantityChange: PropTypes.func,

  /** list of inventory pools for selecting */
  inventoryPools: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      totalReservableQuantity: PropTypes.number,
      userHasNoAccess: PropTypes.bool,
      userIsSuspended: PropTypes.bool,
      /** whether the pool offers reservations by the hour, within its start and end time ("HH:mm") */
      hourlyReservations: PropTypes.bool,
      hourlyReservationsStartTime: PropTypes.string,
      hourlyReservationsEndTime: PropTypes.string,
      /** longest reservation the user may make (by the hour), empty for no limit */
      maxReservationHours: PropTypes.number,
      /** whether the user is a member of a group of the pool (only members may reserve by the hour) */
      isMemberOfAGroup: PropTypes.bool
    }).isRequired
  ).isRequired,
  /** initially selected pool */
  initialInventoryPoolId: PropTypes.string,
  /** callback, when selected pool changes */
  onInventoryPoolChange: PropTypes.func,

  /** times (`{startTime, endTime}`, "HH:mm") initially selected in a pool by the hour: pick up on the start date, return on the end date. Empty means whole days */
  initialTimeSlot: PropTypes.shape({ startTime: PropTypes.string, endTime: PropTypes.string }),
  /** without `initialTimeSlot`: whether to start by the hour (default) or with whole days */
  initialHourly: PropTypes.bool,
  /** callback, when the selected times change (argument: state incl. `timeSlot`, which is null for whole days) */
  onTimeSlotChange: PropTypes.func,
  /** available quantity for exactly the period of the state given to `onPeriodChange` (`{poolId, startDate, endDate, startTime, endTime, quantity}`) */
  periodAvailability: PropTypes.shape({
    poolId: PropTypes.string,
    startDate: PropTypes.instanceOf(Date),
    endDate: PropTypes.instanceOf(Date),
    startTime: PropTypes.string,
    endTime: PropTypes.string,
    quantity: PropTypes.number
  }),
  /** callback, when the period by the hour changes (argument: state incl. `timeSlot`) */
  onPeriodChange: PropTypes.func,

  /** callback, submits user selection. arguments: `{startDate, endDate, quantity, poolId, timeSlot}` */
  onSubmit: PropTypes.func.isRequired,
  /** function which is called initially and on each form value change with true (valid) or false (invalid)  */
  onValidate: PropTypes.func,

  /** ISO localization string, like "en-GB" or "de-CH" */
  locale: PropTypes.string.isRequired,
  /** date localization from date-fns/locale. Default: en-GB */
  dateLocale: PropTypes.object,
  /** translation map */
  txt: PropTypes.object.isRequired
}

export default OrderPanelPropTypes

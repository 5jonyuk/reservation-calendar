export default function Calendar({
  currentYear,
  currentMonth,
  selectedDate,
  onDateSelect,
  onPrevMonth,
  onNextMonth,
  reservations,
}) {
  const getDaysInMonth = (year, month) => {
    return new Date(year, month + 1, 0).getDate();
  };

  const getFirstDayOfMonth = (year, month) => {
    return new Date(year, month, 1).getDay();
  };

  const getReservationsForDate = (day) => {
    const dateStr = `${currentYear}-${String(currentMonth + 1).padStart(
      2,
      "0"
    )}-${String(day).padStart(2, "0")}`;
    return reservations.filter((r) => r.pickupDate === dateStr);
  };

  const daysInMonth = getDaysInMonth(currentYear, currentMonth);
  const firstDay = getFirstDayOfMonth(currentYear, currentMonth);
  const days = Array.from({ length: daysInMonth }, (_, i) => i + 1);
  const weekDays = ["일", "월", "화", "수", "목", "금", "토"];

  return (
    <div className="bg-white rounded-lg shadow-sm h-full p-6">
      {/* 헤더 */}
      <div className="flex items-center justify-center mb-6">
        <button
          onClick={onPrevMonth}
          className="p-2 hover:bg-gray-100 rounded-full transition cursor-pointer"
        >
          ◀
        </button>
        <h2 className="text-xl font-bold">
          {currentYear}년 {currentMonth + 1}월
        </h2>
        <button
          onClick={onNextMonth}
          className="p-2 hover:bg-gray-100 rounded-full transition cursor-pointer"
        >
          ▶
        </button>
      </div>

      {/* 요일 헤더 */}
      <div className="grid grid-cols-7 gap-1 mb-2">
        {weekDays.map((day, idx) => (
          <div
            key={day}
            className={`text-center text-sm font-semibold py-2 bg-green-50 rounded ${
              idx === 0 ? " text-red-500" : "text-gray-600"
            }`}
          >
            {day}
          </div>
        ))}
      </div>

      {/* 날짜 그리드 */}
      <div className="grid grid-cols-7 gap-1">
        {/* 빈 칸 채우기 */}
        {Array.from({ length: firstDay }).map((_, i) => (
          <div
            key={`empty-${i}`}
            className="min-h-24 border border-transparent"
          />
        ))}

        {/* 실제 날짜들 */}
        {days.map((day, idx) => {
          const dayOfWeek = (firstDay + idx) % 7;
          const isSunday = dayOfWeek === 0;
          const reservationsForDay = getReservationsForDate(day);
          const hasReservations = reservationsForDay.length > 0;
          const isSelected = selectedDate === day;

          return (
            <div
              key={day}
              onClick={() => onDateSelect(day)}
              className={`min-h-24 p-2 border cursor-pointer transition-all rounded flex flex-col items-center justify-between ${
                isSelected
                  ? "bg-green-50 border-green-400 shadow-sm"
                  : "bg-white border-gray-200 hover:bg-gray-50 hover:border-gray-300"
              }`}
            >
              <div
                className={`text-sm font-medium  ${
                  isSunday ? "text-red-500" : "text-gray-700"
                }`}
              >
                {day}
              </div>

              {/* 예약 있는 날 표시 */}
              {hasReservations && (
                <div className="w-2 h-2 mt-1 rounded-full bg-green-400" />
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}

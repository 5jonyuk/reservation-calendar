import ReservationCard from "./ReservationCard";

export default function ReservationView({
  selectedDay,
  currentMonth,
  selectedDateReservations,
  onSelectDetail,
  onEditClick,
  onDeleteClick,
}) {
  return (
    <div className="relative bg-gray-50 p-6 overflow-auto h-screen">
      <div className="absolute left-0 top-6 bottom-6 border-l border-gray-300" />
      <h2 className="text-xl font-bold mb-6 text-center">예약 내역</h2>

      {!selectedDay && (
        <p className="text-gray-500 text-sm text-center mt-8">
          달력에서 날짜를 선택하세요
        </p>
      )}

      {selectedDay && selectedDateReservations.length === 0 && (
        <p className="text-gray-500 text-sm text-center mt-8">
          {currentMonth + 1}월 {selectedDay}일에 예약 내역이 없습니다
        </p>
      )}

      {selectedDay && selectedDateReservations.length > 0 && (
        <div className="space-y-4">
          {selectedDateReservations.map((reservation) => (
            <ReservationCard
              key={reservation.id}
              reservation={reservation}
              onSelectDetail={() => onSelectDetail(reservation.id)}
              onEditClick={() => onEditClick(reservation.id)}
              onDeleteClick={() => onDeleteClick(reservation.id)}
            />
          ))}
        </div>
      )}
    </div>
  );
}

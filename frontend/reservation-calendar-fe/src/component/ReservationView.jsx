import { useMemo } from "react";
import ReservationCard from "./ReservationCard";
import Icon from "./Icon";

export default function ReservationView({
  selectedDay,
  currentMonth,
  selectedDateReservations,
  onSelectDetail,
  onEditClick,
  onDeleteClick,
  onLogout,
}) {
  const LogoutIcon = (props) => (
    <Icon {...props}>
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16 17 21 12 16 7" />
      <line x1="21" y1="12" x2="9" y2="12" />
    </Icon>
  );

  const parsePickupTimeToMinutes = (pickupTime) => {
    if (!pickupTime || typeof pickupTime !== "string") return Number.MAX_SAFE_INTEGER;

    const [hours = "99", minutes = "59"] = pickupTime.split(":");
    return Number(hours) * 60 + Number(minutes);
  };

  const sortedReservations = useMemo(() => {
    return [...selectedDateReservations].sort((a, b) => {
      const pickupCompletedDiff =
        Number(Boolean(a.pickupCompleted)) - Number(Boolean(b.pickupCompleted));
      if (pickupCompletedDiff !== 0) return pickupCompletedDiff;

      const pickupTimeDiff =
        parsePickupTimeToMinutes(a.pickupTime) -
        parsePickupTimeToMinutes(b.pickupTime);
      if (pickupTimeDiff !== 0) return pickupTimeDiff;

      return Number(a.id) - Number(b.id);
    });
  }, [selectedDateReservations]);

  return (
    <div className="relative bg-gray-50 p-6 overflow-auto h-screen">
      <div className="absolute left-0 top-6 bottom-6 border-l border-gray-300" />

      <div className="flex justify-between items-center mb-6">
        <div className="flex-1" />
        <h2 className="text-xl font-bold text-center flex-1">예약 내역</h2>
        <div className="flex-1 flex justify-end">
          <button
            onClick={onLogout}
            className="p-2 hover:bg-gray-200 rounded-lg transition cursor-pointer"
            title="로그아웃"
          >
            <LogoutIcon className="w-6 h-6 text-gray-400" />
          </button>
        </div>
      </div>

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
          {sortedReservations.map((reservation) => (
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

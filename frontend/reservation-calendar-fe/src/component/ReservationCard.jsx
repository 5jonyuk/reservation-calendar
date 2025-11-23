import { useState } from "react";

export default function ReservationCard({
  reservation,
  onSelectDetail,
  onEditClick,
  onDeleteClick,
}) {
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const toggleDropdown = (e) => {
    e.stopPropagation();
    setDropdownOpen(!dropdownOpen);
  };

  const formatTime = (inputTime) => {
    if (!inputTime || typeof inputTime !== "string") {
      return "시간 정보 없음";
    }

    const [hours, minutes] = inputTime.split(":");
    const formattedHours = String(hours).padStart(2, "0");
    const formattedMinutes = String(minutes).padStart(2, "0");
    return `${formattedHours}시 ${formattedMinutes}분`;
  };

  return (
    <div
      className="bg-white rounded-lg p-6 relative hover:shadow-sm transition cursor-pointer h-auto"
      onClick={() => onSelectDetail(reservation.id)}
    >
      <div className="flex items-start justify-between mb-2">
        <span className="text-sm text-gray-600">{reservation.pickupDate}</span>

        {/* 드롭다운 버튼 */}
        <div className="relative">
          <button
            className=" text-gray-400 hover:text-gray-600 text-xl leading-none cursor-pointer transition"
            onClick={toggleDropdown}
          >
            ⋮
          </button>

          {dropdownOpen && (
            <div className="absolute right-0 mt-2 w-32 bg-white border border-gray-200 rounded-lg shadow-lg z-50">
              <button
                className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer"
                onClick={(e) => {
                  e.stopPropagation();
                  onEditClick(reservation.id);
                  setDropdownOpen(false);
                }}
              >
                수정
              </button>
              <button
                className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer"
                onClick={(e) => {
                  e.stopPropagation();
                  onDeleteClick(reservation.id);
                  setDropdownOpen(false);
                }}
              >
                삭제
              </button>
            </div>
          )}
        </div>
      </div>

      <div className="text-2xl font-bold mb-3 text-blue-600">
        {reservation.amount.toLocaleString()}원
      </div>

      <div className="flex justify-start items-center space-x-3">
        <div className="inline-block px-3 py-1 bg-white rounded-full border text-sm">
          예약자명: {reservation.customerName}
        </div>
        <div className="inline-block px-3 py-1 bg-white rounded-full border text-sm">
          픽업시간: {formatTime(reservation.pickupTime)}
        </div>
      </div>
    </div>
  );
}

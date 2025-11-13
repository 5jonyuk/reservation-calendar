import { useState } from "react";
import ReservationModal from "./ReservationModal";
import ReservationCard from "./ReservationCard";
import axios from "axios";
import apiUrl from "../config/ApiUrl";

export default function ReservationView({
  selectedDate,
  currentMonth,
  reservations,
}) {
  const [selectedReservation, setSelectedReservation] = useState(null);

  const selectedReservations = selectedDate
    ? reservations.filter((r) => {
        const dateStr = `${r.pickupDate.split("-")[0]}-${String(
          currentMonth + 1
        ).padStart(2, "0")}-${String(selectedDate).padStart(2, "0")}`;
        return r.pickupDate === dateStr;
      })
    : [];

  const handleEdit = (reservation) => {
    console.log("수정", reservation);
  };

  const handleDelete = (reservation) => {
    console.log("삭제", reservation);
  };

  const handleSelect = async (reservationId) => {
    try {
      const response = await axios.get(
        `${apiUrl}/api/reservation/${reservationId}`
      );
      setSelectedReservation(response.data);
    } catch (error) {
      console.error("예약 상세정보 조회 실패:", error);
    }
  };

  return (
    <div className="relative bg-gray-50 p-6 overflow-auto h-screen">
      <div className="absolute left-0 top-6 bottom-6 border-l border-gray-300" />
      <h2 className="text-xl font-bold mb-6 text-center">예약 내역</h2>

      {!selectedDate && (
        <p className="text-gray-500 text-sm text-center mt-8">
          달력에서 날짜를 선택하세요
        </p>
      )}

      {selectedDate && selectedReservations.length === 0 && (
        <p className="text-gray-500 text-sm text-center mt-8">
          {currentMonth + 1}월 {selectedDate}일에 예약 내역이 없습니다
        </p>
      )}

      {selectedDate && selectedReservations.length > 0 && (
        <div className="space-y-4">
          {selectedReservations.map((reservation) => (
            <ReservationCard
              key={reservation.id}
              reservation={reservation}
              onSelect={handleSelect}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}

      {/* 모달 호출 */}
      <ReservationModal
        reservation={selectedReservation}
        currentMonth={currentMonth}
        selectedDate={selectedDate}
        onClose={() => setSelectedReservation(null)}
      />
    </div>
  );
}

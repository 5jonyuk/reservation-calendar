import { useState } from "react";
import ReservationDetailModal from "./modal/ReservationDetailModal";
import ReservationUpdateModal from "./modal/ReservationUpdateModal";
import ReservationCard from "./ReservationCard";
import axios from "axios";
import apiUrl from "../config/ApiUrl";

export default function ReservationView({
  selectedDate,
  currentMonth,
  selectedDateReservations,
}) {
  const [selectedDetailReservation, setSelectedDetailReservation] =
    useState(null);

  const modalModes = {
    DETAIL: "detail",
    EDIT: "edit",
    NULL: null,
  };
  const [modalMode, setModalMode] = useState(modalModes.NULL);

  // const selectedDateReservations = selectedDate
  //   ? reservations.filter((r) => {
  //       const dateStr = `${r.pickupDate.split("-")[0]}-${String(
  //         currentMonth + 1
  //       ).padStart(2, "0")}-${String(selectedDate).padStart(2, "0")}`;
  //       return r.pickupDate === dateStr;
  //     })
  //   : [];

  const handleEdit = async (reservationId, updatedReservation) => {
    try {
      const response = await axios.patch(
        `${apiUrl}/api/reservation/${reservationId}`,
        updatedReservation
      );
      setSelectedDetailReservation({
        ...response.data,
        createdAt: selectedDetailReservation.createdAt,
      });
    } catch (error) {
      const errorMessage = `[ERROR] ${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  const handleEditClick = async (reservationId) => {
    const isCashed =
      selectedDetailReservation &&
      selectedDetailReservation.id === reservationId;
    try {
      if (!isCashed) {
        const response = await axios.get(
          `${apiUrl}/api/reservation/${reservationId}`
        );
        setSelectedDetailReservation(response.data);
      }
      setModalMode(modalModes.EDIT);
    } catch (error) {
      const errorMessage = `[ERROR] ${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  const handleDelete = (reservation) => {
    console.log("삭제", reservation);
  };

  const handleSelect = async (reservationId) => {
    const isCashed =
      selectedDetailReservation &&
      selectedDetailReservation.id === reservationId;
    try {
      if (!isCashed) {
        const response = await axios.get(
          `${apiUrl}/api/reservation/${reservationId}`
        );
        setSelectedDetailReservation(response.data);
      }
      setModalMode(modalModes.DETAIL);
    } catch (error) {
      const errorMessage = `[ERROR] ${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  const closeModal = () => {
    setModalMode(null);
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

      {selectedDate && selectedDateReservations.length === 0 && (
        <p className="text-gray-500 text-sm text-center mt-8">
          {currentMonth + 1}월 {selectedDate}일에 예약 내역이 없습니다
        </p>
      )}

      {selectedDate && selectedDateReservations.length > 0 && (
        <div className="space-y-4">
          {selectedDateReservations.map((reservation) => (
            <ReservationCard
              key={reservation.id}
              reservation={reservation}
              onSelect={handleSelect}
              onEditClick={handleEditClick}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}

      {/* 상세 모달 */}
      {modalMode === modalModes.DETAIL && (
        <ReservationDetailModal
          reservation={selectedDetailReservation}
          isOpen={modalMode === modalModes.DETAIL}
          onClose={closeModal}
          // onEdit={handleEditClick}
        />
      )}

      {/* 수정 모달 */}
      {modalMode === modalModes.EDIT && (
        <ReservationUpdateModal
          reservation={selectedDetailReservation}
          isOpen={modalMode === modalModes.EDIT}
          onClose={closeModal}
          onEdit={handleEdit}
        />
      )}
    </div>
  );
}

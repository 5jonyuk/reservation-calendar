import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Calendar from "../component/Calendar";
import ReservationView from "../component/ReservationView";
import apiUrl from "../config/ApiUrl";
import axios from "axios";
import ReservationAddModal from "../component/modal/ReservationAddModal";
import ReservationDetailModal from "../component/modal/ReservationDetailModal";
import ReservationUpdateModal from "../component/modal/ReservationUpdateModal";
import ReservationDeleteModal from "../component/modal/ReservationDeleteModal";
import Spinner from "../component/Spinner";

export default function Home({ onLogout }) {
  const now = new Date();
  const KST = new Date(now.toLocaleString("en-US", { timeZone: "Asia/Seoul" }));
  const currentYearKST = KST.getFullYear();
  const currentMonthKST = KST.getMonth();
  const currentDayKST = KST.getDate();

  const [currentYear, setCurrentYear] = useState(currentYearKST);
  const [currentMonth, setCurrentMonth] = useState(currentMonthKST);
  const [selectedDay, setSelectedDay] = useState(currentDayKST);

  const [monthReservations, setMonthReservations] = useState([]);
  const [dayReservations, setDayReservations] = useState([]);
  const [selectedReservation, setSelectedReservation] = useState(null);

  const modalModes = {
    DETAIL: "detail",
    ADD: "add",
    EDIT: "edit",
    DELETE: "delete",
    NULL: null,
  };
  const [modalMode, setModalMode] = useState(modalModes.NULL);
  const [isServerReady, setIsServerReady] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const navigate = useNavigate();

  const fetchMonthReservations = async (year, month) => {
    try {
      const yearMonth = `${year}-${String(month + 1).padStart(2, "0")}`;
      const response = await axios.get(`${apiUrl}/api/reservation/month`, {
        params: { yearMonth },
      });
      setMonthReservations(response.data);
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      console.error(errorMessage);
    }
  };

  const fetchDayReservations = async (date) => {
    try {
      const response = await axios.get(`${apiUrl}/api/reservation/date`, {
        params: { date },
      });
      setDayReservations(response.data);
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      console.error(errorMessage);
    }
  };

  const handlePrevMonth = () => {
    if (currentMonth === 0) {
      setCurrentMonth(11);
      setCurrentYear(currentYear - 1);
    } else {
      setCurrentMonth(currentMonth - 1);
    }
    setSelectedDay(null);
  };

  const handleNextMonth = () => {
    if (currentMonth === 11) {
      setCurrentMonth(0);
      setCurrentYear(currentYear + 1);
    } else {
      setCurrentMonth(currentMonth + 1);
    }
    setSelectedDay(null);
  };

  const handleAddClick = () => {
    setSelectedReservation(null);
    setModalMode(modalModes.ADD);
  };

  const handleEditClick = async (reservationId) => {
    const isCashed =
      selectedReservation && selectedReservation.id === reservationId;
    try {
      if (!isCashed) {
        const response = await axios.get(
          `${apiUrl}/api/reservation/${reservationId}`
        );
        setSelectedReservation(response.data);
      }
      setModalMode(modalModes.EDIT);
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  const handleDeleteClick = async (reservationId) => {
    const isCashed =
      selectedReservation && selectedReservation.id === reservationId;
    try {
      if (!isCashed) {
        const response = await axios.get(
          `${apiUrl}/api/reservation/${reservationId}`
        );
        setSelectedReservation(response.data);
      }
      setModalMode(modalModes.DELETE);
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  const handleSelectDetail = async (reservationId) => {
    const isCashed =
      selectedReservation && selectedReservation.id === reservationId;
    try {
      if (!isCashed) {
        const response = await axios.get(
          `${apiUrl}/api/reservation/${reservationId}`
        );
        setSelectedReservation(response.data);
      }
      setModalMode(modalModes.DETAIL);
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  const handleAdd = async (newReservation) => {
    try {
      const response = await axios.post(
        `${apiUrl}/api/reservation`,
        newReservation
      );
      alert("예약이 추가되었습니다.");
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      alert(errorMessage);
      throw error;
    }
  };

  const handleEdit = async (reservationId, updatedReservation) => {
    try {
      const response = await axios.patch(
        `${apiUrl}/api/reservation/${reservationId}`,
        updatedReservation
      );
      setSelectedReservation((prev) => {
        if (!prev || prev.id !== reservationId) return prev;

        return {
          ...response.data,
          createdAt: prev.createdAt,
        };
      });

      setDayReservations((prev) =>
        prev.map((reservation) =>
          reservation.id === reservationId
            ? { ...reservation, ...updatedReservation }
            : reservation
        )
      );

      alert("예약이 수정되었습니다.");
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      alert(errorMessage);
      throw error;
    }
  };

  const handlePickupComplete = async (reservationId) => {
    try {
      const response = await axios.get(`${apiUrl}/api/reservation/${reservationId}`);
      const reservationDetail = response.data;

      await handleEdit(reservationId, {
        pickupDate: reservationDetail.pickupDate,
        pickupTime: reservationDetail.pickupTime,
        customerName: reservationDetail.customerName,
        customerPhone: reservationDetail.customerPhone,
        menu: reservationDetail.menu,
        amount: reservationDetail.amount,
        paymentCompleted: reservationDetail.paymentCompleted,
        pickupCompleted: true,
      });
    } catch (error) {
      console.error(error);
    }
  };

  const handleDelete = async (reservationId) => {
    try {
      const response = await axios.delete(
        `${apiUrl}/api/reservation/${reservationId}`
      );
      setSelectedReservation(null);
      setModalMode(modalModes.NULL);
    } catch (error) {
      const errorMessage = `${error.response.data.message}`;
      alert(errorMessage);
      throw error;
    }
  };

  const closeModal = () => {
    setModalMode(null);
  };

  const handleLogout = async () => {
    await onLogout();
    navigate("/login");
  };

  const checkServerHealth = async () => {
    try {
      await axios.get(`${apiUrl}/api/health`);

      setIsServerReady(true);
      setIsLoading(false);
    } catch (error) {
      console.log("Server still cold start...");
    }
  };

  useEffect(() => {
    fetchMonthReservations(currentYear, currentMonth);
    setDayReservations([]);

    if (currentYear === currentYearKST && currentMonth === currentMonthKST) {
      // 현재 월로 돌아오면 오늘 날짜로 포커싱을 유지
      setSelectedDay(currentDayKST);
    } else {
      // 다른 월로 이동하면 날짜 선택을 해제
      setSelectedDay(null);
    }
  }, [currentYear, currentMonth]);

  useEffect(() => {
    if (selectedDay !== null) {
      const seletedDateFormatted = `${currentYear}-${String(
        currentMonth + 1
      ).padStart(2, "0")}-${String(selectedDay).padStart(2, "0")}`;

      fetchDayReservations(seletedDateFormatted);
    }
  }, [currentYear, currentMonth, selectedDay]);

  useEffect(() => {
    if (isServerReady) {
      setIsLoading(false);
      return;
    }
    checkServerHealth();

    // 5초 간격으로 헬스체크 반복
    const intervalId = setInterval(checkServerHealth, 5000);

    return () => clearInterval(intervalId);
  }, [isServerReady]);

  if (isLoading) {
    return (
      <div className="flex flex-col justify-center items-center h-screen bg-gray-100">
        <p className="mt-2 text-xl text-gray-700">
          🧊 서버를 깨우는 중입니다... 잠시만 기다려 주세요. 🧊
        </p>
        <div>
          <Spinner className="mt-3" size="lg" color="text-black-500" />
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      {/* 달력 */}
      <div className="flex-1 p-6">
        <Calendar
          currentYear={currentYear}
          currentMonth={currentMonth}
          selectedDay={selectedDay}
          onDateSelect={setSelectedDay}
          onPrevMonth={handlePrevMonth}
          onNextMonth={handleNextMonth}
          reservations={monthReservations}
          onAddClick={handleAddClick}
        />
      </div>

      {/* 예약 내역 */}
      <div className="w-96">
        <ReservationView
          selectedDay={selectedDay}
          currentMonth={currentMonth}
          selectedDateReservations={dayReservations}
          onSelectDetail={handleSelectDetail}
          onEditClick={handleEditClick}
          onDeleteClick={handleDeleteClick}
          onCompleteClick={handlePickupComplete}
          onLogout={handleLogout}
        />
      </div>

      {/* ================ 모달 부분 ================ */}

      {/* 상세 모달 */}
      {modalMode === modalModes.DETAIL && selectedReservation && (
        <ReservationDetailModal
          reservation={selectedReservation}
          isOpen={modalMode === modalModes.DETAIL}
          onClose={closeModal}
        />
      )}

      {/* 추가 모달 */}
      {modalMode === modalModes.ADD && (
        <ReservationAddModal
          currentYear={currentYear}
          currentMonth={currentMonth}
          selectedDay={selectedDay}
          isOpen={modalMode === modalModes.ADD}
          onClose={closeModal}
          onAdd={handleAdd}
        />
      )}

      {/* 수정 모달 */}
      {modalMode === modalModes.EDIT && selectedReservation && (
        <ReservationUpdateModal
          reservation={selectedReservation}
          isOpen={modalMode === modalModes.EDIT}
          onClose={closeModal}
          onEdit={handleEdit}
        />
      )}

      {/* 삭제 모달 */}
      {modalMode === modalModes.DELETE && selectedReservation && (
        <ReservationDeleteModal
          reservation={selectedReservation}
          isOpen={modalMode === modalModes.DELETE}
          onClose={closeModal}
          onDelete={handleDelete}
        />
      )}
    </div>
  );
}

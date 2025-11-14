import { useEffect, useState } from "react";
import Calendar from "../component/Calendar";
import ReservationView from "../component/ReservationView";
import apiUrl from "../config/ApiUrl";
import axios from "axios";
import ReservationAddModal from "../component/modal/ReservationAddModal";

export default function Home() {
  const now = new Date();
  const KST = new Date(now.getTime() + 9 * 60 * 60 * 1000);
  const currentYearKST = KST.getFullYear();
  const currentMonthKST = KST.getMonth();
  const currentDayKST = KST.getDate();

  const [currentYear, setCurrentYear] = useState(currentYearKST);
  const [currentMonth, setCurrentMonth] = useState(currentMonthKST);
  const [selectedDay, setSelectedDay] = useState(currentDayKST);

  const [monthReservations, setMonthReservations] = useState([]);
  const [dayReservations, setDayReservations] = useState([]);

  const [isAddModalOpen, setIsAddModalOpen] = useState(false);

  const fetchMonthReservations = async (year, month) => {
    try {
      const yearMonth = `${year}-${String(month + 1).padStart(2, "0")}`;
      const response = await axios.get(`${apiUrl}/api/reservation/month`, {
        params: { yearMonth },
      });
      setMonthReservations(response.data);
    } catch (error) {
      console.error("월별 예약 내역 조회 실패:", error);
    }
  };

  const fetchDayReservations = async (date) => {
    try {
      const response = await axios.get(`${apiUrl}/api/reservation/date`, {
        params: { date },
      });
      setDayReservations(response.data);
    } catch (error) {
      console.error("날짜별 예약 내역 조회 실패:", error);
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

  const handleAdd = async (newReservation) => {
    try {
      const response = await axios.post(
        `${apiUrl}/api/reservation`,
        newReservation
      );
      alert("예약이 추가되었습니다.");
    } catch (error) {
      const errorMessage = `[ERROR] ${error.response.data.message}`;
      alert(errorMessage);
    }
  };

  useEffect(() => {
    fetchMonthReservations(currentYear, currentMonth);
    if (currentYear === currentYearKST && currentMonth === currentMonthKST) {
      setDayReservations(currentDayKST);
    } else {
      setDayReservations(null);
    }
  }, [currentYear, currentMonth]);

  useEffect(() => {
    if (selectedDay) {
      const dateFormatted = `${currentYear}-${String(currentMonth + 1).padStart(
        2,
        "0"
      )}-${String(selectedDay).padStart(2, "0")}`;
      fetchDayReservations(dateFormatted);
    }
  }, [selectedDay]);

  return (
    <div className="flex h-screen bg-gray-50">
      {/* 왼쪽 달력 */}
      <div className="flex-1 p-6">
        <Calendar
          currentYear={currentYear}
          currentMonth={currentMonth}
          selectedDay={selectedDay}
          onDateSelect={setSelectedDay}
          onPrevMonth={handlePrevMonth}
          onNextMonth={handleNextMonth}
          reservations={monthReservations}
          onAddClick={() => setIsAddModalOpen(true)}
        />
      </div>

      {/* 오른쪽 예약 내역 */}
      <div className="w-96">
        <ReservationView
          selectedDay={selectedDay}
          currentMonth={currentMonth}
          selectedDateReservations={dayReservations}
        />
      </div>
      <ReservationAddModal
        currentYear={currentYear}
        currentMonth={currentMonth}
        selectedDay={selectedDay}
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onAdd={handleAdd}
      />
    </div>
  );
}

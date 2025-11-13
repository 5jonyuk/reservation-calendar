import { useEffect, useState } from "react";
import Calendar from "../component/Calendar";
import ReservationView from "../component/ReservationView";
import apiUrl from "../config/ApiUrl";
import axios from "axios";

export default function Home() {
  const now = new Date();
  const KST = new Date(now.getTime() + 9 * 60 * 60 * 1000);
  const currentYearKST = KST.getUTCFullYear();
  const currentMonthKST = KST.getUTCMonth();

  const [currentYear, setCurrentYear] = useState(currentYearKST);
  const [currentMonth, setCurrentMonth] = useState(currentMonthKST);
  const [selectedDate, setSelectedDate] = useState(null);

  const [monthReservations, setMonthReservations] = useState([]);
  const [dayReservations, setDayReservations] = useState([]);

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
    setSelectedDate(null);
  };

  const handleNextMonth = () => {
    if (currentMonth === 11) {
      setCurrentMonth(0);
      setCurrentYear(currentYear + 1);
    } else {
      setCurrentMonth(currentMonth + 1);
    }
    setSelectedDate(null);
  };

  useEffect(() => {
    fetchMonthReservations(currentYear, currentMonth);
    setSelectedDate(null);
    setDayReservations([]);
  }, [currentYear, currentMonth]);

  useEffect(() => {
    if (selectedDate) {
      const dateFormatted = `${currentYear}-${String(currentMonth + 1).padStart(
        2,
        "0"
      )}-${String(selectedDate).padStart(2, "0")}`;
      fetchDayReservations(dateFormatted);
    }
  }, [selectedDate]);

  return (
    <div className="flex h-screen bg-gray-50">
      {/* 왼쪽 달력 */}
      <div className="flex-1 p-6">
        <Calendar
          currentYear={currentYear}
          currentMonth={currentMonth}
          selectedDate={selectedDate}
          onDateSelect={setSelectedDate}
          onPrevMonth={handlePrevMonth}
          onNextMonth={handleNextMonth}
          reservations={monthReservations}
        />
      </div>

      {/* 오른쪽 예약 내역 */}
      <div className="w-96">
        <ReservationView
          selectedDate={selectedDate}
          currentMonth={currentMonth}
          selectedDateReservations={dayReservations}
        />
      </div>
    </div>
  );
}

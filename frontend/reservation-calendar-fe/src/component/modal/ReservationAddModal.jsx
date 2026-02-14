import { useEffect, useState } from "react";
import Modal from "react-modal";
import customStyles from "./ModalStyle";

export default function ReservationAddModal({
  currentYear,
  currentMonth,
  selectedDay,
  isOpen,
  onClose,
  onAdd,
}) {
  const selectedDate = `${currentYear}-${String(currentMonth + 1).padStart(
    2,
    "0",
  )}-${String(selectedDay).padStart(2, "0")}`;

  const [formData, setFormData] = useState({
    customerName: "",
    customerPhone: "",
    menu: "",
    amount: "",
    pickupDate: selectedDate || "",
    pickupTime: "",
    paymentCompleted: false,
    pickupCompleted: false,
  });

  const [confirmOpen, setConfirmOpen] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSaveClick = () => {
    if (!formData.customerName || !formData.customerPhone || !formData.menu) {
      alert("이름과 연락처, 메뉴는 필수 입력값입니다.");
      return;
    }
    setConfirmOpen(true);
  };

  const handleConfirm = async () => {
    try {
      await onAdd(formData);
      setConfirmOpen(false);
      onClose();
      window.location.reload();
    } catch (e) {
      console.error(e);
      setConfirmOpen(false);
    }
  };

  const handleCancel = () => {
    setConfirmOpen(false);
  };

  useEffect(() => {
    setFormData((prev) => ({
      ...prev,
      pickupDate: selectedDate || "",
    }));
  }, [selectedDate]);

  return (
    <>
      <Modal
        isOpen={isOpen}
        onRequestClose={onClose}
        contentLabel="예약 추가"
        className="bg-white rounded-xl shadow-2xl w-110 max-w-full mx-4 relative p-6 max-h-screen"
        style={customStyles}
      >
        <h2 className="text-2xl font-semibold mb-6 text-center text-gray-800">
          예약 추가
        </h2>

        <div className="space-y-3">
          {/* 이름 */}
          <label className="block text-sm font-medium text-gray-600">
            이름 <span className="text-red-400"> *</span>
          </label>
          <input
            required
            name="customerName"
            value={formData.customerName}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          {/* 연락처 */}
          <label className="block text-sm font-medium text-gray-600">
            연락처 <span className="text-red-400"> *</span>
          </label>
          <input
            required
            type="tel"
            name="customerPhone"
            placeholder="010-1234-1234"
            value={formData.customerPhone}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          {/* 메뉴 */}
          <label className="block text-sm font-medium text-gray-600">
            메뉴 <span className="text-red-400"> *</span>
          </label>
          <input
            required
            name="menu"
            value={formData.menu}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          {/* 총 금액 */}
          <label className="block text-sm font-medium text-gray-600">
            총 금액 <span className="text-red-400"> *</span>
          </label>
          <input
            required
            name="amount"
            type="number"
            value={formData.amount}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          {/* 픽업 날짜 */}
          <label className="block text-sm font-medium text-gray-600">
            픽업 날짜
          </label>
          <input
            name="pickupDate"
            type="date"
            value={formData.pickupDate}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          {/* 픽업 시간 */}
          <label className="block text-sm font-medium text-gray-600">
            픽업 시간
          </label>
          <input
            name="pickupTime"
            type="time"
            value={formData.pickupTime}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          {/* 체크박스 */}
          <div className="space-x-4">
            <label>
              <input
                type="checkbox"
                name="paymentCompleted"
                checked={formData.paymentCompleted}
                onChange={handleChange}
              />{" "}
              결제 완료
            </label>

            <label>
              <input
                type="checkbox"
                name="pickupCompleted"
                checked={formData.pickupCompleted}
                onChange={handleChange}
              />{" "}
              픽업 완료
            </label>
          </div>
        </div>

        {/* 버튼 영역 */}
        <div className="flex justify-end space-x-2 mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-300 hover:bg-gray-400 rounded cursor-pointer transition"
          >
            취소
          </button>

          <button
            onClick={handleSaveClick}
            className="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded cursor-pointer transition"
          >
            추가
          </button>
        </div>
      </Modal>

      {/* 확인 모달 */}
      <Modal
        isOpen={confirmOpen}
        onRequestClose={handleCancel}
        contentLabel="추가 확인"
        className="bg-white rounded-lg shadow-2xl w-80 mx-4 relative p-6"
        style={customStyles}
      >
        <h3 className="text-lg font-semibold mb-4 text-gray-800">
          정말 추가하시겠습니까?
        </h3>
        <div className="flex justify-end space-x-2">
          <button
            onClick={handleCancel}
            className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 cursor-pointer"
          >
            취소
          </button>
          <button
            onClick={handleConfirm}
            className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 cursor-pointer"
          >
            확인
          </button>
        </div>
      </Modal>
    </>
  );
}

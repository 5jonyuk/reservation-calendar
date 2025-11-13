import { useState, useEffect } from "react";
import Modal from "react-modal";
import customStyles from "./ModalStyle";

export default function ReservationUpdateModal({
  reservation,
  isOpen,
  onClose,
  onEdit,
}) {
  const [formData, setFormData] = useState(reservation || {});
  const [confirmOpen, setConfirmOpen] = useState(false);

  useEffect(() => {
    if (reservation) {
      setFormData(reservation);
    }
  }, [reservation]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSaveClick = () => {
    setConfirmOpen(true);
  };

  const handleConfirm = () => {
    onEdit(reservation.id, formData);
    setConfirmOpen(false);
    onClose();
    alert("예약이 수정되었습니다.");
    window.location.reload();
  };

  const handleCancel = () => {
    setConfirmOpen(false);
  };

  if (!reservation) return null;

  return (
    <>
      <Modal
        isOpen={isOpen}
        onRequestClose={onClose}
        contentLabel="예약 수정"
        className="bg-white rounded-xl shadow-2xl w-110 max-w-full mx-4 relative p-6 max-h-screen"
        style={customStyles}
      >
        <h2 className="text-2xl font-semibold mb-6 text-center text-gray-800">
          예약 수정
        </h2>

        <div className="space-y-3">
          <label className="block text-sm font-medium text-gray-600 mb-1">
            이름
          </label>
          <input
            name="customerName"
            value={formData.customerName || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          <label className="block text-sm font-medium text-gray-600 mb-1">
            연락처
          </label>
          <input
            name="customerPhone"
            value={formData.customerPhone || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          <label className="block text-sm font-medium text-gray-600 mb-1">
            메뉴
          </label>
          <input
            name="menu"
            value={formData.menu || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          <label className="block text-sm font-medium text-gray-600 mb-1">
            예약금액
          </label>
          <input
            name="amount"
            type="number"
            value={formData.amount || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          <label className="block text-sm font-medium text-gray-600 mb-1">
            픽업날짜
          </label>
          <input
            name="pickupDate"
            type="date"
            value={formData.pickupDate || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          <label className="block text-sm font-medium text-gray-600 mb-1">
            픽업시간
          </label>
          <input
            name="pickupTime"
            type="time"
            value={formData.pickupTime || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />

          <div className="space-x-3">
            <label>
              <input
                type="checkbox"
                name="paymentCompleted"
                checked={formData.paymentCompleted || false}
                onChange={handleChange}
              />{" "}
              결제 완료
            </label>
            <label>
              <input
                type="checkbox"
                name="pickupCompleted"
                checked={formData.pickupCompleted || false}
                onChange={handleChange}
              />{" "}
              픽업 완료
            </label>
          </div>
        </div>

        <div className="flex justify-end space-x-2">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-300 rounded cursor-pointer"
          >
            취소
          </button>
          <button
            onClick={handleSaveClick}
            className="px-4 py-2 bg-blue-500 text-white rounded cursor-pointer"
          >
            저장
          </button>
        </div>
      </Modal>

      {/* 확인 모달 */}
      <Modal
        isOpen={confirmOpen}
        onRequestClose={handleCancel}
        contentLabel="수정 확인"
        className="bg-white rounded-lg shadow-2xl w-80 mx-4 relative p-6"
        style={customStyles}
      >
        <h3 className="text-lg font-semibold mb-4 text-gray-800">
          정말 수정하시겠습니까?
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

import Modal from "react-modal";
import customStyles from "./ModalStyle";

export default function ReservationDeleteModal({
  reservation,
  isOpen,
  onClose,
  onDelete,
}) {
  const handleCancel = () => {
    onClose();
  };

  const handleConfirm = () => {
    if (reservation && reservation.id) {
      onDelete(reservation.id);
    }
    onClose();
    alert("예약이 삭제되었습니다.");
    window.location.reload();
  };

  if (!reservation) return null;

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={handleCancel}
      contentLabel="삭제 확인"
      className="bg-white rounded-lg shadow-2xl w-95 mx-4 relative p-6"
      style={customStyles}
    >
      <h3 className="text-lg font-semibold mb-4 text-gray-800">
        정말 {reservation.customerName}님의 예약을 삭제하시겠습니까?
      </h3>
      <div className="flex justify-end space-x-2">
        <button
          onClick={handleCancel}
          className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 cursor-pointer transition"
        >
          취소
        </button>
        <button
          onClick={handleConfirm}
          className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 cursor-pointer transition"
        >
          확인
        </button>
      </div>
    </Modal>
  );
}

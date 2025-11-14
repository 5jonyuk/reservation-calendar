import Modal from "react-modal";
import customStyles from "./ModalStyle";

export default function ReservationDetailModal({
  reservation,
  isOpen,
  onClose,
}) {
  if (!reservation) return null;

  const formatDateTimeKST = (inputDateTime) => {
    const date = new Date(inputDateTime);
    const kstDate = new Date(date.getTime() + 9 * 60 * 60 * 1000);

    const year = kstDate.getFullYear();
    const month = String(kstDate.getMonth() + 1).padStart(2, "0");
    const day = String(kstDate.getDate()).padStart(2, "0");
    const hours = String(kstDate.getHours()).padStart(2, "0");
    const minutes = String(kstDate.getMinutes()).padStart(2, "0");

    const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}`;
    return formattedDate;
  };

  const formatTime = (inputTime) => {
    const [hours, minutes] = inputTime.split(":");
    const formattedHours = String(hours).padStart(2, "0");
    const formattedMinutes = String(minutes).padStart(2, "0");
    return `${formattedHours}시 ${formattedMinutes}분`;
  };

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onClose}
      contentLabel="예약 상세정보"
      style={customStyles}
      className="bg-white rounded-xl shadow-2xl w-110 max-w-full mx-4 relative p-6 max-h-[79vh] overflow-y-auto"
    >
      {/* 닫기 버튼 */}
      <button
        className="absolute top-4 right-6 text-gray-400 hover:text-gray-600 text-2xl font-bold cursor-pointer transition"
        onClick={onClose}
      >
        &times;
      </button>

      {/* 모달 제목 */}
      <h2 className="text-2xl font-semibold mb-6 text-center text-gray-800">
        예약 상세정보
      </h2>

      {/* 예약 정보 폼 */}
      <div className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            예약자명
          </label>
          <input
            type="text"
            value={reservation.customerName}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            연락처
          </label>
          <input
            type="text"
            value={reservation.customerPhone}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            메뉴
          </label>
          <input
            type="text"
            value={reservation.menu}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            예약 금액
          </label>
          <input
            type="text"
            value={`${reservation.amount.toLocaleString()}원`}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            픽업 날짜
          </label>
          <input
            type="text"
            value={reservation.pickupDate}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            픽업 시간
          </label>
          <input
            type="text"
            value={formatTime(reservation.pickupTime)}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            결제완료 여부
          </label>
          <input
            type="text"
            value={reservation.paymentCompleted ? "완료" : "미완료"}
            readOnly
            className={`w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 ${
              reservation.paymentCompleted ? "text-green-600" : "text-red-600"
            }`}
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            픽업완료 여부
          </label>
          <input
            type="text"
            value={reservation.pickupCompleted ? "완료" : "미완료"}
            readOnly
            className={`w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 ${
              reservation.pickupCompleted ? "text-green-600" : "text-red-600"
            }`}
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            예약 생성 시간
          </label>
          <input
            type="text"
            value={formatDateTimeKST(reservation.createdAt)}
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">
            최근 수정일
          </label>
          <input
            type="text"
            value={
              reservation.lastUpdatedAt
                ? formatDateTimeKST(reservation.lastUpdatedAt)
                : "수정 없음"
            }
            readOnly
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400 bg-gray-50 text-gray-800"
          />
        </div>
      </div>
    </Modal>
  );
}

import { useEffect, useRef, useState } from "react";

export default function ReservationCard({
  reservation,
  onSelectDetail,
  onEditClick,
  onDeleteClick,
}) {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [displayMenu, setDisplayMenu] = useState(reservation.menu ?? "");
  const menuRef = useRef(null);
  const menuSuffix = "···";

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

  useEffect(() => {
    const element = menuRef.current;
    if (!element) return;

    const context = document.createElement("canvas").getContext("2d");
    if (!context) {
      setDisplayMenu(reservation.menu ?? "");
      return;
    }

    const measureTextWidth = (text) => context.measureText(text).width;

    const updateMenuText = () => {
      const fullMenu = reservation.menu ?? "";
      const availableWidth = element.clientWidth;

      if (!fullMenu || availableWidth <= 0) {
        setDisplayMenu(fullMenu);
        return;
      }

      const computedStyle = window.getComputedStyle(element);
      context.font = `${computedStyle.fontWeight} ${computedStyle.fontSize} ${computedStyle.fontFamily}`;

      if (measureTextWidth(fullMenu) <= availableWidth) {
        setDisplayMenu(fullMenu);
        return;
      }

      let low = 0;
      let high = fullMenu.length;

      while (low < high) {
        const mid = Math.ceil((low + high) / 2);
        const candidate = `${fullMenu.slice(0, mid)}${menuSuffix}`;

        if (measureTextWidth(candidate) <= availableWidth) {
          low = mid;
        } else {
          high = mid - 1;
        }
      }

      setDisplayMenu(`${fullMenu.slice(0, low)}${menuSuffix}`);
    };

    updateMenuText();

    window.addEventListener("resize", updateMenuText);

    if (typeof ResizeObserver === "undefined") {
      return () => {
        window.removeEventListener("resize", updateMenuText);
      };
    }

    const resizeObserver = new ResizeObserver(updateMenuText);
    resizeObserver.observe(element);

    return () => {
      resizeObserver.disconnect();
      window.removeEventListener("resize", updateMenuText);
    };
  }, [reservation.menu]);

  return (
    <div
      className={`rounded-lg p-6 relative hover:shadow-sm transition cursor-pointer h-auto ${
        reservation.pickupCompleted ? "bg-gray-200/60" : "bg-white"
      }`}
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

      <div
        ref={menuRef}
        className="text-2xl font-bold mb-3 text-[#5C3A21] overflow-hidden whitespace-nowrap"
        title={reservation.menu}
      >
        {displayMenu}
      </div>
      <div className="text-xl font-bold mb-3 text-[#5C3A21]">
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

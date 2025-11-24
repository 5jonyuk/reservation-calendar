import React from "react";

export default function Spinner({
  size = "md",
  color = "text-blue-500",
  className = "",
}) {
  // Tailwind CSS 클래스를 사용하여 스피너의 크기를 동적으로 조절합니다.
  // size: 'sm', 'md', 'lg', 'xl' 등
  const sizeClasses = {
    sm: "w-4 h-4 border-2",
    md: "w-8 h-8 border-4", // 기본값
    lg: "w-12 h-12 border-4",
    xl: "w-16 h-16 border-8",
  };

  const currentSizeClass = sizeClasses[size] || sizeClasses.md; // 기본값 또는 전달된 크기 사용

  return (
    <div
      className={`
        animate-spin 
        rounded-full 
        ${currentSizeClass} 
        border-solid 
        border-current 
        border-r-transparent 
        ${color}
        ${className}
      `}
      role="status"
    >
      <span className="sr-only">Loading...</span>
    </div>
  );
}

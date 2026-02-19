export default function PhoneNumberInput({
  value,
  onChange,
  name = "customerPhone",
  placeholder = "'-' 없이 입력해주세요.",
  className,
  required = true,
}) {
  const formatPhoneNumber = (phoneNumber) => {
    const digitsOnly = phoneNumber.replace(/\D/g, "").slice(0, 11);

    if (digitsOnly.length < 3) return digitsOnly;
    if (digitsOnly.length === 3) return `${digitsOnly}-`;
    if (digitsOnly.length < 7) {
      return `${digitsOnly.slice(0, 3)}-${digitsOnly.slice(3)}`;
    }
    if (digitsOnly.length === 7) {
      return `${digitsOnly.slice(0, 3)}-${digitsOnly.slice(3)}-`;
    }
    return `${digitsOnly.slice(0, 3)}-${digitsOnly.slice(3, 7)}-${digitsOnly.slice(7)}`;
  };

  const getCursorPositionByDigits = (formattedPhone, digitCount) => {
    if (digitCount <= 0) return 0;

    let currentDigitCount = 0;
    for (let i = 0; i < formattedPhone.length; i += 1) {
      if (/\d/.test(formattedPhone[i])) {
        currentDigitCount += 1;
      }
      if (currentDigitCount === digitCount) {
        const basePosition = i + 1;
        if (formattedPhone[basePosition] === "-") {
          return basePosition + 1;
        }
        return basePosition;
      }
    }

    return formattedPhone.length;
  };

  const handlePhoneChange = (e) => {
    const input = e.target;
    const inputValue = input.value;
    const cursorPosition = input.selectionStart ?? inputValue.length;
    const digitsBeforeCursor = inputValue
      .slice(0, cursorPosition)
      .replace(/\D/g, "").length;
    const previousPhone = value || "";
    const isDeleteBackward =
      e.nativeEvent?.inputType === "deleteContentBackward";

    let nextPhone = formatPhoneNumber(inputValue);
    if (
      isDeleteBackward &&
      previousPhone.endsWith("-") &&
      inputValue === previousPhone.slice(0, -1)
    ) {
      nextPhone = inputValue;
    }

    onChange(nextPhone);

    requestAnimationFrame(() => {
      const nextCursor = getCursorPositionByDigits(
        nextPhone,
        digitsBeforeCursor,
      );
      input.setSelectionRange(nextCursor, nextCursor);
    });
  };

  return (
    <input
      required={required}
      type="tel"
      name={name}
      placeholder={placeholder}
      value={value || ""}
      onChange={handlePhoneChange}
      maxLength={13}
      className={className}
    />
  );
}

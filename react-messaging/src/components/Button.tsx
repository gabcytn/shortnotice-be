type ButtonProps = {
  title: string;
  className: string;
  type: "button" | "submit" | "reset";
  disabled?: boolean;
  onClick?: () => void;
};

function Button({ title, className, type, disabled, onClick }: ButtonProps) {
  return (
    <button
      className={`btn ${className}`}
      type={type}
      disabled={disabled}
      onClick={onClick}
    >
      {title}
    </button>
  );
}

export default Button;

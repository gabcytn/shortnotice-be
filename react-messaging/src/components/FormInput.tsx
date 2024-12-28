type FormInputProps = {
  id?: string;
  label?: string;
  disabled?: boolean;
  type: string;
  value: string;
  setValue: (val: string) => void;
};
function FormInput({
  id,
  label,
  type,
  value,
  setValue,
  disabled,
}: FormInputProps) {
  return (
    <>
      {label ? (
        <label htmlFor={id} className="form-label">
          {label}
        </label>
      ) : null}
      <input
        className="form-control"
        id={id ?? undefined}
        type={type}
        value={value}
        onChange={(e) => {
          setValue(e.target.value);
        }}
        disabled={disabled}
      />
    </>
  );
}

export default FormInput;

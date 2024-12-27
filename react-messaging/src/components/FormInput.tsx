type FormInputProps = {
  id?: string;
  label?: string;
  type: string;
  value: string;
  setValue: (val: string) => void;
};
function FormInput({ id, label, type, value, setValue }: FormInputProps) {
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
      />
    </>
  );
}

export default FormInput;

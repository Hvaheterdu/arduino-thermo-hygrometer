import { Field, Input as ChakraInput, type InputProps as ChakraInputProps } from "@chakra-ui/react";
import { type ForwardedRef, forwardRef, type ReactElement, useId } from "react";

export type InputProps = ChakraInputProps & {
  label: string;
  helperText?: string | undefined;
  errorText?: string | undefined;
  required?: boolean;
};

export const Input = forwardRef<HTMLInputElement, InputProps>(function Input(
  { label, helperText, errorText, required, id, ...rest }: InputProps,
  ref: ForwardedRef<HTMLInputElement>
): ReactElement {
  const generatedId = useId();
  const inputId = id ?? generatedId;
  const invalid = Boolean(errorText);

  return (
    <Field.Root invalid={invalid} required={required}>
      <Field.Label htmlFor={inputId}>
        {label}
        {required && <Field.RequiredIndicator />}
      </Field.Label>
      <ChakraInput id={inputId} ref={ref} {...rest} />
      {invalid ? (
        <Field.ErrorText>{errorText}</Field.ErrorText>
      ) : (
        helperText && <Field.HelperText>{helperText}</Field.HelperText>
      )}
    </Field.Root>
  );
});

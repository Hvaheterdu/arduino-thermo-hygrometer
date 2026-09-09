import { Button as ChakraButton, type ButtonProps as ChakraButtonProps } from "@chakra-ui/react";
import { type ForwardedRef, forwardRef, type ReactElement } from "react";

type ButtonProps = ChakraButtonProps;

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(function Button(
  { colorPalette = "brand", variant = "solid", ...rest }: ButtonProps,
  ref: ForwardedRef<HTMLButtonElement>
): ReactElement {
  return <ChakraButton colorPalette={colorPalette} ref={ref} variant={variant} {...rest} />;
});

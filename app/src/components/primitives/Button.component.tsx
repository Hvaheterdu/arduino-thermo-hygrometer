import { Button as ChakraButton, type ButtonProps as ChakraButtonProps } from "@chakra-ui/react";
import { type ForwardedRef, forwardRef, type ReactElement } from "react";

export const Button = forwardRef<HTMLButtonElement, ChakraButtonProps>(function Button(
  { colorPalette = "brand", variant = "solid", ...rest }: ChakraButtonProps,
  ref: ForwardedRef<HTMLButtonElement>
): ReactElement {
  return <ChakraButton colorPalette={colorPalette} ref={ref} variant={variant} {...rest} />;
});

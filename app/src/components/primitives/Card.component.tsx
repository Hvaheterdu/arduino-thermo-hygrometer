import { Card as ChakraCard, type CardRootProps } from "@chakra-ui/react";
import type { ReactElement, ReactNode } from "react";

type CardProps = CardRootProps & {
  heading?: ReactNode;
  footer?: ReactNode;
};

export const Card = ({ heading, footer, children, ...rest }: CardProps): ReactElement => {
  return (
    <ChakraCard.Root {...rest}>
      {heading && (
        <ChakraCard.Header>
          <ChakraCard.Title>{heading}</ChakraCard.Title>
        </ChakraCard.Header>
      )}
      <ChakraCard.Body>{children}</ChakraCard.Body>
      {footer && <ChakraCard.Footer>{footer}</ChakraCard.Footer>}
    </ChakraCard.Root>
  );
};

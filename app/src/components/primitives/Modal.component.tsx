import { Dialog, Portal } from "@chakra-ui/react";
import type { ReactElement, ReactNode } from "react";

type ModalProps = {
  open: boolean;
  onClose: () => void;
  title: string;
  footer?: ReactNode;
  children: ReactNode;
};

export const Modal = ({ open, onClose, title, footer, children }: ModalProps): ReactElement => {
  return (
    <Dialog.Root open={open} onOpenChange={(details) => !details.open && onClose()} placement="center">
      <Portal>
        <Dialog.Backdrop />
        <Dialog.Positioner>
          <Dialog.Content>
            <Dialog.Header>
              <Dialog.Title>{title}</Dialog.Title>
            </Dialog.Header>
            <Dialog.CloseTrigger aria-label="Close dialog" />
            <Dialog.Body>{children}</Dialog.Body>
            {footer && <Dialog.Footer>{footer}</Dialog.Footer>}
          </Dialog.Content>
        </Dialog.Positioner>
      </Portal>
    </Dialog.Root>
  );
};

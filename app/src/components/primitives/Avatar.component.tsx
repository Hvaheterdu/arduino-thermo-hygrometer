import { Avatar as ChakraAvatar, type AvatarRootProps } from "@chakra-ui/react";
import { faBatteryFull } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { ReactElement } from "react";

type AvatarProps = Omit<AvatarRootProps, "children"> & {
  batteryStatus: number;
};

const colorPaletteForBatteryStatus = (batteryStatus: number): string => {
  if (batteryStatus <= 20) {
    return "red";
  }
  if (batteryStatus <= 50) {
    return "orange";
  }

  return "brand";
};

export const Avatar = ({ batteryStatus, ...rest }: AvatarProps): ReactElement => {
  return (
    <ChakraAvatar.Root colorPalette={colorPaletteForBatteryStatus(batteryStatus)} {...rest}>
      <ChakraAvatar.Fallback aria-label={`Arduino device, battery at ${batteryStatus}%`}>
        <FontAwesomeIcon aria-hidden="true" icon={faBatteryFull} />
      </ChakraAvatar.Fallback>
    </ChakraAvatar.Root>
  );
};

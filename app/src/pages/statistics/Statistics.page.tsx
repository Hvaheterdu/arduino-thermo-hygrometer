import type { JSX } from "react";
import { type NavigateFunction, useNavigate } from "react-router";

export const Statistics = (): JSX.Element => {
  const navigate: NavigateFunction = useNavigate();

  return (
    <>
      <h1>This is the statistics page.</h1>
      <button
        onClick={(): void => {
          void navigate("/home");
        }}
      >
        Home page
      </button>
      <button
        onClick={(): void => {
          void navigate("/about");
        }}
      >
        About page
      </button>
    </>
  );
};

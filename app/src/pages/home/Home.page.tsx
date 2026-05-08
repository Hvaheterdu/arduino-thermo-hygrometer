import type { JSX } from "react";
import { type NavigateFunction, useNavigate } from "react-router";

export const Home = (): JSX.Element => {
  const navigate: NavigateFunction = useNavigate();

  return (
    <>
      <h1>This is the home page.</h1>
      <button
        onClick={(): void => {
          void navigate("/about");
        }}
      >
        About page
      </button>
      <button
        onClick={(): void => {
          void navigate("/statistics");
        }}
      >
        Statistics page
      </button>
    </>
  );
};

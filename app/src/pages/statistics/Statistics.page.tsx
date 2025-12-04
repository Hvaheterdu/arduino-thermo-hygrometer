import type { JSX } from "react";
import { useNavigate } from "react-router";

export const Statistics = (): JSX.Element => {
  const navigate = useNavigate();

  return (
    <>
      <h1>This is the statistics page.</h1>
      <button
        onClick={() => {
          void navigate("/home");
        }}
      >
        Home page
      </button>
      <button
        onClick={() => {
          void navigate("/about");
        }}
      >
        About page
      </button>
    </>
  );
};

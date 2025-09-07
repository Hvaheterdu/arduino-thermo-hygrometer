import type { JSX } from "react";
import { useNavigate } from "react-router";

export const Home = (): JSX.Element => {
  const navigate = useNavigate();

  return (
    <>
      <h1>This is the home page.</h1>
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

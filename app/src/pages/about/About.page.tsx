import { useNavigate } from "react-router";
import "./About.style.css";

export const About = () => {
  const navigate = useNavigate();

  return (
    <>
      <h1>This is the about page.</h1>
      <button
        className="button"
        onClick={() => {
          void navigate("/home");
        }}
      >
        Home page
      </button>
    </>
  );
};

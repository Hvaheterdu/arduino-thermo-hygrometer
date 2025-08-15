import { useNavigate } from "react-router";

export const About = () => {
  const navigate = useNavigate();

  return (
    <>
      <h1>This is the about page.</h1>
      <button
        onClick={() => {
          void navigate("/home");
        }}
      >
        Home page
      </button>
    </>
  );
};

import type { JSX } from "react";
import { NavLink, Outlet } from "react-router";

export const AppLayout = (): JSX.Element => {
  return (
    <div className="app-layout">
      <header className="top-nav-shell">
        <nav className="top-nav" aria-label="Primary">
          <NavLink
            className={({ isActive }) => (isActive ? "top-nav-link top-nav-link-active" : "top-nav-link")}
            end
            to="/"
          >
            Dashboard
          </NavLink>
          <NavLink
            className={({ isActive }) => (isActive ? "top-nav-link top-nav-link-active" : "top-nav-link")}
            to="/trends"
          >
            Trends
          </NavLink>
          <NavLink
            className={({ isActive }) => (isActive ? "top-nav-link top-nav-link-active" : "top-nav-link")}
            to="/sensor-health"
          >
            Sensor Health
          </NavLink>
        </nav>
      </header>
      <Outlet />
    </div>
  );
};

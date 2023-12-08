import React, { useContext, useEffect, useState } from "react";
import { useLocation, Navigate, useNavigate } from "react-router-dom";
import AuthContext from "./AuthContext";

const Authentication = ({ children }) => {
  const { appState, setAppState } = useContext(AuthContext);
  useEffect(() => {
    const fetchAuth = () => {
      fetch("/api/v1/loggedUser").then((response) => {
        if (response && response.status === 200)
          setAppState({ type: "AUTHENTICATED" });
      });
    };
    fetchAuth();
  }, [setAppState]);
  let location = useLocation();

  if (!appState.isAuthenticated) {
    return (
      <React.Fragment>
        <Navigate to="/auth/login" state={{ from: location }} replace />
      </React.Fragment>
    );
  }

  return <React.Fragment> {children} </React.Fragment>;
};

export default Authentication;

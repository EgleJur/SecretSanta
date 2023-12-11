import React, { useContext } from "react";
import { Route, Navigate } from "react-router-dom";
import AuthContext from "./AuthContext";

function PrivateRoute({ element, ...props }) {
  const { appState } = useContext(AuthContext);

  if (appState.isAuthenticated) {
    return <Route {...props} element={element} />;
  } else {
    return <Navigate to="/auth/login" />;
  }
}

export default PrivateRoute;

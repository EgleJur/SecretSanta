import { React, useContext, useState, useEffect } from "react";
import { Segment, Menu, Button } from "semantic-ui-react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import AuthContext from "../AuthContext";
import { NavLink } from "react-router-dom";

export function Navbar() {
  const { appState, setAppState } = useContext(AuthContext);
  const navigate = useNavigate();
  //   const [username, setUsername] = useState("");

  // const logoutHandler = async () => {
  //   fetch("/logout", {
  //     method: "POST",
  //   })
  //     .then(localStorage.clear)
  //     .then((response) => {
  //       setAppState({ type: "LOGOUT" });
  //       navigate("/", { replace: true });
  //     });
  // };
  const logoutHandler = () => {
    console.log("LOG OUT");
    localStorage.clear();

    setAppState({ type: "LOGOUT" });
    navigate("/", { replace: true });
  };

  return (
    <Segment style={{ backgroundColor: "rgb(250, 110, 110" }} inverted>
      <Menu style={{ backgroundColor: "rgb(250, 110, 110" }} inverted secondary>
        {/* <Menu.Item>
          <img alt="logo" src={car} />
        </Menu.Item> */}
        <Menu.Item
          as={NavLink}
          exact
          to="/"
          name="home"
          content="SECRET SANTA"
        />
        {/* )}      
          {!appState.isAuthenticated && ( */}
        <Menu.Item
          position="right"
          as={NavLink}
          exact
          to="/auth/login"
          name="login"
          content="Log in"
        />
        {/* )}
        {appState.isAuthenticated && ( */}
        <Menu.Item
          position="right"
          as={Button}
          onClick={logoutHandler}
          name="logout"
          content="Log out"
        />
        {/* )} */}
      </Menu>
    </Segment>
  );
}

import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import AuthContext from "../AuthContext";
import { Button, Form, Grid, Segment } from "semantic-ui-react";
import { jwtDecode } from "jwt-decode";

export function getUsername() {
  const token = localStorage.getItem("token");
  if (token !== null) {
    const decoded = jwtDecode(token);
    console.log("decoded", decoded)
    return decoded.sub;
  }
  return null;
}

export function setUsernameIndex() {
  const username = getUsername();
  const hash = username
    .split("")
    .reduce((acc, char) => acc + char.charCodeAt(0), 0);

  const index = (hash % 25) + 1;
  localStorage.setItem("avatar", index);
  return index;
}

export function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [user, setUser] = useState("");
  const [role, setRole] = useState("");
  const navigate = useNavigate();
  const { appState, setAppState } = useContext(AuthContext);
  const [state, setState] = useState({
    email: "",
    username: "",
    password: "",
    loading: false,
  });

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleRegisterClick = () => {
    navigate(`/auth/register`);
  };

  const handleSubmit = (event) => {
    if (!appState.isAuthenticated) {
      setAppState({ type: "LOADING", value: true });
      event.preventDefault();

      const credentials = {
        email: email,
        password: password,
      };

      fetch("/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("accessToken"),
        },
        body: JSON.stringify(credentials),
      })
        .then(applyResult)
        .catch((error) => {
          setError("An error occurred. Please try again later.");
        });
    } else {
      alert("You are already logged in");
      setAppState({ type: "AUTHENTICATED", value: true });
    }
  };

  const applyResult = (result) => {
    if (result.ok) {
      result.json().then((data) => {
        console.log("DATA:", data);
        localStorage.setItem("token", data);
        setUsernameIndex();
        console.log("USERNAME INDEX:", getUsername());
        console.log("LOCAL STORAGE", localStorage);
        setAppState({ type: "LOGIN", value: true });
        navigate(`/users/${data.userId}`);
      });
    } else {
      setError("Login failed. Please try again.");
    }
  };

  return (
    <Grid centered columns={2}>
      <Grid.Column>
        <h2>Log in</h2>
        <Segment style={{ backgroundColor: "rgb(250, 110, 110" }} inverted>
          <Form style={{ backgroundColor: "rgb(250, 110, 110" }} inverted>
            <Form.Group widths="equal">
              <Form.Field>
                <label>Email address</label>
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={email}
                  onChange={handleEmailChange}
                />
              </Form.Field>
              <Form.Field>
                <label>Password</label>
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={password}
                  onChange={handlePasswordChange}
                />
              </Form.Field>
            </Form.Group>
            <Button
              fluid
              type="submit"
              color="white"
              inverted
              onClick={handleSubmit}
            >
              Login
            </Button>
            <Button
              className="create mt-2"
              color="white"
              inverted
              fluid
              onClick={handleRegisterClick}
            >
              Register
            </Button>
          </Form>
        </Segment>
      </Grid.Column>
    </Grid>
  );
}

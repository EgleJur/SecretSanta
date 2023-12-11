import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import AuthContext from "../AuthContext";
import { Button, Form, Grid, Segment } from "semantic-ui-react";

export function LoginPage() {
  const [error, setError] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const { appState, setAppState } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleRegisterClick = () => {
    navigate(`/auth/register`);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!appState.isAuthenticated) {
      setAppState({ type: "LOADING", value: true });

      const credentials = {
        email: email,
        password: password,
      };

      try {
        const response = await fetch("/api/v1/auth/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(credentials),
        });

        if (response.ok) {
          const data = await response.json();
          localStorage.setItem("token", data.access_token);

          setAppState({ type: "LOGIN", value: true });
          navigate(`/users/${data.user.userId}`);
        } else {
          setError("Login failed. Please try again.");
        }
      } catch (error) {
        setError("An error occurred. Please try again later.");
      } finally {
        setAppState({ type: "LOADING", value: false });
      }
    } else {
      alert("You are already logged in");
      setAppState({ type: "AUTHENTICATED", value: true });
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

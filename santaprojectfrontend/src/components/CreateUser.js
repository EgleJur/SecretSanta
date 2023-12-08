import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Form, Grid, Segment } from "semantic-ui-react";

const JSON_HEADERS = {
  "Content-Type": "application/json",
};

export function CreateUser() {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState(""); 
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleUserNameChange = (event) => {
    setUserName(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const createUser = async () => {
    try {
      const response = await fetch("/api/v1/auth/register", {
        method: "POST",
        headers: JSON_HEADERS,
        body: JSON.stringify({
          name: userName,
          email: email,
          password: password,
          role: "USER",
        }),
      });
  
      if (response.ok) {
        console.log("User registered successfully");
        navigate("/auth/login");
      } else {
        const errorMessage = await response.text();
        setError(`User registration failed: ${response.status} - ${errorMessage}`);
      }
    } catch (error) {
      console.error("Error registering user:", error);
      setError("An unexpected error occurred. Please try again later.");
    }
  };
  

  return (
    <div>
      <Grid centered columns={2}>
        <Grid.Column>
          <h2>Register here</h2>
          <Segment style={{ backgroundColor: "rgb(250, 110, 110" }} inverted>
            <Form style={{ backgroundColor: "rgb(250, 110, 110" }} inverted>
              <Form.Group widths="equal">
                <Form.Field>
                  <label>User name</label>
                  <input
                    type="text"
                    name="username"
                    placeholder="User name"
                    value={userName}
                    onChange={handleUserNameChange}
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
                <Form.Field>
                  <label>Email</label>
                  <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={email}
                    onChange={handleEmailChange}
                  />
                </Form.Field>
              </Form.Group>
              <Form.Field>
                <Button
                  fluid
                  color="white"
                  inverted
                  onClick={createUser}
                >
                  Confirm
                </Button>
              </Form.Field>
            </Form>
            {error && <p style={{ color: "red" }}>{error}</p>}
          </Segment>
        </Grid.Column>
      </Grid>
    </div>
  );
}

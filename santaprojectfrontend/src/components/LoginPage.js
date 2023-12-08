import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import AuthContext from "../AuthContext";
import { Button, Form, Grid, Segment } from "semantic-ui-react";
import { jwtDecode } from "jwt-decode";

export function getUsername() {
  const token = localStorage.getItem("token");
  if (token !== null) {
    const decoded = jwtDecode(token);
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
  const [error, setError] = useState("");
  const [user, setUser] = useState("");
  const [role, setRole] = useState("");
  const navigate = useNavigate();

  const [state, setState] = useState({
    username: "",
    password: "",
    loading: false,
  });

  const listUrl = useHref("/menus/list");

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  //   const handleSubmit = (event) => {
  //     if(!appState.isAuthenticated){
  //     setAppState({ type: "LOADING", value: true })
  //     event.preventDefault();

  //     const credentials = {
  //       username: username,
  //       password: password
  //     };

  // fetch('api/v1/login', {
  //   method: 'POST',
  //   headers: {
  //     'Content-Type': 'application/json',
  //     // 'Accept': 'application/json'
  //   },
  //   body: JSON.stringify(credentials)
  // })
  //   .then(applyResult)
  //   .catch((error) => {
  //     setError('An error occurred. Please try again later.');
  //   })
  // }
  // else{
  //     alert("You are already logged in");
  // setAppState({ type: "AUTHENTICATED", value: true })
  //     }
  //   };

  //   const applyResult = (result) => {
  //     const clear = () => {
  //         clearForm();
  //     }
  //         if (result.ok) {
  //            setAppState({type: "LOGIN", value: true })
  //            navigate('/menus/list');
  //         } else {
  //           setError('Login failed. Please try again.');
  //         }
  //   }
  //   const clearForm = () => {
  //     setState({
  //         ...state, user: '',
  //         password: null,
  //     })
  // }

  // const { appState, setAppState } = useContext(AuthContext)

  return (
    <Grid centered columns={2}>
      <Grid.Column>
        <h2>Log in</h2>
        <Segment color="red" inverted>
          <Form color="red" inverted>
            <Form.Group widths="equal">
              <Form.Field>
                <label>User name</label>
                <input
                  type="text"
                  name="username"
                  placeholder="User name"
                  value={username}
                  onChange={handleUsernameChange}
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
              type="submit"
              color="white"
              inverted
              //   onClick={handleSubmit}
            >
              Confirm
            </Button>
          </Form>
        </Segment>
      </Grid.Column>
    </Grid>
  );
}

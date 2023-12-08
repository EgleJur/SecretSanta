import logo from "./logo.svg";
import "./App.css";
import { useReducer } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { GroupList } from "./components/GroupList";
import { ViewGroup } from "./components/ViewGroup";
import { CreateUser } from "./components/CreateUser";
import { CreateGift } from "./components/CreateGift";
import { CreateGroup } from "./components/CreateGroup";
import { ViewUser } from "./components/ViewUser";
import { EditGift } from "./components/EditGift";
import { LoginPage } from "./components/LoginPage";
import { Navbar } from "./components/Navbar";
import ChatRoom from "./components/ChatRoom";
import { Container } from "semantic-ui-react";
import AuthContext from "./AuthContext";

// const apiUrl = "http://localhost:8085";

function App() {
  var initState = {
    isAuthenticated: false,
    username: null,
    // role: null,
  };

  const auth = (appState, action) => {
    switch (action.type) {
      case "LOGIN":
        return {
          ...appState,
          isAuthenticated: true,
          username: action.value.username,
          // role: action.value.role,
        };
      case "LOADING":
        return {
          ...appState,
          isLoading: action.value,
        };
      case "LOGOUT":
        return {
          ...appState,
          isAuthenticated: false,
          username: null,
          // role: null,
        };
      case "AUTHENTICATED":
        return {
          ...appState,
          isAuthenticated: true,
          // admin: true,
        };
      default:
        return appState;
    }
  };
  const [appState, setAppState] = useReducer(auth, initState);

  return (
    <div className="App">
      <Container>
        <AuthContext.Provider value={{ appState, setAppState }}>
          <Router>
            <Navbar />
            <div className="container">
              <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/auth/login" element={<LoginPage />} />
                <Route path="/auth/register" element={<CreateUser />} />
                <Route path="/users/:id" element={<ViewUser />} />
                <Route path="/users/:userId/groups/:groupId" element={<ViewGroup />} />
                <Route path="/create/group/:id" element={<CreateGroup />} />
                <Route path="/create/gift/:id" element={<CreateGift />} />
                <Route path="/users/:userId/gifts/:giftId" element={<EditGift />} />
                {/* <Route path="/chatroom" element={<ChatRoom />} /> */}
              </Routes>
            </div>
          </Router>
        </AuthContext.Provider>
      </Container>
    </div>
  );
}

export default App;
// export { apiUrl };

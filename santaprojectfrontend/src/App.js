import logo from "./logo.svg";
import "./App.css";
import { HashRouter, Route, Routes } from "react-router-dom";
import { GroupList } from "./components/GroupList";
import { ViewGroup } from "./components/ViewGroup";
import { CreateUser } from "./components/CreateUser";
import { CreateGift } from "./components/CreateGift";
import { CreateGroup } from "./components/CreateGroup";
import { ViewUser } from "./components/ViewUser";
import { EditGift } from "./components/EditGift";
import { LoginPage } from "./components/LoginPage";
import { Navbar } from "./components/Navbar";

const apiUrl = "http://localhost:8080";

function App() {
  return (
    <div className="App">
      <HashRouter>
        <Navbar />
        <div className="container">
          <Routes>
            <Route path="/" element={<GroupList />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/users" element={<CreateUser />} />
            <Route path="/create/gift/:id" element={<CreateGift />} />
            <Route path="/groups/:id" element={<ViewGroup />} />
            <Route path="/create/group" element={<CreateGroup />} />
            <Route path="/users/:id" element={<ViewUser />} />
            <Route path="/users/:userId/gifts/:giftId" element={<EditGift />} />
          </Routes>
        </div>
      </HashRouter>
    </div>
  );
}

export default App;
export { apiUrl };

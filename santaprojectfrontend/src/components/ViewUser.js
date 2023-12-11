import React, { useState, useEffect, useContext } from "react";
import { Card, Image } from "semantic-ui-react";
import { useParams } from "react-router-dom";
import {  useNavigate } from "react-router-dom";
import { GiftList } from "./GiftList";
import { GroupList } from "./GroupList";
import AuthContext from "../AuthContext";

export function ViewUser() {
  const navigate = useNavigate();
  const params = useParams();
  const [user, setUser] = useState({
    name: "",
    email: "",
  });

  // const { appState, setAppState } = useContext(AuthContext);

  const fetchUser = async () => {
    try {
      const response = await fetch("/api/v1/users/" + params.id, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const jsonResponse = await response.json();
      setUser(jsonResponse);
      console.log("Fetched User:", jsonResponse);
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  };

  useEffect(() => {
    fetchUser();
  }, [params]);

  const handleCreateGroupClick = () => {
    navigate(`/create/group/${params.id}`);
  };

  const handleCreateGiftClick = () => {
    navigate(`/create/gift/${params.id}`);
  };

  return (
    <div className="ui centered container">
      <div>
        <Card
          style={{
            width: "1300px",
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
          }}
          centered
        >
          <Card.Content>
            <Image src="/images/user.png" circular size="small" centered />
          </Card.Content>
          <Card.Content>
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                height: "100%",
              }}
            >
              <div>
                <Card.Header
                  style={{
                    fontSize: "1.5em",
                    // marginBottom: "5px",
                    textAlign: "center",
                    // fontFamily: "Times-Roman",
                    // fontWeight: "bold",
                  }}
                >
                  Welcome <b>{user.name}</b>!
                </Card.Header>
                <Card.Meta
                  style={{
                    fontSize: "1.2em",
                    color: "red",
                    fontFamily: "Times-Italic",
                  }}
                >
                  <span className="date">
                    <br />
                    Are you ready to be <b>Secret Santa </b>
                    for someone special?
                  </span>
                </Card.Meta>
              </div>
            </div>
          </Card.Content>
        </Card>
      </div>
      <div className="ui two column stackable grid"></div>
      <GroupList handleCreateGroupClick={handleCreateGroupClick} />
      <GiftList handleCreateGiftClick={handleCreateGiftClick} />
    </div>
  );
}

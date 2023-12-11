import React, { useState, useEffect } from "react";
import { Form, Button, Card, Icon } from "semantic-ui-react";
import { Link, useNavigate, useParams } from "react-router-dom";
import "./SecretSanta.css";

const JSON_HEADERS = {
  "Content-Type": "application/json",
  Authorization: "Bearer " + localStorage.getItem("token"),
};

export function CreateGroup() {
  const params = useParams();
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [eventDate, setEventDate] = useState("");
  const [budget, setBudget] = useState("");
  const [user, setUser] = useState("");
  const [groupId, setGroupId] = useState("");
  const [ownerId, setOwnerId] = useState(parseInt(params.id));
  const [userList, setUserList] = useState([]);

  const fetchUsers = async () => {
    try {
      const response = await fetch("/api/v1/users", {
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
      setUserList(jsonResponse);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const createGroup = () => {
    console.log("Selected Users:", user);
    console.log("Owner:", ownerId);
    const requestBody = {
      groupId,
      name,
      eventDate,
      budget,
      user: user || [],
      ownerId,
    };
    console.log("Request Body:", JSON.stringify(requestBody));

    fetch("/api/v1/groups", {
      method: "POST",
      headers: JSON_HEADERS,
      body: JSON.stringify(requestBody),
    }).then(() => navigate(`/users/${params.id}`));
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div className="ui one column centered equal width grid">
      <Card className="m-3 p-3">
        <Form
          className="m-3 p-3"
          onSubmit={(e) => {
            e.preventDefault();
            createGroup();
          }}
        >
          <Form.Field className="m-3">
            <label>Event Name</label>
            <input
              placeholder="Event Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </Form.Field>
          <Form.Field>
            <label>Event date</label>
            <input
              placeholder="Event date"
              value={eventDate}
              onChange={(e) => setEventDate(e.target.value)}
            />
          </Form.Field>
          <Form.Field>
            <label>Agreed budget</label>
            <input
              placeholder="Agreed budget"
              value={budget}
              onChange={(e) => setBudget(e.target.value)}
            />
          </Form.Field>
          <Form.Dropdown
            label="Participants"
            value={user}
            onChange={(e, { value }) => setUser(value)}
            placeholder="Participants"
            options={userList.map((user) => ({
              key: user.userId,
              text: user.name,
              value: user,
            }))}
            selection
            multiple
          />

          <Button
            icon
            basic
            labelPosition="left"
            className=""
            as={Link}
            to={`/users/${params.id}`}
          >
            <Icon name="arrow left" />
            Back
          </Button>
          <Button className="button" type="submit" basic color="red">
            Submit
          </Button>
        </Form>
      </Card>
    </div>
  );
}

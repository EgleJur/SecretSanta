import React, { useState, useEffect } from "react";
import { Button, Image, Card, Icon, Input } from "semantic-ui-react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { WishList } from "./WishList";

export function ViewGroup() {
  const params = useParams();

  const [addingUser, setAddingUser] = useState(false);
  const [newUserName, setNewUserName] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [generated, setGenerated] = useState(false);
  const [assignedRecipient, setAssignedRecipient] = useState("");
  const [users, setUsers] = useState([]);
  const [group, setGroup] = useState({
    groupId: "",
    name: "",
    eventDate: "",
    budget: "",
    user: [],
    gifts: [],
    ownerId: "",
    generatedSanta: [],
  });

  const [myRecipient, setMyRecipient] = useState(null);
  const [santaPairs, setSantaPairs] = useState({});

  const fetchGroups = async () => {
    const groupId = parseInt(params.groupId);
    console.log("groupId:", groupId, typeof groupId);
    fetch("/api/v1/groups/" + groupId)
      .then((response) => response.json())
      .then(setGroup);
  };

  const fetchUsers = async () => {
    fetch("/api/v1/users")
      .then((response) => response.json())
      .then(setUsers);
  };

  const handleAddNewUser = () => {
    setAddingUser(true);
  };

  const handleNewUserInputChange = (e) => {
    setNewUserName(e.target.value);
  };

   const fetchFilteredUsers = async () => {
    if (newUserName.trim() !== "") {
      try {
        const response = await fetch(
          `/api/v1/users/search?name=${newUserName}`
        );
        if (response.ok) {
          const matchingUsers = await response.json();
          setFilteredUsers(matchingUsers);
        } else {
          console.error("Failed to fetch filtered users.");
        }
      } catch (error) {
        console.error("Error fetching filtered users:", error);
      }
    } else {
      setFilteredUsers([]);
    }
  };

  const handleAddUser = async (selectedUser) => {
    setNewUserName(selectedUser.name);
    setFilteredUsers([]);

    try {
      const response = await fetch(
        `/api/v1/users/search?name=${selectedUser.name}`
      );

      if (response.ok) {
        const users = await response.json();
        const user = Array.isArray(users) && users.length > 0 ? users[0] : null;
        if (user) {
          const addResponse = await fetch(
            `/api/v1/groups/${parseInt(params.groupId)}/users/${parseInt(
              user.userId
            )}/newUsers`,
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(user),
            }
          );

          if (addResponse.ok) {
            const updatedGroup = await addResponse.json();
            setGroup(updatedGroup);
            setAddingUser(false);
            setNewUserName("");
          } else {
            console.error("Failed to add user to the group.");
          }
        } else {
          console.error("User not found.");
        }
      } else {
        console.error("Failed to fetch user details.");
      }
    } catch (error) {
      console.error("Error adding user:", error);
    }
  };

  const checkSantaPairs = async () => {
    const userId = parseInt(params.userId);
    const groupId = parseInt(params.groupId);
    console.log("!!!userId", userId);

    try {
      const response = await fetch(
        `/api/v1/generate_santa/santa_group/${userId}?groupId=${groupId}`
      );

      if (response.ok) {
        const santaPairs = await response.json();
        console.log("santapair", santaPairs);

        // Check if santaPairs is an object and has 'santa' and 'recipient' properties
        if (
          santaPairs &&
          typeof santaPairs === "object" &&
          santaPairs.hasOwnProperty("santa") &&
          santaPairs.hasOwnProperty("recipient")
        ) {
          setAssignedRecipient(santaPairs.recipient);
          setGenerated(true);
        } else {
          console.error("Invalid format of Santa pairs data.");
        }
      } else {
        console.error("Failed to fetch Santa pairs.");
      }
    } catch (error) {
      console.error("Error checking Santa pairs:", error);
    }
  };

  const generateSantaAndAssignRecipient = async () => {
    try {
      const groupId = parseInt(params.groupId);

      const generateSantaResponse = await fetch(
        `/api/v1/generate_santa/random/${groupId}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!generateSantaResponse.ok) {
        console.error("Failed to generate Santa.");
        return null;
      }

      const generatedSanta = await generateSantaResponse.json();

      const assignRecipientResponse = await fetch(
        `/api/v1/generate_santa/santa_group/${params.userId}?groupId=${groupId}`
      );

      if (!assignRecipientResponse.ok) {
        console.error("Failed to fetch generateSanta.");
        return null;
      }

      const assignedRecipient = await assignRecipientResponse.json();

      console.log("Generated Santa:", generatedSanta);
      console.log("Assigned Recipient:", assignedRecipient);

      return assignedRecipient?.recipient?.name || "SOMEONE";
    } catch (error) {
      console.error("Error in generateSantaAndAssignRecipient:", error);
      return "SOMEONE";
    }
  };

  const handleGenerateButtonClick = async () => {
    if (group.ownerId === parseInt(params.userId)) {
      const recipient = await generateSantaAndAssignRecipient();
      setMyRecipient(recipient);
      setGenerated(true);
    }
  };

  useEffect(() => {
    fetchGroups();
    fetchUsers();
  }, [parseInt(params.groupId)]);

  useEffect(() => {
    checkSantaPairs();
  }, [parseInt(params.groupId)]);

  useEffect(() => {
    newUserName.length > 0 ? fetchFilteredUsers() : fetchUsers();
  }, [newUserName]);

  useEffect(() => {
    if (generated && assignedRecipient && assignedRecipient.recipient) {
      setGenerated(true);
    }
  }, [generated, assignedRecipient]);

  useEffect(() => {
    if (generated) {
      checkSantaPairs();
    }
  }, [generated]);

  return (
    <div className="ui one column centered equal width grid">
      <div className="d-flex justify-content-center m-3 centered">
        <div key={group.groupId} className="m-3 cursor-pointer">
          <Card>
            <Image src="/images/santa.jpg" wrapped ui={false} />
            <Card.Content>
              <Card.Header>{group.name}</Card.Header>
              <Card.Meta>
                <span className="date">
                  Event date is set to {group.eventDate}
                </span>
              </Card.Meta>
              <Card.Description>
                Event budget is {group.budget}Є
              </Card.Description>
            </Card.Content>
            <Card.Content extra className="info">
              <a>
                <h3>Participants:</h3>
                <Icon name="user" />
                {group.user.map((user) => (
                  <Button
                    className="button"
                    content="Standard"
                    basic
                    key={user.id}
                  >
                    {user.name}
                  </Button>
                ))}
                {group.ownerId && group.ownerId === parseInt(params.userId) ? (
                  addingUser ? (
                    <div>
                      <Input
                        placeholder="Enter name"
                        value={newUserName}
                        onChange={handleNewUserInputChange}
                        onKeyPress={(e) => {
                          if (e.key === "Enter") {
                            handleAddUser();
                          }
                        }}
                      />

                      <div>
                        {filteredUsers.map((user) => (
                          <div
                            key={user.id}
                            onClick={() => handleAddUser(user)}
                          >
                            {user.name}
                          </div>
                        ))}
                      </div>
                    </div>
                  ) : (
                    <Button
                      content="Standard"
                      basic
                      className="button"
                      onClick={handleAddNewUser}
                      color="red"
                    >
                      Add new
                    </Button>
                  )
                ) : null}
              </a>
            </Card.Content>

            <button
              fluid
              className="generate-button"
              size="large"
              onClick={handleGenerateButtonClick}
            >
              {group.ownerId === parseInt(params.userId)
                ? generated
                  ? `You are secret Santa to ${
                      assignedRecipient ? assignedRecipient.name : ""
                    }`
                  : "GENERATE"
                : generated
                ? `You Secret Santa to ${
                    assignedRecipient ? assignedRecipient.name : ""
                  }`
                : "Naughty or Nice List is being prepared"}
            </button>
          </Card>
        </div>
      </div>
      {generated && assignedRecipient && (
        <WishList recipientId={assignedRecipient.userId} />
      )}
    </div>
  );
}

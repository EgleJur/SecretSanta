import { useState, useEffect } from "react";
import { Card, Image } from "semantic-ui-react";
import { useNavigate, useParams } from "react-router-dom";
import "./SecretSanta.css";

export function GroupList({ handleCreateGroupClick }) {
  const navigate = useNavigate();
  const params = useParams();
  const [groups, setGroups] = useState([]);
  const [imageUrls, setImageUrls] = useState([
    "/images/santa1.jpg",
    "/images/santa2.jpg",
    "/images/santa3.jpg",
    "/images/santa4.jpg",
    "/images/santa5.jpg",
    "/images/santa6.jpg",
    "/images/santa7.jpg",
  ]);

  const fetchGroups = async () => {
    try {
      const response = await fetch(`/api/v1/groups/user/${params.id}/groups`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem('token'), 
        },
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const jsonResponse = await response.json();
      setGroups(jsonResponse);
      console.log("Fetched Groups:", jsonResponse);
    } catch (error) {
      console.error("Error fetching groups:", error);
    }
  };

  const getRandomGroupImageUrl = () => {
    const randomIndex = Math.floor(Math.random() * imageUrls.length);
    return imageUrls[randomIndex];
  };

  const handleGroupClick = (userId, groupId) => {
    console.log("groupId:", typeof groupId);
    console.log(`Card clicked for group ID ${groupId}`);
    navigate(`/users/${userId}/groups/${groupId}`);
  };

  useEffect(() => {
    fetchGroups();
  }, [params.id]);

  return (
    <div>
      <div>
        <Card style={{ width: "1300px", backgroundColor: "rgb(250, 110, 110" }}>
          <Card.Content>
            <Card.Header style={{ color: "white" }}>
              Attending events:
            </Card.Header>
          </Card.Content>
          <Card.Content>
            <Card.Group style={{ display: "flex", justifyContent: "center" }}>
              {groups.map((group) => (
                <Card
                  key={group.id}
                  className="m-3 cursor-pointer"
                  onClick={() => handleGroupClick(params.id, group.groupId)}
                  style={{ width: "200px", height: "350px" }}
                >
                  <Image src={getRandomGroupImageUrl()} wrapped ui={false} />
                  <Card.Content>
                    <Card.Header>{group.name}</Card.Header>
                    <Card.Meta>
                      <span className="date">
                        Event date is set to
                        <br /> {group.eventDate}
                      </span>
                    </Card.Meta>
                    <Card.Description>
                      Event budget is {group.budget}Є
                    </Card.Description>
                  </Card.Content>
                </Card>
              ))}
              <Card
                className="m-3 cursor-pointer"
                onClick={() => handleCreateGroupClick(params.id)}
                style={{
                  width: "200px",
                  height: "350px",
                  textAlign: "center",
                  cursor: "pointer",
                  backgroundColor: "rgba(250, 140, 140)",
                }}
              >
                <Card.Content
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <Card.Header
                    style={{
                      fontSize: "20px",
                      //   paddingTop: "150px",
                      color: "grey",
                    }}
                  >
                    Add New Group
                  </Card.Header>
                </Card.Content>
              </Card>
            </Card.Group>
          </Card.Content>
        </Card>
      </div>
    </div>
  );
}

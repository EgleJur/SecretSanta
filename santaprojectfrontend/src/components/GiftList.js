import React, { useState, useEffect } from "react";
import { Card, Image } from "semantic-ui-react";
import { useParams, useNavigate } from "react-router-dom";

export function GiftList() {
  const params = useParams();
  const [gifts, setGifts] = useState([]);
  const [recipient, setRecipient] = useState(null);

  const [giftImage, setGiftImage] = useState([
    "/images/gift1.jpg",
    "/images/gift2.jpg",
    "/images/gift3.jpg",
    "/images/gift4.jpg",
    "/images/gift5.jpg",
    "/images/gift6.jpg",
    "/images/gift7.jpg",
    "/images/gift8.jpg",
    "/images/gift9.jpg",
  ]);

  const navigate = useNavigate();

  const fetchGifts = async () => {
    try {
      const response = await fetch("/api/v1/gifts/createdBy/" + params.id, {
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
      setGifts(jsonResponse);
      console.log("Fetched Gifts:", jsonResponse);
    } catch (error) {
      console.error("Error fetching gifts:", error);
    }
  };

  const fetchRecipient = async () => {
    try {
      const response = await fetch(`/api/v1/users/${params.id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem('token'), 
        },
      });
      
      if (!response.ok) {
        throw new Error(`Failed to fetch user. Status: ${response.status}`);
      }
  
      const user = await response.json();
      setRecipient(user);
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  };
  
  

  const getRandomGiftImageUrl = () => {
    const randomIndex = Math.floor(Math.random() * giftImage.length);
    return giftImage[randomIndex];
  };

  const handleGiftClick = (userId, giftId) => {
    console.log(`Card clicked for gift ID ${giftId} of user ID ${userId}`);
    navigate(`/users/${userId}/gifts/${giftId}`);
  };

  const handleCreateGiftClick = (userId) => {
    console.log(`Card clicked for gift ID ${userId}`);
    navigate(`/create/gift/${userId}`);
  };

  useEffect(() => {
    fetchGifts();
    fetchRecipient();
  }, [params.id]);

  return (
    <div>
      <div>
        <Card style={{ width: "1300px", backgroundColor: "rgb(250, 110, 110" }}>
          <Card.Content>
            <Card.Header style={{ color: "white" }}>
            {recipient ? `${recipient.name}'s Gift Wishlist` : "Gift Wishlist"}
            </Card.Header>
          </Card.Content>
          <Card.Content>
            <Card.Group style={{ display: "flex", justifyContent: "center" }}>
              {gifts.map((gift) => (
                <Card
                  key={gift.id}
                  className="m-3 cursor-pointer"
                  onClick={() => handleGiftClick(params.id, gift.giftId)}
                  style={{ width: "150px", height: "240px" }}
                >
                  <Image src={getRandomGiftImageUrl()} wrapped ui={false} />
                  <Card.Content>
                    <Card.Header>{gift.name}</Card.Header>
                    <Card.Meta>
                      <span className="date">
                        Created for <b> {gift.group.name} </b>
                      </span>
                    </Card.Meta>
                  </Card.Content>
                </Card>
              ))}
              <Card
                className="m-3 cursor-pointer"
                onClick={() => handleCreateGiftClick(params.id)}
                style={{
                  width: "150px",
                  height: "240px",
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
                      color: "grey",
                    }}
                  >
                    Add New Gift
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

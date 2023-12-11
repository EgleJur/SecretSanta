import React, { useState, useEffect } from "react";
import { Card, Image } from "semantic-ui-react";
import { useParams, useNavigate } from "react-router-dom";

export function WishList({ recipientId }) {
  const params = useParams();
  const [gifts, setGifts] = useState([]);
  const [user, setUser] = useState();

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

  const MAX_DISPLAY_LENGTH = 20; 

  const shortenLink = (link) => {
    if (link.length <= MAX_DISPLAY_LENGTH) {
      return link;
    } else {
      return link.substring(0, MAX_DISPLAY_LENGTH) + '...';
    }
  };

  const navigate = useNavigate();

  const fetchGifts = async () => {
    try {
      const id = parseInt(recipientId);
      const response = await fetch("/api/v1/gifts/createdBy/" + id, {
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
      setGifts(jsonResponse);
      console.log("Fetched Gifts:", jsonResponse);
    } catch (error) {
      console.error("Error fetching gifts:", error);
    }
  };

  const fetchUser = async () => {
    try {
      const id = parseInt(recipientId);
      const response = await fetch(`/api/v1/users/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const user = await response.json();
      setUser(user);
      console.log("Reciever: ", user);
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
    if (recipientId) {
      fetchGifts();
    }
  }, [recipientId]);

  useEffect(() => {
    fetchUser();
  }, [params.id]);

  return (
    <div>
      <div>
        <Card style={{ width: "1300px", backgroundColor: "rgb(250, 110, 110" }}>
          <Card.Content>
            <Card.Header style={{ color: "white" }}>
              {gifts.length > 0
                ? `${gifts[0].createdBy.name}'s Gift Wishlist`
                : "Gift Wishlist"}
            </Card.Header>
          </Card.Content>

          <Card.Content>
            <Card.Group style={{ display: "flex", justifyContent: "center" }}>
              {gifts.map((gift) => (
                <Card
                  key={gift.id}
                  className="m-3 cursor-pointer"
                  style={{ width: "170px", height: "270px" }}
                >
                  <Image src={getRandomGiftImageUrl()} wrapped ui={false} />
                  <Card.Content>
                    <Card.Header>{gift.name}</Card.Header>
                    <Card.Meta>
                      <span className="date">
                        <b> {gift.description} </b>
                      </span>
                    </Card.Meta>
                    <Card.Meta>
                      <span className="date">
                        <b> {shortenLink(gift.link)} </b>
                      </span>
                    </Card.Meta>
                  </Card.Content>
                </Card>
              ))}
            </Card.Group>
          </Card.Content>
        </Card>
      </div>
    </div>
  );
}

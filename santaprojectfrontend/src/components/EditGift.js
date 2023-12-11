import React, { useState, useEffect } from "react";
import { Button, Image, Card, Form, Icon } from "semantic-ui-react";
import { useNavigate, useParams } from "react-router-dom";

const JSON_HEADERS = {
    "Content-Type": "application/json",
    Authorization: "Bearer " + localStorage.getItem("token"),
  };

export function EditGift() {
    const params = useParams();
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [link, setLink] = useState("");
    const [price, setPrice] = useState("");
    const [createdBy, setCreatedBy] = useState("");
    const [groupId, setGroup] = useState("");
    const [giftId, setGiftId] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchGiftDetails(params.userId, params.giftId);
    }, [params.userId, params.giftId]);

    const fetchGiftDetails = async (userId, id) => {
        try {
            const response = await fetch(`/api/v1/gifts//users/${userId}/gifts/${id}`, {
                method: "GET",
                headers: {
                  "Content-Type": "application/json",
                  Authorization: "Bearer " + localStorage.getItem('token'), 
                },
              });
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const giftData = await response.json();
            setGiftId(giftData.giftId);
            setName(giftData.name);
            setDescription(giftData.description);
            setLink(giftData.link);
            setPrice(giftData.price);
            setCreatedBy(giftData.createdBy);
            setGroup(giftData.groupId);
            
        } catch (error) {
            console.error("Error fetching gift details:", error);
        }
    };

    const editGift = () => {
         console.log("group:", groupId);
        fetch(`/api/v1/gifts`, {
            method: "PUT",
            headers: JSON_HEADERS,
            body: JSON.stringify({
                giftId,
                name,
                description,
                link,
                price,
                createdBy,
                groupId,
            }),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(() => navigate(`/users/${params.userId}`))
            .catch((error) => {
                console.error("Error editing gift:", error);
            });
    };

    const deleteGift = () => {
        fetch(`/api/v1/gifts/${params.giftId}`, {
            method: "DELETE",
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                navigate(`/users/${params.userId}`);
            })
            .catch((error) => {
                console.error("Error deleting gift:", error);
            });
    };

    return (
        <div className="ui one column centered equal width grid">
            <div className="d-flex justify-content-center m-3 centered">
                <div className="m-3 cursor-pointer">
                    <Card>
                        <Image src="/images/gifts.jpg" wrapped ui={false} />
                        <Card.Content>
                            <Card.Header>{name}</Card.Header>
                            <Card.Meta>
                                <span className="date">
                                    Event date is set to {description}
                                </span>
                            </Card.Meta>
                        </Card.Content>
                        <Form
                            className="m-3 p-3"
                            onSubmit={(e) => {
                                e.preventDefault();
                                editGift();
                            }}
                        >
                            <Form.Field className="m-3">
                                <label>Gift Name</label>
                                <input
                                    placeholder="Gift Name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                />
                            </Form.Field>
                            <Form.Field>
                                <label>Description</label>
                                <input
                                    placeholder="Description"
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                />
                            </Form.Field>
                            <Form.Field>
                                <label>Link</label>
                                <input
                                    placeholder="Link"
                                    value={link}
                                    onChange={(e) => setLink(e.target.value)}
                                />
                            </Form.Field>
                            <Form.Field>
                                <label>Price</label>
                                <input
                                    placeholder="Price"
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)}
                                />
                            </Form.Field>

                            <Button icon
                                labelPosition="left"
                                onClick={() => navigate(`/users/${params.userId}`)}
                            > <Icon name="arrow left" />
                                Back
                            </Button>
                            <Button type="submit">Update</Button>
                            <Button type="button" onClick={deleteGift}>Delete</Button>
                        </Form>
                    </Card>
                </div>
            </div>
        </div>
    );
}

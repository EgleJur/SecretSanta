import React, { useState, useEffect } from "react";
import { Form, Button, Card, Icon } from "semantic-ui-react";
import { Link, useNavigate, useParams} from "react-router-dom";

const JSON_HEADERS = {
  "Content-Type": "application/json",
  Authorization: "Bearer " + localStorage.getItem("token"),
};

export function CreateGift() {
  const params = useParams();
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [link, setLink] = useState("");
  const [price, setPrice] = useState("");
  const [createdBy, setCreatedBy] = useState(parseInt(params.id));
  const [groupId, setGroupId] = useState("");
  const [giftId, setGiftId] = useState("");
  const [groupList, setGroupList] = useState([]);

  const createGift = () => {
    fetch(`/api/v1/gifts`, { 
      method: "POST",
      headers: JSON_HEADERS,
      body: JSON.stringify({
        giftId,
        name,
        description,
        link,
        price,
        groupId,
        createdBy,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then(() => navigate(`/users/${params.id}`)) 
      .catch((error) => {
        console.error("Error creating gift:", error);
      });
  };
  

  const fetchGroups = async () => {
    try {
      const response = await fetch(`/api/v1/groups/user/${parseInt(params.id)}/groups` , {
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
      setGroupList(jsonResponse);
      console.log("Fetched Groups:", jsonResponse);
    } catch (error) {
      console.error("Error fetching groups:", error);
    }
  };

  useEffect(() => {
    fetchGroups();
  }, []);

  return (
    <div class="ui one column centered equal width grid">
      <Card className="m-3 p-3">
        <Form
          className="m-3 p-3"
          onSubmit={(e) => {
            e.preventDefault();
            createGift();
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
          <Form.Select
            label="Group"
            value={groupId} 
            onChange={(e, { value }) => setGroupId(value)} 
            placeholder="Select a Group"
            options={groupList.map((group) => ({
              key: group.groupId,
              text: group.name,
              value: group.groupId,
            }))}
          />


          <Button icon labelPosition="left" className="" 
          as={Link} to={`/users/${params.id}`}>
            <Icon name="arrow left" />
            Back
          </Button>
          <Button type="submit">Submit</Button>
        </Form>
      </Card>
    </div>
  );
}

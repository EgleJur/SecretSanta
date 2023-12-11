import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import AuthContext from "../AuthContext";
import {
  Button,
  Form,
  Grid,
  Segment,
  Image,
  Header,
  Container,
} from "semantic-ui-react";
import "./SecretSanta.css";

export function HomePage() {
  const [imageUrls, setImageUrls] = useState(["/images/secretSantaSanta.jpg"]);

  const { appState, setAppState } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleRegisterClick = () => {
    navigate("/auth/register");
  };

  return (
    <Grid centered columns={2}>
      <Grid.Column>
        <Segment>
          <Container fluid textAlign="center">
            <Header as="h3" style={{ color: "red" }}>
              ARE YOU READY TO BECOME SOMEONES SECRET SANTA???
            </Header>
            <Container>
              <Image
                src={imageUrls}
                centered
                fluid
                style={{ marginTop: "10px", marginBottom: "10px" }}
              />
              <Form>
                <Button
                  className="create mt-2"
                  style={{
                    backgroundColor: "rgb(250, 110, 110)",
                    color: "white",
                  }}
                  fluid
                  onClick={handleRegisterClick}
                >
                  Register
                </Button>
              </Form>
            </Container>
          </Container>
        </Segment>
      </Grid.Column>
    </Grid>
  );
}

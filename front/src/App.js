import './App.css';

import { Button, Form } from 'react-bootstrap';
import React, { Component } from 'react';

import axios from 'axios';

class App extends Component {

  state = {
    input : ''
  }

  render() {
    return (
      <div>
        <h2>Type your code below!</h2>
        <Form onSubmit={ this.sendInput }>
          <Form.Group controlId="exampleForm.ControlTextarea1">
            <Form.Control as="textarea" rows="3" onChange={(ref) => {this.setState({input: ref.target.value} )}}/>
          </Form.Group>
          <Button variant="primary" onClick={ this.sendInput }>
            Submit
          </Button>
        </Form>
      </div>
    );
  }

  sendInput = () => {
    console.log("sending:", this.state.input);
    const instance = axios.create({
      baseURL: 'http://localhost:8080/'
    });
    instance.post('/parse', {
      'input': this.state.input
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }
}

export default App;


import React, { Component } from 'react'
import { Alert } from 'react-bootstrap';

import './messageAlert.css';

class MessageAlert extends Component {

    render() {
        return (
           this.props.type ? <Alert variant="danger" className="alertStyle">{this.props.message}</Alert> : null
        );
    }
}

export default MessageAlert;
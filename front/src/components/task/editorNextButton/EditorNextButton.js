import React, { Component } from 'react';
import {LinkContainer} from 'react-router-bootstrap';

import './editorNextButton.css';

class EditorNextButton extends Component {

    render = () => {
        return (
            <LinkContainer to={this.props.nextUrl}><button className='editorNextButton'>Dalej</button></LinkContainer>
        );
    }
}

export default EditorNextButton;
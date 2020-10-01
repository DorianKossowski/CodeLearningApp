import React, { Component } from 'react';

import './editorVerifyButton.css';

class EditorVerifyButton extends Component {

    render = () => {
        return (
            <button className='editorVerifyButton' onClick={this.props.verify}>Sprawdź</button>
        );
    }
}

export default EditorVerifyButton;
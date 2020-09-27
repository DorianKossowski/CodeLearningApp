import React, { Component } from 'react';

import './editorVerifyButton.css';

class EditorVerifyButton extends Component {

    render = () => {
        return (
            <button className='editorButton' onClick={this.props.verify}>Sprawdź</button>
        );
    }
}

export default EditorVerifyButton;
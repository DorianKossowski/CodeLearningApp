import React, { Component } from 'react';

import './editorLogArea.css';

class EditorLogArea extends Component {

    render = () => {
        return (
            <div className='editorLogArea'>{this.props.logMessage}</div>
        );
    }
}

export default EditorLogArea;
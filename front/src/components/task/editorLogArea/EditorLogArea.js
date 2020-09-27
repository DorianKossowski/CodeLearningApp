import React, { Component } from 'react';

import './editorLogArea.css';

class EditorLogArea extends Component {

    render = () => {
        let style = 'editorLogArea';
        if(this.props.isValid) {
            style += this.props.isValid === true ? ' validEditorLogArea' : ' invalidEditorLogArea';
        }
        return (
            <div className={style}>{this.props.logMessage}</div>
        );
    }
}

export default EditorLogArea;
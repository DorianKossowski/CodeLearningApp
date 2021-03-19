import React, { Component } from 'react';
import Typewriter from 'typewriter-effect';

import './editorConsoleArea.css';

class EditorConsoleArea extends Component {

    render = () => {
        let style = 'editorConsoleArea';
        return (
            <div className={style}>
                <Typewriter
                options={{
                    strings: this.prepareConsoleOutput(this.props.consoleOutput),
                    autoStart: true,
                    delay: 1
                }}
                />
            </div>
        );
    }

    prepareConsoleOutput = output => {
        return output?.replace(/[&<>'"]/g, 
        tag => ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            "'": '&#39;',
            '"': '&quot;'
          }[tag]))
        .split(/\r?\n/).join('<br>');
    }
}

export default EditorConsoleArea;
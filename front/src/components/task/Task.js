import React, { Component } from 'react';

import './task.css';
import EditorLogAreaContainer from './editorLogArea';
import EditorVerifyButtonContainer from './editorVerifyButton';
import EditorNextButtonContainer from './editorNextButton';
import TaskEditorContainer from './taskEditor';


class Task extends Component {

    render = () => {
        return (
            <div className='task'>
                <div className='editorWrapper'>
                    <TaskEditorContainer />
                    <div className='editorBottomArea'>
                        <EditorLogAreaContainer />
                        <EditorVerifyButtonContainer />
                    </div>
                    <div className='editorBottomAreaBlock'>
                        <EditorNextButtonContainer />
                    </div>
                </div>
                <div className='description'>
                    {this.prepareText(this.props.description)}
                    <p className='instructionTitle'>ZADANIE:</p>
                    {this.prepareText(this.props.instruction)}
                </div>
            </div>
        );
    }

    prepareText = (text) => {
        return text.split('\n').map((item, i) => {
            return <p key={i}>{item}</p>;
        });
    }
}

export default Task;
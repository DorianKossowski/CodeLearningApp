import React, { Component } from 'react';

import './task.css';
import EditorLogAreaContainer from './editorLogArea';
import EditorVerifyButtonContainer from './editorVerifyButton';
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
                </div>
                <div className='description'>
                    {this.prepareDescription()}
                    <p className='instructionTitle'>ZADANIE:</p>
                    {this.props.instruction}
                </div>
            </div>
        );
    }

    prepareDescription = () => {
        return this.props.description.split('\n').map((item, i) => {
            return <p key={i}>{item}</p>;
        });
    }
}

export default Task;
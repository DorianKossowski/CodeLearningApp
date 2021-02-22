import React, { Component } from 'react';

import './task.css';
import EditorConsoleAreaContainer from './editorConsoleArea';
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
                        <div className='editorBottomAreaContent'>
                            <EditorConsoleAreaContainer />
                            <EditorLogAreaContainer />
                        </div>
                        <div className='editorBottomAreaButtons'>
                            <EditorVerifyButtonContainer />
                            <EditorNextButtonContainer />
                        </div>
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
        var preparedText = '';
        const textLines = text.replace('\t', '    ').split('\n');
        for(var currentLine = { i : 0 }; currentLine.i < textLines.length; ++currentLine.i) {
            if(textLines[currentLine.i] === '<java>') {
                preparedText += '<p key=' + currentLine.i + ' class="descriptionCode">' + this.prepareCodeText(textLines, currentLine) + '</p>';
            } else {
                preparedText += '<p key=' + currentLine.i + '>' + textLines[currentLine.i] + '</p>';
            }
        }
        return <div dangerouslySetInnerHTML={{__html: preparedText}}></div>;
    }

    prepareCodeText = (textLines, currentLine) => {
        var codeText = '';
        currentLine.i++;
        while(currentLine.i < textLines.length && textLines[currentLine.i] !== '</java>') {
            codeText += textLines[currentLine.i] + '<br>';
            currentLine.i++;
        }
        return codeText;
    }
}

export default Task;
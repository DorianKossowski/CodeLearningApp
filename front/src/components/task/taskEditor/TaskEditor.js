import React, { Component } from 'react';
import Editor from "react-simple-code-editor";

import { highlight, languages } from "prismjs/components/prism-core";
import "prismjs/components/prism-clike";
import "prismjs/components/prism-java";
import "prismjs/themes/prism.css";

import './taskEditor.css';

const hightlightWithLineNumbers = (input, language, errorLine) =>
    highlight(input, language)
        .split("\n")
        .map((line, i) => {
            if(i + 1 === errorLine) {
                return `<span class='editorLineNumber' style='background: #ff4b4b'>${i + 1}</span>${line}`;
            }
            return `<span class='editorLineNumber'>${i + 1}</span>${line}`;
        })
        .join("\n");

class TaskEditor extends Component {
    
    render = () => {
        return (
            <Editor
            value={this.props.input}
            onValueChange={this.props.onValueChange}
            highlight={code => hightlightWithLineNumbers(code, languages.java, 0)}
            padding={10}
            textareaId="codeArea"
            className="editor"
            />
        );
    }
}

export default TaskEditor;
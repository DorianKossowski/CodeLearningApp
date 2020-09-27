import React, { Component } from 'react';
import Editor from "react-simple-code-editor";

import { highlight, languages } from "prismjs/components/prism-core";
import "prismjs/components/prism-clike";
import "prismjs/components/prism-java";
import "prismjs/themes/prism.css";

import './taskEditor.css';

class TaskEditor extends Component {

    hightlightWithLineNumbers = (input, language, errorLine) =>
        highlight(input, language)
            .split("\n")
            .map((line, i) => {
                let style = 'editorLineNumber';
                style += (i + 1 === errorLine) ? ' errorLineNumber' : '';
                return `<span class='${style}'>${i + 1}</span>${line}`;
            })
            .join("\n");
    
    render = () => {
        return (
            <Editor
            value={this.props.input}
            onValueChange={this.props.onValueChange}
            highlight={code => this.hightlightWithLineNumbers(code, languages.java, this.props.errorLineNumber)}
            padding={10}
            textareaId="codeArea"
            className="editor"
            />
        );
    }
}

export default TaskEditor;
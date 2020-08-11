import React, { Component } from 'react';

import './components.css';
import Editor from "react-simple-code-editor";

import { highlight, languages } from "prismjs/components/prism-core";
import "prismjs/components/prism-clike";
import "prismjs/components/prism-java";
import "prismjs/themes/prism.css";

const hightlightWithLineNumbers = (input, language) =>
    highlight(input, language)
        .split("\n")
        .map((line, i) => `<span class='editorLineNumber'>${i + 1}</span>${line}`)
        .join("\n");

class Hello extends Component {

    state = { code: 
`public class Hello {
    public static void main(String[] args){
        System.out.print("Hello World");
    }
}`
   }

    onValueChange = code => {
      this.setState({ code })
    }

    render = () => {
        return (
            <Editor
            value={this.state.code}
            onValueChange={this.onValueChange}
            highlight={code => hightlightWithLineNumbers(code, languages.java)}
            padding={10}
            textareaId="codeArea"
            className="editor"
            />
        );
    }
}

export default Hello;
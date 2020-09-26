import React, { Component } from 'react';

import './components.css';
import api from '../helpers/Api.js';
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
}`,
        description:
`Przyjęło się, że pierwszy program jaki tworzy się w dowolnym języku to zawsze "Hello World", czyli "Witaj Świecie". Jest to prosty program który wyświetla na ekranie taki tekst.
By tego dokonać należy najpierw dodać tak zwaną metodę main którą można uruchomić, czyli coś w rodzaju punktu startowego naszego programu.
Teraz możemy skorzystać z funkcji println, która to wyświetla dowolny ciąg znaków na tak zwanym standardowym wyjściu.`,
        task:
`-> method
    with modifiers: { "public", "static" }
    with result: "void"
    with name: "main"
    with args: { "String[]", - }
-> statement
    in method: "main"
    with text: "System.out.print(\\"Hello World\\");"
    error message: "Wywołanie metody z literału"`        
    }

    onValueChange = code => {
      this.setState({ code })
    }

    verify = () => {
        api({
            method: 'POST',
            url: 'parse',
            data: { task: this.state.task, input: this.state.code }
        })
    }

    render = () => {
        const newText = this.state.description.split('\n').map((item, i) => {
            return <p key={i}>{item}</p>;
        });

        return (
            <div className='exercise'>
                <div className='editorWrapper'>
                    <Editor
                    value={this.state.code}
                    onValueChange={this.onValueChange}
                    highlight={code => hightlightWithLineNumbers(code, languages.java)}
                    padding={10}
                    textareaId="codeArea"
                    className="editor"
                    />
                    <div className='editorBottomArea'>
                        <div className='editorLogArea'>LOG</div>
                        <button className='editorButton' onClick={this.verify}>Sprawdź</button>
                    </div>
                </div>
                <div className='description'>{ newText }</div>
            </div>
        );
    }
}

export default Hello;
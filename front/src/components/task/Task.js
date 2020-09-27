import React, { Component } from 'react';
import { connect } from "react-redux";

import './task.css';
import {taskActions} from './duck';
import EditorLogAreaContainer from './editorLogArea';
import EditorVerifyButtonContainer from './editorVerifyButton';
import TaskEditorContainer from './taskEditor';


class Task extends Component {

    componentDidMount = () => {
        this.props.setTask(`-> method
        with modifiers: { "public", "static" }
        with result: "void"
        with name: "main"
        with args: { "String[]", - }
    -> statement
        in method: "main"
        with text: "System.out.print(\\"Hello World\\");"
        error message: "Wywołanie metody z literału"`);

        this.props.setInput(`public class Hello {
            public static void main(String[] args){
                System.out.print("Hello World");
            }
        }`);
    }

    state = { 
        description:
`Przyjęło się, że pierwszy program jaki tworzy się w dowolnym języku to zawsze "Hello World", czyli "Witaj Świecie". Jest to prosty program który wyświetla na ekranie taki tekst.
By tego dokonać należy najpierw dodać tak zwaną metodę main którą można uruchomić, czyli coś w rodzaju punktu startowego naszego programu.
Teraz możemy skorzystać z funkcji println, która to wyświetla dowolny ciąg znaków na tak zwanym standardowym wyjściu.`
    }

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
                <div className='description'>{this.prepareDescription()}</div>
            </div>
        );
    }

    prepareDescription = () => {
        return this.state.description.split('\n').map((item, i) => {
            return <p key={i}>{item}</p>;
        });
    }
}

const mapDispatchToProps = dispatch => ({
    setTask: (value) => dispatch(taskActions.setTask(value)),
    setInput: (value) => dispatch(taskActions.setInput(value))
});

export default connect(null, mapDispatchToProps)(Task);
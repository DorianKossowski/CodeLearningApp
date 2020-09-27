import React, { Component } from 'react';

import './task.css';
import EditorLogAreaContainer from './editorLogArea';
import EditorVerifyButtonContainer from './editorVerifyButton';
import TaskEditorContainer from './taskEditor';


class Task extends Component {

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

export default Task;
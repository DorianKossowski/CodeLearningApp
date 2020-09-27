import React, { Component } from 'react';
import { connect } from 'react-redux';

import {taskActions} from '../duck';
import TaskEditor from './TaskEditor';

class TaskEditorContainer extends Component {

    onValueChange = code => {
        this.props.setInput(code);
    }

    render = () => {
        return (
            <TaskEditor input={this.props.input} onValueChange={this.onValueChange}/>
        );
    }
}

const mapSateToProps = state => ({
    input: state.task.input
});

const mapDispatchToProps = dispatch => ({
    setInput: (value) => dispatch(taskActions.setInput(value))
});


export default connect(mapSateToProps, mapDispatchToProps)(TaskEditorContainer);
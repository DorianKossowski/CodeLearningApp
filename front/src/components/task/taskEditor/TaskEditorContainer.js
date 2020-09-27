import React, { Component } from 'react';
import { connect } from 'react-redux';

import {taskActions} from '../duck';
import TaskEditor from './TaskEditor';

class TaskEditorContainer extends Component {

    onValueChange = code => {
        this.props.setInput(code);
        this.props.resetValidation();
    }

    render = () => {
        return (
            <TaskEditor input={this.props.input} onValueChange={this.onValueChange} errorLineNumber={this.props.errorLineNumber} />
        );
    }
}

const mapSateToProps = state => ({
    input: state.task.input,
    errorLineNumber: state.task.errorLineNumber
});

const mapDispatchToProps = dispatch => ({
    setInput: (value) => dispatch(taskActions.setInput(value)),
    resetValidation: () => dispatch(taskActions.resetValidation())
});


export default connect(mapSateToProps, mapDispatchToProps)(TaskEditorContainer);
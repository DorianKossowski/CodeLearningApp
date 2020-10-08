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
            <TaskEditor onValueChange={this.onValueChange} {...this.props} />
        );
    }
}

const mapStateToProps = state => ({
    input: state.task.input,
    errorLineNumber: state.task.errorLineNumber
});

const mapDispatchToProps = dispatch => ({
    setInput: (value) => dispatch(taskActions.setInput(value)),
    resetValidation: () => dispatch(taskActions.resetValidation())
});


export default connect(mapStateToProps, mapDispatchToProps)(TaskEditorContainer);
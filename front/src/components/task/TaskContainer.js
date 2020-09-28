import React, { Component } from "react";
import { connect } from "react-redux";

import {taskActions} from './duck';
import Task from './Task';


class TaskContainer extends Component {

    componentDidMount = () => {
        const taskData = require('../../resources/' + this.props.match.params.category + '/' + this.props.match.params.id);
        this.props.setTask(taskData.task);
        this.props.setInput(taskData.input);
        this.props.setDescription(taskData.description);
        this.props.setInstruction(taskData.instruction);
    }

    render = () => {
        return (
            <Task description={this.props.description} instruction={this.props.instruction} />
        );
    }

}

const mapStateToProps = state => ({
    description: state.task.description,
    instruction: state.task.instruction
});

const mapDispatchToProps = dispatch => ({
    setTask: (value) => dispatch(taskActions.setTask(value)),
    setInput: (value) => dispatch(taskActions.setInput(value)),
    setDescription: (value) => dispatch(taskActions.setDescription(value)),
    setInstruction: (value) => dispatch(taskActions.setInstruction(value))
});

export default connect(mapStateToProps, mapDispatchToProps)(TaskContainer);
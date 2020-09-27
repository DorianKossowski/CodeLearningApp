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
    }

    render = () => {
        return (
            <Task description={this.props.description} />
        );
    }

}

const mapStateToProps = state => ({
    description: state.task.description
});

const mapDispatchToProps = dispatch => ({
    setTask: (value) => dispatch(taskActions.setTask(value)),
    setInput: (value) => dispatch(taskActions.setInput(value)),
    setDescription: (value) => dispatch(taskActions.setDescription(value))
});

export default connect(mapStateToProps, mapDispatchToProps)(TaskContainer);
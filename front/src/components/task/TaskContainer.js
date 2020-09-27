import React, { Component } from "react";
import { connect } from "react-redux";

import {taskActions} from './duck';
import Task from './Task';


class TaskContainer extends Component {

    componentDidMount = () => {
        const taskData = require('../../resources/' + this.props.match.params.category + '/' + this.props.match.params.id);
        this.props.setTask(taskData.task);
        this.props.setInput(taskData.input);
    }

    render = () => {
        return (
            <Task />
        );
    }

}

const mapDispatchToProps = dispatch => ({
    setTask: (value) => dispatch(taskActions.setTask(value)),
    setInput: (value) => dispatch(taskActions.setInput(value))
});

export default connect(null, mapDispatchToProps)(TaskContainer);
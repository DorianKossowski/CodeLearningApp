import React, { Component } from "react";
import { connect } from "react-redux";

import {taskActions} from './duck';
import Task from './Task';


class TaskContainer extends Component {

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
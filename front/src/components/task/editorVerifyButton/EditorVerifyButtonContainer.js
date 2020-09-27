import React, { Component } from "react";
import { connect } from "react-redux";

import EditorVerifyButton from "./EditorVerifyButton";
import { taskActions } from '../duck';
import api from '../../../helpers/Api';

const VALID_MESSAGE = 'Poprawnie wykonano zadanie. Przejdź do następnego!';

class EditorVerifyButtonContainer extends Component {

    verify = () => {
        api({
            method: 'POST',
            url: 'parse',
            data: { task: this.props.task, input: this.props.input }
        })
        .then(data => {
            if(!data.errorMessage) {
                this.props.valid(VALID_MESSAGE);
            } else {
                this.props.invalid(data.errorMessage, data.lineNumber);
            }
        })
    }

    render = () => {
        return (
            <EditorVerifyButton verify={this.verify} />
        );
    }
}

const mapSateToProps = state => ({
    task: state.task.task,
    input: state.task.input
});

const mapDispatchToProps = dispatch => ({
    valid: (message) => dispatch(taskActions.valid(message)),
    invalid: (message, lineNumber=null) => dispatch(taskActions.invalid(message, lineNumber))
});

export default connect(mapSateToProps, mapDispatchToProps)(EditorVerifyButtonContainer);
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
            }
        })
    }

    render = () => {
        return (
            <EditorVerifyButton verify={this.verify} />
        );
    }
}

const mapDispatchToProps = dispatch => ({
    valid: (message) => dispatch(taskActions.valid(message))
});

export default connect(null, mapDispatchToProps)(EditorVerifyButtonContainer);
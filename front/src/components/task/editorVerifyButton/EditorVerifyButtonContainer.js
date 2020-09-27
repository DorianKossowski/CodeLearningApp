import React, { Component } from "react";
import { connect } from "react-redux";

import EditorVerifyButton from "./EditorVerifyButton";
import { taskActions } from '../duck';
import api from '../../../helpers/Api';

class EditorVerifyButtonContainer extends Component {
    verify = () => {
        api({
            method: 'POST',
            url: 'parse',
            data: { task: this.props.task, input: this.props.input }
        })
        .then(data => {
            if(!data.errorMessage) {
                this.props.valid();
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
    valid: () => dispatch(taskActions.valid())
});

export default connect(null, mapDispatchToProps)(EditorVerifyButtonContainer);
import React, { Component } from "react";
import { connect } from "react-redux";

import EditorNextButton from "./EditorNextButton";

class EditorNextButtonContainer extends Component {

    render = () => {
        return (
            this.props.isValid ? <EditorNextButton nextUrl={this.getNextUrl()} /> : null
        );
    }

    getNextUrl = () => {
        if (this.props.hasNext) {
            const currentUrl = window.location.pathname;
            const lastSegment = currentUrl.lastIndexOf('/');
            const id = currentUrl.substring(lastSegment + 1);
            return currentUrl.substring(0, lastSegment + 1) + (parseInt(id) + 1);
        }
        return '/';
    }
}

const mapStateToProps = state => ({
    isValid: state.task.isValid, // TODO use state from DB?
    hasNext: state.task.hasNext
});

export default connect(mapStateToProps)(EditorNextButtonContainer);
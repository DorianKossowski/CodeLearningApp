import { connect } from 'react-redux';
import EditorLogArea from './EditorLogArea';

const mapStateToProps = state => ({
    logMessage: state.task.logMessage,
    isValid: state.task.isValid
});

export const EditorLogAreaContainer = connect(mapStateToProps)(EditorLogArea);
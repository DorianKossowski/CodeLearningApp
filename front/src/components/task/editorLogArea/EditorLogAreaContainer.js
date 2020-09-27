import { connect } from 'react-redux';
import EditorLogArea from './EditorLogArea';

const mapSateToProps = state => ({
    logMessage: state.task.logMessage
});

export const EditorLogAreaContainer = connect(mapSateToProps)(EditorLogArea);
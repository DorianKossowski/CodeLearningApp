import { connect } from 'react-redux';
import EditorConsoleArea from './EditorConsoleArea';

const mapStateToProps = state => ({
    consoleOutput: state.task.consoleOutput
});

export const EditorConsoleAreaContainer = connect(mapStateToProps)(EditorConsoleArea);
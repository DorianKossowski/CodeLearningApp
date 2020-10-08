import { connect } from 'react-redux';
import MessageAlert from './MessageAlert';

const mapStateToProps = state => ({
    type: state.alert.type,
    message: state.alert.message
});

export const MessageAlertContainer = connect(mapStateToProps)(MessageAlert);
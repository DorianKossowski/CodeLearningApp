import store from "../store";
import { alertActions } from "../components/alert/duck";

const handleError = (error, msg) => {
    store.dispatch(alertActions.setError(getErrorMessage(error, msg)));
}

const getErrorMessage = (error, msg) => {
    if (error.data) {
        if(error.status === 500) {
            return msg + 'Interal error'
        }
        return msg + error.data.message;
    } else {
        return msg + error;
    }
}

export default handleError;
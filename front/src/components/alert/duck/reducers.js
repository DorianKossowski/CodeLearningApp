import types from './types';

const INIT_STATE = {
    type: '',
    message: '',
}

const alertReducer = (state = INIT_STATE, action) => {
    switch (action.type) {
        case types.SET_ERROR:
            return {...state, type: 'error', message: action.value};
        case types.RESET_ALERT:
            return INIT_STATE;
        default:
            return state;
    }
}

export default alertReducer;
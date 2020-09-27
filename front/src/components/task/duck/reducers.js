import types from './types';

const INIT_STATE = {
    logMessage: '',
    isValid: null
}

const taskReducer = (state = INIT_STATE, action) => {
    switch (action.type) {
        case types.VALID_TASK:
            return {...state, logMessage: action.message, isValid: true}
        default:
            return state;
    }
}

export default taskReducer;
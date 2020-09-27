import types from './types';

const INIT_STATE = {
    task: '',
    input: '',
    logMessage: '',
    isValid: null
}

const taskReducer = (state = INIT_STATE, action) => {
    switch (action.type) {
        case types.SET_TASK:
            return {...state, task: action.value};
        case types.SET_INPUT:
            return {...state, input: action.value};
        case types.VALID_TASK:
            return {...state, logMessage: action.message, isValid: true};
        default:
            return state;
    }
}

export default taskReducer;
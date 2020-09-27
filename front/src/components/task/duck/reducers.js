import types from './types';

const INIT_STATE = {
    logMessage : ''
}

const taskReducer = (state = INIT_STATE, action) => {
    switch (action.type) {
        case types.VALID_TASK:
            return {...state, logMessage: 'valid!'}
        default:
            return state;
    }
}

export default taskReducer;
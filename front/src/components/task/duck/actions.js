import types from './types';

const setTask = (task) => ({type: types.SET_TASK, value: task});
const setInput = (input) => ({type: types.SET_INPUT, value: input});
const valid = (validMessage) => ({type: types.VALID_TASK, message: validMessage});

export default {
    setTask,
    setInput,
    valid
};
import types from './types';

const setTask = (task) => ({type: types.SET_TASK, value: task});
const setInput = (input) => ({type: types.SET_INPUT, value: input});
const setDescription = (description) => ({type: types.SET_DESCRIPTION, value: description});
const valid = (validMessage) => ({type: types.VALID_TASK, message: validMessage});
const invalid = (invalidMessage, lineNumber) => ({type: types.INVALID_TASK, message: invalidMessage, number: lineNumber});
const resetValidation = () => ({type: types.RESET_VALIDATION_RESULT});

export default {
    setTask,
    setInput,
    setDescription,
    valid,
    invalid,
    resetValidation
};
import types from './types';

const setTask = (task) => ({type: types.SET_TASK, value: task});
const setInput = (input) => ({type: types.SET_INPUT, value: input});
const setDescription = (description) => ({type: types.SET_DESCRIPTION, value: description});
const setInstruction = (instruction) => ({type: types.SET_INSTRUCTION, value: instruction});
const setHasNext = (hasNext) => ({type: types.SET_HAS_NEXT, value: hasNext});
const valid = (validMessage) => ({type: types.VALID_TASK, message: validMessage});
const invalid = (invalidMessage, lineNumber) => ({type: types.INVALID_TASK, message: invalidMessage, number: lineNumber});
const resetValidation = () => ({type: types.RESET_VALIDATION_RESULT});

export default {
    setTask,
    setInput,
    setDescription,
    setInstruction,
    setHasNext,
    valid,
    invalid,
    resetValidation
};
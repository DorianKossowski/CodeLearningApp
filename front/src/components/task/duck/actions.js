import types from './types';

const valid = (validMessage) => ({type: types.VALID_TASK, message: validMessage})

export default {valid};
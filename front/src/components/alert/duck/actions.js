import types from './types';

const setError = (error) => ({type: types.SET_ERROR, value: error});
const resetAlert = () => ({type: types.RESET_ALERT});

export default {
    setError,
    resetAlert
};
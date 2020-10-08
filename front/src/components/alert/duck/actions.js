import types from './types';

const setError = (error) => ({type: types.SET_ERROR, value: error});

export default {
    setError
};
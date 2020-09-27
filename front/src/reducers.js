import { combineReducers } from 'redux';
import taskReducer  from './components/task/duck';

const rootReducer = combineReducers({
  task: taskReducer
});

export default rootReducer;
import { combineReducers } from 'redux';
import alertReducer from './components/alert/duck';
import taskReducer  from './components/task/duck';

const rootReducer = combineReducers({
  task: taskReducer,
  alert: alertReducer
});

export default rootReducer;
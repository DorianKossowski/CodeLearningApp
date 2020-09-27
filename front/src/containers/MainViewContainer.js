import './containers.css';

import React, { Component } from 'react';
import { Route, Switch } from "react-router-dom";

import TaskContainer from '../components/task';
import Home from '../components/home/Home';

class MainViewContainer extends Component {
    render = () => {
        return (
            <div>
                <Switch>
                    <Route exact path='/task' component={ TaskContainer }/>
                    <Route exact path='/' component={ Home }/>
                </Switch>
            </div>
        );
    }
}

export default MainViewContainer;
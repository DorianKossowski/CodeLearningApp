import './containers.css';

import React, { Component } from 'react';
import { Route, Switch } from "react-router-dom";

import Hello from '../components/task/Task'
import Home from '../components/home/Home';

class MainViewContainer extends Component {
    render = () => {
        return (
            <div>
                <Switch>
                    <Route exact path='/hello' component={ Hello }/>
                    <Route exact path='/' component={ Home }/>
                </Switch>
            </div>
        );
    }
}

export default MainViewContainer;
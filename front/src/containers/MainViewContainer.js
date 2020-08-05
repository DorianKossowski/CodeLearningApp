import './containers.css';

import React, { Component } from 'react';
import { Route, Switch } from "react-router-dom";

import Home from '../components/Home';

class MainViewContainer extends Component {
    render = () => {
        return (
            <div>
                <Switch>
                    <Route exact path='/' component={ Home }/>
                </Switch>
            </div>
        );
    }
}

export default MainViewContainer;
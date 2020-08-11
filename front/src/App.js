import React, { Component } from 'react';
import { Route, BrowserRouter as Router, Switch } from "react-router-dom";

import AuthorizedViewContainer from './containers/AuthorizedViewContainer';

class App extends Component {

  render = () => {
    return (
      <div>
        <Router>
          <Switch>
            <Route path='/' component={ AuthorizedViewContainer }/>
          </Switch>
        </Router>
      </div>
    );
  }

}

export default App;


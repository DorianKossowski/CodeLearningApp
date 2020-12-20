import React, { Component } from 'react';

import { Container } from 'react-bootstrap';
import HomeIntroduction from './HomeIntroduction';
import HomeVarsAndTypes from './HomeVarsAndTypes';
import HomeConditionals from './HomeConditionals';
import HomeLoops from './HomeLoops';

import './home.css';
import HomeCompAndLogic from './HomeCompAndLogic';

class Home extends Component {

    render = () => {
        return (
            <Container className='home'>
                <h2>Kurs programowania JAVA</h2>
                <HomeIntroduction />
                <HomeVarsAndTypes />
                <HomeCompAndLogic />
                <HomeConditionals />
                <HomeLoops />
            </Container>
        );
    }
}

export default Home;
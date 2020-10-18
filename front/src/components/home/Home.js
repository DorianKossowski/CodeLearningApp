import React, { Component } from 'react';

import { Container } from 'react-bootstrap';
import HomeIntroduction from './HomeIntroduction';
import HomeVarsAndTypes from './HomeVarsAndTypes';

import './home.css';

class Home extends Component {

    render = () => {
        return (
            <Container className='home'>
                <h2>Kurs programowania JAVA</h2>
                <HomeIntroduction />
                <HomeVarsAndTypes />
            </Container>
        );
    }
}

export default Home;
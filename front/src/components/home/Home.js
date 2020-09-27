import './home.css';

import React, { Component } from 'react';

import { Container } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';

class Home extends Component {

    render = () => {
        return (
            <Container className='home'>
                <h2>Kurs programowania JAVA</h2>
                <div className='category'>
                    <p className='categoryTitle'>Wprowadzenie</p><div className='categoryTitleUnderline'/>
                    <div className='categoryElement'><LinkContainer to={'task'}><a className='categoryElementText'>1. Pierwszy program - Hello World</a></LinkContainer></div><div className='categoryElementUnderline'/>
                </div>
            </Container>
        );
    }
}

export default Home;
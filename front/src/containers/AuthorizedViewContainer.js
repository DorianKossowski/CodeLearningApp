import {Container, Nav, NavDropdown, Navbar} from 'react-bootstrap/';
import React, { Component } from 'react';
import { LinkContainer } from "react-router-bootstrap";

import MainViewContainer from './MainViewContainer';

class AuthorizedViewContainer extends Component {
    render = () => {
        return (
            <>
            <Navbar variant='dark' bg='dark' expand='lg'>
                <Container>
                <Navbar.Brand> 
                <Navbar><LinkContainer to='/'><b className='logo'>KursProgramowania</b></LinkContainer></Navbar>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls='basic-navbar-nav' />
                <Navbar.Collapse id='basic-navbar-nav' className='justify-content-end'>
                <Nav>
                    <NavDropdown title={'Hello USER'}  id='basic-nav-dropdown'>
                    <NavDropdown.Item>Log out</NavDropdown.Item>
                    </NavDropdown>
                </Nav>
                </Navbar.Collapse>
                </Container>
            </Navbar>
            <MainViewContainer/>
            </>
        );
    }
}

export default AuthorizedViewContainer;
import {Container, Nav, NavDropdown, Navbar} from 'react-bootstrap/';
import React, { Component } from 'react';
import { LinkContainer } from "react-router-bootstrap";
import { connect } from 'react-redux';

import MainViewContainer from './MainViewContainer';
import MessageAlertContainer from '../components/alert';
import { alertActions } from '../components/alert/duck';

class AuthorizedViewContainer extends Component {
    componentDidUpdate = () => {
        this.props.resetAlert();
    }

    render = () => {
        return (
            <>
            <Navbar variant='dark' bg='dark' expand='lg'>
                <Container>
                <Navbar.Brand> 
                <Navbar><LinkContainer to='/'><b className='logo'>KursProgramowania</b></LinkContainer></Navbar>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls='basic-navbar-nav' />
                </Container>
            </Navbar>
            <MessageAlertContainer/>
            <MainViewContainer/>
            </>
        );
    }
}

const mapDispatchToProps = dispatch => ({
    resetAlert: () => dispatch(alertActions.resetAlert())
});

export default connect(null, mapDispatchToProps)(AuthorizedViewContainer);
import React, {useState} from 'react';
import {Collapse} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {FaCaretDown, FaCaretUp} from 'react-icons/fa';

const HomeCompAndLogic = (props) => {
    const [open, setOpen] = useState(false);

    const DynamiCaretTag = open ? FaCaretUp : FaCaretDown;
    return (
    <div className='category'>
        <div className='categoryTitleArea'>
            <p className='categoryTitle'>Porównania i operatory logiczne</p>
            <DynamiCaretTag className='titleCaret' onClick={() => setOpen(!open)} aria-controls='elements' aria-expanded={open} />
        </div>
        <div className='categoryTitleUnderline'/>
        <Collapse in={open}>
            <div id='elements'>
                <div className='categoryElement' >
                    <LinkContainer to={'task/compAndLogic/1'}><a className='categoryElementText'>1. Porównania i operatory logiczne - wprowadzenie</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/compAndLogic/2'}><a className='categoryElementText'>2. Porównywanie liczb</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/compAndLogic/3'}><a className='categoryElementText'>3. Porównywanie typów złożonych</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/compAndLogic/4'}><a className='categoryElementText'>4. Podstawowe operacje logiczne</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
            </div>
        </Collapse>
    </div>
    );
}

export default HomeCompAndLogic;
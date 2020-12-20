import React, {useState} from 'react';
import {Collapse} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {FaCaretDown, FaCaretUp} from 'react-icons/fa';

const HomeIntroduction = (props) => {
    const [open, setOpen] = useState(false);

    const DynamiCaretTag = open ? FaCaretUp : FaCaretDown;
    return (
    <div className='category'>
        <div className='categoryTitleArea'>
            <p className='categoryTitle' onClick={() => setOpen(!open)}>Wprowadzenie</p>
            <DynamiCaretTag className='titleCaret' onClick={() => setOpen(!open)} aria-controls='elements' aria-expanded={open} />
        </div>
        <div className='categoryTitleUnderline'/>
        <Collapse in={open}>
            <div id='elements'>
                <div className='categoryElement' >
                    <LinkContainer to={'task/introduction/1'}><a className='categoryElementText'>1. Pierwszy program - Hello World</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/introduction/2'}><a className='categoryElementText'>2. Pierwszy program - My Hello World</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/introduction/3'}><a className='categoryElementText'>3. Pierwszy program - Hello World z użyciem zmiennej</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
            </div>
        </Collapse>
    </div>
    );
}

export default HomeIntroduction;
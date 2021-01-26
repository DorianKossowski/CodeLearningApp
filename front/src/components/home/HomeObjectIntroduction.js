import React, {useState} from 'react';
import {Collapse} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {FaCaretDown, FaCaretUp} from 'react-icons/fa';

const HomeObjectIntroduction = (props) => {
    const [open, setOpen] = useState(false);

    const DynamiCaretTag = open ? FaCaretUp : FaCaretDown;
    return (
    <div className='category'>
        <div className='categoryTitleArea'>
            <p className='categoryTitle' onClick={() => setOpen(!open)}>Wstęp do programowania obiektowego</p>
            <DynamiCaretTag className='titleCaret' onClick={() => setOpen(!open)} aria-controls='elements' aria-expanded={open} />
        </div>
        <div className='categoryTitleUnderline'/>
        <Collapse in={open}>
            <div id='elements'>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectIntroduction/1'}><a className='categoryElementText'>1. Wstęp do programowania obiektowego - wprowadzenie</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectIntroduction/2'}><a className='categoryElementText'>2. Definicja klasy</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectIntroduction/3'}><a className='categoryElementText'>3. Definicja konstruktorów</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
            </div>
        </Collapse>
    </div>
    );
}

export default HomeObjectIntroduction;
import React, {useState} from 'react';
import {Collapse} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {FaCaretDown, FaCaretUp} from 'react-icons/fa';

const HomeFunctions = (props) => {
    const [open, setOpen] = useState(false);

    const DynamiCaretTag = open ? FaCaretUp : FaCaretDown;
    return (
    <div className='category'>
        <div className='categoryTitleArea'>
            <p className='categoryTitle' onClick={() => setOpen(!open)}>Programowanie obiektowe</p>
            <DynamiCaretTag className='titleCaret' onClick={() => setOpen(!open)} aria-controls='elements' aria-expanded={open} />
        </div>
        <div className='categoryTitleUnderline'/>
        <Collapse in={open}>
            <div id='elements'>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectExpansion/1'}><a className='categoryElementText'>1. Programowanie obiektowe - wprowadzenie</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectExpansion/2'}><a className='categoryElementText'>2. Tworzenie instancji przy użyciu konstruktorów</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectExpansion/3'}><a className='categoryElementText'>3. Wołanie metod na obiektach</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectExpansion/4'}><a className='categoryElementText'>4. Korzystanie z pól instancji</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectExpansion/5'}><a className='categoryElementText'>5. Słowo kluczowe this</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/objectExpansion/6'}><a className='categoryElementText'>6. Statyczne składowe w obiektowości</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
            </div>
        </Collapse>
    </div>
    );
}

export default HomeFunctions;
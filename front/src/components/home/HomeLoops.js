import React, {useState} from 'react';
import {Collapse} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap';
import {FaCaretDown, FaCaretUp} from 'react-icons/fa';

const HomeConditionals = (props) => {
    const [open, setOpen] = useState(false);

    const DynamiCaretTag = open ? FaCaretUp : FaCaretDown;
    return (
    <div className='category'>
        <div className='categoryTitleArea'>
            <p className='categoryTitle'>Pętle</p>
            <DynamiCaretTag className='titleCaret' onClick={() => setOpen(!open)} aria-controls='elements' aria-expanded={open} />
        </div>
        <div className='categoryTitleUnderline'/>
        <Collapse in={open}>
            <div id='elements'>
                <div className='categoryElement' >
                    <LinkContainer to={'task/loop/1'}><a className='categoryElementText'>1. Pętle - wprowadzenie</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/loop/2'}><a className='categoryElementText'>2. Pętla for</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/loop/3'}><a className='categoryElementText'>3. Pętla while</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
                <div className='categoryElement' >
                    <LinkContainer to={'task/loop/4'}><a className='categoryElementText'>4. Pętla do while</a></LinkContainer>
                    <div className='categoryElementUnderline'/>
                </div>
            </div>
        </Collapse>
    </div>
    );
}

export default HomeConditionals;
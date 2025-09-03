import { faPhone, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import React from 'react'
import { Button, Image } from 'react-bootstrap'

export default function ChatRoomHeader({ user, navigate}) {

    return (
        <div className='chat-header-container'>
            <Button className='chat-back-btn' onClick={() => navigate(-1)}><FontAwesomeIcon icon={faRightToBracket} /></Button>
            <Image className='chat-header-avatar' 
                src={user?.avatar || 'https://res.cloudinary.com/dnqt29l2e/image/upload/v1747068051/c47vxjryuhnfz2ljk3dn.jpg'}
                alt='avatar'
                width={30}
            />
            <div className='chat-header-username'>{`${user?.lastname} ${user?.firstname}`}</div>
            <Button className='chat-phone-btn'><FontAwesomeIcon icon={faPhone} /></Button>
        </div>
    )
}

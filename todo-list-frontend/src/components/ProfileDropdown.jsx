import React, {useState, useRef} from 'react'

function ProfileDropdown() {


    const dropdownContentRef = useRef(null);
    const [showDropdownState, setShowDropdownState] = useState(false);

    function toggleDropdown() {
        setShowDropdownState(!showDropdownState);
    }

    function checkShowDropdown(){
        if(showDropdownState){
            return 'show '
        }
        return '';
    }
    return (

        <div className="dropdown">
            <button onClick={toggleDropdown} className='dropbtn'>Dropdown</button>
            <div id="myDropdown" ref={dropdownContentRef} className={`dropdown-content ${checkShowDropdown()}`}>
                <a href="#" className=''>Link 1</a>
                <a href="#" className=''>Link 2</a>
                <a href="#" className=''>Link 3</a>
            </div>
        </div>
    )
}

export default ProfileDropdown
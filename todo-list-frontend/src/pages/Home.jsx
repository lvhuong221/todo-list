import React, { useState, useEffect, useRef, Children, useContext } from 'react'
import { Link } from 'react-router-dom';
import SecuredMessages from '../components/SecuredMessages';
import 'bootstrap/dist/css/bootstrap.min.css';
import classNames from 'classnames';
import authContext from '../Context/AuthContext';
import '../components/dropdown.css'

function HomePage() {

    const [state, setState] = useState("welcomeGuest");
    const {isAuthenticated} = useContext(authContext);

    useEffect(() => {
        if (isAuthenticated === true) {
            setState("loggedIn")
        } else {
            setState("welcomeGuest")
        }
        console.log(state);
    }, [isAuthenticated])

    return (
        <>
            <h1>Welcome to todo list app</h1>
            <SecuredMessages></SecuredMessages>
            {state === "welcomeGuest" &&
                <button type="button">
                    <Link className="login-button" to="/login">Login</Link>
                </button>
            }
        </>
    )


}

export default HomePage

function DropdownItem(props) {
    return (
        <li className='dropdown-item'>
            <img src={props.img}></img>
            <a >{props.text}</a>
        </li>
    )
}
import React, { useState, useEffect, useRef, Children, useContext } from 'react'
import { Link } from 'react-router-dom';
import SecuredMessages from '../components/SecuredMessages';
import 'bootstrap/dist/css/bootstrap.min.css';
import classNames from 'classnames';
import authContext from '../Context/AuthContext';
import '../components/dropdown.css'
import LogoutButton from '../components/LogoutButton';

function HomePage() {

    const [state, setState] = useState("welcomeGuest");
    const {authenticated, setAuthenticated} = useContext(authContext);

    useEffect(() => {
        if (authenticated === true) {
            setState("loggedIn")
        } else {
            setState("welcomeGuest")
        }
        console.log(state);
    }, [authenticated])

    return (
        <>
            <h1>Welcome to todo list app</h1>
            <SecuredMessages></SecuredMessages>
            {state === "welcomeGuest" &&
                <button type="button">
                    <Link className="login-button" to="/login">Login</Link>
                </button>
            }
            <LogoutButton></LogoutButton>
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
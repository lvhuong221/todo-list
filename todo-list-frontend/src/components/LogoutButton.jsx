import React, { useState, useContext } from 'react'
import authContext from '../Context/AuthContext';
import { Navigate } from 'react-router-dom';
import { setAuthHeader } from '../helpers/auth';

function LogoutButton() {
    const { authenticated, setAuthenticated } = useContext(authContext);

    function handleLogout(){
        setAuthHeader(null);
        setAuthenticated(false);
        Navigate('/login');
    }
    if (!authenticated) {
        return (<></>)
    }

    return (
        <button onClick={() => handleLogout()}>Logout</button>
    );
}

export default LogoutButton
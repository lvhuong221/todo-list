import React, { useState, useEffect, useContext } from 'react'
import { request, setAuthToken } from '../helpers/axios_helper';
import classNames from 'classnames';
import authContext from '../Context/AuthContext';

function securedMessages(props) {

    const [listMessages, setListMessages] = useState([]);
    const listMessagesElements = [];
    const {authenticated, setAuthenticated} = useContext(authContext);

    useEffect(() => {request(
        "GET",
        "/messages",
        {}).then(
            (response) => {
                setListMessages(response.data);
                setAuthenticated(true);
            }).catch(
                (error) => {
                    console.log(error);
                    if (error.response.status == 401) {
                        setAuthToken(null);
                        setListMessages([]);
                    }
                    setAuthenticated(false);
                }
            );}, [])

    if (authenticated === true) {
        return (<div>
            <ul>{listMessages.map(item =>
                <li key={item}>
                    <b>{item}</b>
                </li>
            )}</ul>
        </div>)
    } else {
        return (
            <div>
                <p>Login to see secured messages</p>
            </div>
        );
    }

}

export default securedMessages
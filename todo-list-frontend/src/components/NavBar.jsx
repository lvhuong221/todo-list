import * as React from 'react';
import "./NavBar.css";
import { Link, useMatch, useResolvedPath } from 'react-router-dom';
import '/node_modules/bootstrap/dist/css/bootstrap.min.css';

export default function NavBar() {
    return (
        <nav className="nav">
            <Link className="site-title" to="/">Site name</Link>
            <ul>
                <CustomLink to={"/todo-list"}>Todo list</CustomLink>
                <CustomLink to={"/about"}>About</CustomLink>
                
            </ul>
        </nav>
    );
}

function CustomLink({ to, children, ...props }) {
    const resolvedPath = useResolvedPath(to)
    const isActive = useMatch({ path: resolvedPath.pathname, end: true })
    return (
        <li className={isActive ? "active" : ""}>
            <Link to={to} {...props}>{children}</Link>
        </li>
    )
}
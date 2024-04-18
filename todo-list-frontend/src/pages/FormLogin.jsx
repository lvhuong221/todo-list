import React, { useContext, useState } from 'react';
import classNames from 'classnames';
import { getAuthToken, request, setAuthToken } from '../helpers/axios_helper';
import { Navigate } from 'react-router-dom';
import authContext from '../Context/AuthContext';
import { useNavigate } from "react-router-dom";

function FormLogin(props) {

  let navigate = useNavigate();

  const {authenticated, setAuthenticated } = useContext(authContext)
  const [formState, setFormState] = useState("login");
  const [loginDto, setLoginDto] = useState({
    login: "",
    password: "",
  });
  const [signUpDto, setSignUpDto] = useState({
    firstName: "",
    lastName: "",
    login: "",
    password: "",
  })

  function handleLoginInputChange(event) {
    let name = event.target.name;
    let value = event.target.value;
    setLoginDto(l => ({ ...l, [name]: value }));
  }

  function handleSignupInputChange(event) {
    let name = event.target.name;
    let value = event.target.value;
    setSignUpDto(s => ({ ...s, [name]: value }));
  }
  function handleChangeFormState(event) {
    setFormState(event.target.name);
  }

  function onSubmitLogin(e) {
    e.preventDefault();
    request(
      "POST",
      "/login",
      {
        username: loginDto.login,
        password: loginDto.password
      }).then(
        (response) => {
          onLoginSuccessfully(response);
        }).catch(
          (error) => {
            onLoginFailed(error);
          }
        );
  };

  function onLoginSuccessfully(response) {
    setAuthToken(response.data.token);
    setAuthenticated(true);
    console.log(response.data.token);
    navigate('/');
  }

  function onLoginFailed(response) {
    setAuthenticated(false);
    console.log("Error occured!" + response);
  }

  function onSubmitRegister(event) {
    event.preventDefault();
    request(
      "POST",
      "/register",
      {
        firstName: signUpDto.firstName,
        lastName: signUpDto.lastName,
        login: signUpDto.username,
        password: signUpDto.password
      }).then(
        (response) => {
          setAuthToken(response.data.token);
          setFormState("messages");
        }).catch(
          (error) => {
            setAuthToken(null);
            setFormState("welcome")
          }
        );
  };

  // if (getAuthToken() !== null) {
  //   console.log("User already logged in, going to /");
  //   return <Navigate to="/" />
  // }

  if(authenticated){
    return(
      <div>User logged in</div>
    )
  }
  return (
    <div className="row justify-content-center">
      <div className="col-4">
        <ul className="nav nav-pills nav-justified mb-3" id="ex1" role="tablist">
          <li className="nav-item" role="presentation">
            <button name="login" className={classNames("nav-link", formState === "login" ? "active" : "")} id="tab-login"
              onClick={handleChangeFormState}>Login</button>
          </li>
          <li className="nav-item" role="presentation">
            <button name="register" className={classNames("nav-link", formState === "register" ? "active" : "")} id="tab-register"
              onClick={handleChangeFormState}>Register</button>
          </li>
        </ul>

        <div className="tab-content">
          <div className={classNames("tab-pane", "fade", formState === "login" ? "show active" : "")} id="pills-login" >
            <form onSubmit={onSubmitLogin}>

              <div className="form-outline mb-4">
                <label className="form-label" htmlFor="loginName">Username</label>
                <input type="login" id="loginName" name="login" className="form-control" value={loginDto.login}
                  onChange={handleLoginInputChange} placeholder='Username' />
              </div>

              <div className="form-outline mb-4">
                <label className="form-label" htmlFor="loginPassword">Password</label>
                <input type="password" id="loginPassword" name="password" className="form-control" value={loginDto.password}
                  onChange={handleLoginInputChange} placeholder='Password' />
              </div>

              <button type="submit" className="btn btn-primary btn-block mb-4">Sign in</button>

            </form>
          </div>
          <div className={classNames("tab-pane", "fade", formState === "register" ? "show active" : "")} id="pills-register" >
            <form onSubmit={onSubmitRegister}>

              <div className="form-outline mb-4">
                <input type="text" id="firstName" name="firstName" className="form-control" value={signUpDto.firstName} onChange={handleSignupInputChange} />
                <label className="form-label" htmlFor="firstName">First name</label>
              </div>

              <div className="form-outline mb-4">
                <input type="text" id="lastName" name="lastName" className="form-control" value={signUpDto.lastName} onChange={handleSignupInputChange} />
                <label className="form-label" htmlFor="lastName" >Last name</label>
              </div>

              <div className="form-outline mb-4">
                <input type="text" id="username" name="login" className="form-control" value={signUpDto.login} onChange={handleSignupInputChange} />
                <label className="form-label" htmlFor="login">Username</label>
              </div>

              <div className="form-outline mb-4">
                <input type="password" id="registerPassword" name="password" value={signUpDto.password} className="form-control" onChange={handleSignupInputChange} />
                <label className="form-label" htmlFor="registerPassword">Password</label>
              </div>

              <button type="submit" className="btn btn-primary btn-block mb-3">Sign in</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );

}
export default FormLogin
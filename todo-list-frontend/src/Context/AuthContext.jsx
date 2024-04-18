import { createContext } from "react";

const authContext = createContext({
  authenticated: false,
  setAuthenticated: (auth) => {authenticated = auth;
    console.log("Auth value changed: " + auth)}
});

export default authContext;
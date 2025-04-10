import { createContext, useContext, useState } from 'react'
import './App.css'
import Home from './pages/Home'
import TodoList from './pages/TodoList'
import About from './pages/About'
import { Route, Routes, redirect } from 'react-router-dom'
import NavBar from './components/NavBar'
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import PageNotFound from './pages/PageNotFound'
import authContext from './Context/AuthContext';
import FormLogin from './login/views/FormLogin'

export default function App() {

  const [authenticated, setAuthenticated] = useState(false);
  
  return (
    <>
      <authContext.Provider value={{authenticated, setAuthenticated}}>
        <NavBar></NavBar>
        <div className='container'>
          <Routes>
            <Route patch="/*" element={<PageNotFound/>}></Route>
            <Route path="/" element={<Home />}></Route>
            <Route path="/todo-list" element={<TodoList />}></Route>
            <Route path="/about" element={<About />}></Route>
            <Route path="/login" element={<FormLogin />}></Route>
          </Routes>
        </div>
      </authContext.Provider>
    </>
  )
}



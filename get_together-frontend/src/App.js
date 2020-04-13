import React, { useState, useEffect } from 'react';
import './App.css';
import Login from "./login"
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import Scheduler from './Scheduler';

function App(){  
  
  return (
    <Router>
      <Switch>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/dashboard">
            <Scheduler/>
          </Route>
        </Switch>
    </Router>
  ) 

}
export default App;

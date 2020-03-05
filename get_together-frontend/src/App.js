import React, { useState, useEffect } from "react";
import logo from './logo.svg';
import "./App.css";
import "./components/button";
import axios from "axios";

function App() {
  const [data, setData] = useState(null);
  // onCreate
  // useEffect(() => {
  //   axios.get("http://localhost:8080/sprint2").then(data => setData(data.data));
  // }, []);

  useEffect(() => {
      axios.get("http://localhost:8080/users").then(data => console.log(data));
    }, []);

  useEffect(() => {
    axios.post('http://localhost:8080/users/new?name="john"&email="fereshtalaavy@gmail.com"')
    .then(response => { 
      console.log(response)
    })
    .catch(error => {
        console.log(error)
    });
  })

  let index = 0;

  // let content = data ?
  //   data.map(element => {
  //     return <li key={element.correct_answer}>{element.correct_answer}</li>
  //   })
  //   : null

  return (
    <div className="App">
      <div>Hello World!</div>
      <button text="hello world!" />
      <ul>{data ? data : null}</ul>
    </div>
  );
  
}

export default App;

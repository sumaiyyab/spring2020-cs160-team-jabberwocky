import React, { useState, useEffect } from 'react';
import './Scheduler.css';
import Avatar from 'react-avatar';
import PopUp from "./PopUp"
import {withRouter} from "react-router-dom"
import axios from "axios"

import {Inject,ScheduleComponent,Day,Week,Month,Agenda,WorkWeek,EventSettingsModel} from '@syncfusion/ej2-react-schedule'

function Scheduler({location}){  
  const [dataSource, setDataSource] = useState([])

  const [open, setOpen] = useState(false)

  useEffect(() => {}, [dataSource, setDataSource])

  function closePopUp() {
    setOpen(false)
  }
  
  function getData(description, startTime, endTime){
    let data = {
      "hostID":location.state.id,
      "title" :description,
      "startTime": startTime, 
      "endTime": endTime, 
      "duration": 30, 
      "location": "my home", 
       "invited":[]
    }
    let fullStartTime = startTime.getFullYear() + '-'
    + ('0' + (startTime.getMonth()+1)).slice(-2) + '-'
    +  ('0' + startTime.getDate()).slice(-2);
    let fullEndTime = endTime.getFullYear() + '-'
    + ('0' + (endTime.getMonth()+1)).slice(-2) + '-'
    + ('0' + endTime.getDate()).slice(-2);
    
    axios.post('http://localhost:8080/events', data).then(event => {
      axios.get(`http://localhost:8080/events/${event.data.id}/bestTime?startPossible=${fullStartTime}&endPossible=${fullEndTime}`).then(data => {
        for (let time of data.data) {
          let oldDataSource = dataSource
          oldDataSource.push({
          EndTime: time.endTime, 
          StartTime: time.startTime,
          Subject: "available times"
        })
        setDataSource([...dataSource, oldDataSource])
        console.log(dataSource)
        }
      })
    })
  }

  function addevent() {
    setOpen(true)
  }

  return <div className= "scheduler-main">
  <div className= "left-column">

    <Avatar name={location.state.name} size="130" textSizeRatio={1.75} round={true} />
    <div>{location.state.name} profile</div>
    <button className = "btn1">Home</button>{' '}
    <button className = "btn1">Profile</button>{' '}
    <button className = "btn1">Event</button>{' '}
    <button className = "btn1">Members</button>{' '}
    <button className = "btn1">Messages</button>{' '}
    <button className = "btn1">Setting</button>{' '}
    <button className = "btn2">Peeps</button>{' '}
    <div className="txt-event">Create event</div>
    <button className = "btn3" id = "button3"onClick={addevent}>+</button>{' '}
    <PopUp open={open} handleClose={closePopUp} submitData={getData}/>
  
  </div>
  <div className= "scheduler">
  <ScheduleComponent currentView = 'Month' eventSettings={{dataSource}}  >
  <Inject services = {[Day,Week,Month,Agenda,WorkWeek]}/>
 
</ScheduleComponent>
  </div>

  </div>

}

export default withRouter(Scheduler)
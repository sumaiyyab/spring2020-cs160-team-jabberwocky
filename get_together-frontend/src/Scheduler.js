import React, { useState, useEffect } from 'react';
import './Scheduler.css';
import Avatar from 'react-avatar';
import PopUp from "./PopUp"

import {Inject,ScheduleComponent,Day,Week,Month,Agenda,WorkWeek,EventSettingsModel} from '@syncfusion/ej2-react-schedule'

export default function Scheduler(){  
  const [dataSource, setDataSource] = useState([{EndTime : new Date(2020,3,10,6,30),
    StartTime : new Date(2020,3,10,4, 0), Subject: "Paris"}])
    useEffect(() => {}, [dataSource, setDataSource])
  const [open, setOpen] = useState(false)

  function closePopUp() {
    setOpen(false)
  }

  function getData(description, startTime, endTime){
    console.log(description, startTime, endTime)
    setDataSource([...dataSource,{
        EndTime : endTime,
        StartTime : startTime, 
        Subject: description
      }])
  }

  function addevent() {
    setOpen(true)
  }
  return <div className= "scheduler-main">
    <div className= "left-column">

      <Avatar name="Fereshta Alavy" size="130" textSizeRatio={1.75} round={true} />
      <div>Fereshta profile</div>
      <button className = "btn1">Home</button>{' '}
      <button className = "btn1">Profile</button>{' '}
      <button className = "btn1">Event</button>{' '}
      <button className = "btn1">Members</button>{' '}
      <button className = "btn1">Messages</button>{' '}
      <button className = "btn1">Setting</button>{' '}
      <button className = "btn2">Peeps</button>{' '}
      <div className="txt-event">Create event</div>
      <button className = "btn3" onClick={addevent}>+</button>{' '}
      <PopUp open={open} handleClose={closePopUp} submitData={getData}/>
    
    </div>
    <div className= "scheduler">
    <ScheduleComponent currentView = 'Month' eventSettings={{dataSource}}  >
    <Inject services = {[Day,Week,Month,Agenda,WorkWeek]}/>
   
  </ScheduleComponent>
    </div>
  
  </div>

}

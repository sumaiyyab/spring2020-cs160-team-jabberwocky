import React from 'react';
import logo from './logo.svg';
import './App.css';
import Avatar from 'react-avatar';

// import {ScheduleComponent} from '@syncfusion/ej2-react-schedule'

import {Inject,ScheduleComponent,Day,Week,Month,Agenda,WorkWeek,EventSettingsModel} from '@syncfusion/ej2-react-schedule'
import { Button } from '@syncfusion/ej2-buttons';


class App extends React.Component{
  private localData :EventSettingsModel = {
  dataSource: [{
    EndTime : new Date(2020,3,10,6,30),
    StartTime : new Date(2020,3,10,4, 0),
    
  }]
  
  
};

  public render(){
  return <div className= "App">
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
    <button className = "btn3">+</button>{' '}
    
    
    </div>
    <div className= "scheduler">
    <ScheduleComponent currentView = 'Month' eventSettings={this.localData}  >
    <Inject services = {[Day,Week,Month,Agenda,WorkWeek]}/>
  </ScheduleComponent>
    </div>
  
  </div>



}
}


export default App;

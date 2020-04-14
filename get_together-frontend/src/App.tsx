import React from 'react';
import logo from './logo.svg';
import './App.css';

// import {ScheduleComponent} from '@syncfusion/ej2-react-schedule'

import {Inject,ScheduleComponent,Day,Week,Month,Agenda,WorkWeek,EventSettingsModel} from '@syncfusion/ej2-react-schedule'


class App extends React.Component{
  private localData :EventSettingsModel = {
  dataSource: [{
    EndTime : new Date(2020,3,10,6,30),
    StartTime : new Date(2020,3,10,4, 0),
    
  }]
  
  
};

  public render(){
  return <div className= "App">
    <div className= "left-column">hello world</div>
    <div className= "scheduler">
    <ScheduleComponent currentView = 'Month' eventSettings={this.localData}  >
    <Inject services = {[Day,Week,Month,Agenda,WorkWeek]}/>
  </ScheduleComponent>
    </div>
  
  </div>



}
}


export default App;

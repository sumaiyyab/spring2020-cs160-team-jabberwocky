import React, {useState} from 'react'
import DateFnUtils from "@date-io/date-fns"
import {DatePicker, MuiPickersUtilsProvider} from '@material-ui/pickers'
import { Dialog, TextField, DialogActions, DialogTitle, DialogContent, DialogContentText, Button, Tab } from '@material-ui/core';

export default function PopUp({open, handleClose, submitData}){
    const [selectedStartDate, handleStartDateChange] = useState(new Date())
    const [selectedEndDate, handleEndDateChange] = useState(new Date())
    const [description, setDescription] = useState('')

    const handleTextChange = (event) => {
        setDescription(event.target.value)
    }

    function sendData(){
        submitData(description, selectedStartDate, selectedEndDate)
    }
    
    return (
        <Dialog id = "dialog" open={open} onClose={handleClose} aria-labelledby="form-dialog-title" onExit={sendData}>
        <DialogTitle id="form-dialog-title">Add Event</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Select time and date and enter a description for your event. 
          </DialogContentText>
          <MuiPickersUtilsProvider utils={DateFnUtils}>
            <DatePicker value={selectedStartDate} onChange={handleStartDateChange} label="From"/>
            <Tab/>
            <DatePicker value={selectedEndDate} onChange={handleEndDateChange} label="To"/>
        </MuiPickersUtilsProvider>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Description"
            type="text"
            onChange={handleTextChange}
            value={description}
            fullWidth
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={handleClose} color="primary">
            Add
          </Button>
        </DialogActions>
      </Dialog>        
    )
    
}
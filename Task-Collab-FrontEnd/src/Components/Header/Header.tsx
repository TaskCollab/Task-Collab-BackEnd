// src/components/Header.tsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Box, Dialog, DialogTitle, DialogContent, TextField, DialogActions, Select, MenuItem, FormControl, InputLabel, SelectChangeEvent } from '@mui/material';
import { createTask } from '../../API/HeaderAPICall'; 

const Header: React.FC = () => {
  const navigate = useNavigate();
  const [openDialog, setOpenDialog] = useState(false);
  const [newTask, setNewTask] = useState({
    taskTitle: '',
    description: '',
    assignedTo: 'John Doe', // default value
    status: 'Open', // default value
    deadline: '',
  });

  const handleOpenDialog = () => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setNewTask({
      taskTitle: '',
      description: '',
      assignedTo: 'John Doe',
      status: 'Open',
      deadline: '',
    });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setNewTask({ ...newTask, [e.target.name]: e.target.value });
  };

  const handleSelectChange = (e: SelectChangeEvent<string>) => {
    setNewTask({ ...newTask, [e.target.name!]: e.target.value as string });
  };

  const handleCreateTask = async () => {
    try {
        const deadlineDate = new Date(newTask.deadline).toISOString();
        await createTask({
            taskTitle: newTask.taskTitle,
            description: newTask.description,
            assignedTo: newTask.assignedTo,
            status: newTask.status,
            deadline: deadlineDate,
        });
        handleCloseDialog();
        // Optionally, refresh task list or show success message
    } catch (error) {
        console.error('Error creating task:', error);
        // Show error message
    }
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          <Button color="inherit" onClick={() => navigate('/home')}>Home</Button>
        </Typography>
        <Button color="inherit" onClick={handleOpenDialog}>New Task</Button>
      </Toolbar>

      <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth maxWidth="sm">
        <DialogTitle>Create New Task</DialogTitle>
        <DialogContent>
          <TextField label="Title" name="taskTitle" value={newTask.taskTitle} onChange={handleInputChange} fullWidth margin="normal" />
          <TextField label="Description" name="description" value={newTask.description} onChange={handleInputChange} fullWidth margin="normal" multiline rows={4} />
          <FormControl fullWidth margin="normal">
            <InputLabel id="assigned-to-label">Assigned To</InputLabel>
            <Select labelId="assigned-to-label" id="assigned-to" name="assignedTo" value={newTask.assignedTo} onChange={handleSelectChange} label="Assigned To">
              <MenuItem value="John Doe">John Doe</MenuItem>
              <MenuItem value="Jane Smith">Jane Smith</MenuItem>
            </Select>
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel id="status-label">Status</InputLabel>
            <Select labelId="status-label" id="status" name="status" value={newTask.status} onChange={handleSelectChange} label="Status">
              <MenuItem value="Open">Open</MenuItem>
              <MenuItem value="In Progress">In Progress</MenuItem>
              <MenuItem value="Completed">Completed</MenuItem>
            </Select>
          </FormControl>
          <TextField label="Deadline" name="deadline" type="date" value={newTask.deadline} onChange={handleInputChange} fullWidth margin="normal" InputLabelProps={{ shrink: true }} />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">Cancel</Button>
          <Button onClick={handleCreateTask} color="primary">Create</Button>
        </DialogActions>
      </Dialog>
    </AppBar>
  );
};

export default Header;

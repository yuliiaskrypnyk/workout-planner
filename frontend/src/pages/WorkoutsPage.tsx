import {useEffect, useState} from "react";
import {Workout} from "../types/Workout.ts";
import {deleteWorkout, getWorkouts} from "../api/workoutApi.ts";
import {Box, Button, Grid, IconButton, List, ListItem, Typography} from "@mui/material";
import {Link, useNavigate} from "react-router-dom";
import DeleteButton from "../components/DeleteButton.tsx";
import EditIcon from "@mui/icons-material/Edit";

function WorkoutsPage() {
    const [workouts, setWorkouts] = useState<Workout[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        getWorkouts()
            .then(setWorkouts)
            .catch((error) => console.error(error));
    }, []);

    const handleEditClick = (id: string) => {
        navigate(`/workouts/${id}`, {state: {isEditing: true}});
    };

    const removeWorkoutFromList = (id: string) => {
        setWorkouts((prevWorkouts) => prevWorkouts.filter((workout) => workout.id !== id));
    };

    const handleDelete = (id: string) => {
        deleteWorkout(id)
            .then(() => {
                removeWorkoutFromList(id)
            })
            .catch(console.error);
    };

    return (
        <Box>
            <Typography variant="h5" sx={{margin: 2}}>Workout List</Typography>

            <Grid container spacing={2} justifyContent="flex-start" sx={{marginBottom: 3}}>
                <Grid component="div">
                    <Link to={`/workouts/new`}>
                        <Button variant="contained" color="primary">
                            New Workout
                        </Button>
                    </Link>
                </Grid>
            </Grid>

            <List>
                {workouts.map((workout) => (
                    <ListItem key={workout.id}
                              sx={{
                                  '&:hover': {
                                      backgroundColor: '#e1f5fe',
                                      borderRadius: 1,
                                      transition: 'all 0.3s ease'
                                  },
                                  border: '1px solid #ddd',
                                  borderRadius: 1,
                                  marginBottom: 1,
                                  padding: '8px 16px',
                                  backgroundColor: '#fff',
                                  display: 'flex',
                                  justifyContent: 'space-between',
                                  alignItems: 'center',
                              }}
                    >
                        <Box component={Link} to={`/workouts/${workout.id}`}
                             sx={{textDecoration: "none", color: "inherit", flex: 1}}>
                            {workout.name}
                        </Box>

                        <Box sx={{display: "flex", gap: 1}}>
                            <IconButton onClick={() => handleEditClick(workout.id)} color="primary">
                                <EditIcon/>
                            </IconButton>
                            <DeleteButton handleDelete={handleDelete} id={workout.id} itemType="workout"/>
                        </Box>
                    </ListItem>
                ))}
            </List>
        </Box>
    );
}

export default WorkoutsPage;

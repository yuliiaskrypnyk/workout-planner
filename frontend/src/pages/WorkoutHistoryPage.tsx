import {Accordion, AccordionSummary, AccordionDetails, Typography, List, ListItem, ListItemText, Box} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {WorkoutSession} from "../types/WorkoutSession.ts";
import {useEffect, useState} from "react";
import {getWorkoutHistory} from "../api/workoutApi.ts";
import LoadingIndicator from "../components/LoadingIndicator.tsx";

function WorkoutHistoryPage() {
    const [history, setHistory] = useState<WorkoutSession[]>([]);

    useEffect(() => {
        getWorkoutHistory()
            .then(setHistory)
            .catch((error) => console.error(error));
    }, []);

    if (history.length === 0) {
        return <LoadingIndicator/>;
    }

    return (
        <div>
            <Typography variant="h5" sx={{margin: 2}}>Workout History</Typography>

            {history.map(session => (
                <Box key={session.id} sx={{ mb: 3, borderRadius: 3, overflow: "hidden", boxShadow: 1 }}>
                    <Box sx={{
                        backgroundColor: "#e3f2fd",
                        px: 2,
                        py: 1.5,
                        borderTopLeftRadius: 12,
                        borderTopRightRadius: 12
                    }}>
                        <Typography variant="subtitle2" sx={{fontWeight: "bold"}}>
                            {new Date(session.startTime).toLocaleDateString("en-US", {
                                weekday: "short",
                                day: "numeric",
                                month: "long",
                                year: "numeric"
                            })}
                        </Typography>
                    </Box>

                    <Accordion sx={{boxShadow: "none"}}>
                        <AccordionSummary expandIcon={<ExpandMoreIcon/>}>
                            <Typography variant="body1">
                                {session.workoutName}
                            </Typography>
                        </AccordionSummary>

                        <AccordionDetails>
                            <List>
                                {session.exercises.map((exercise) => (
                                    <ListItem key={exercise.exerciseId} alignItems="flex-start"  sx={{ px: 0 }}>
                                        <img
                                            src={`/images/exercises/${exercise.exerciseImage}`}
                                            alt={exercise.exerciseName}
                                            style={{width: 90, height: 50, marginRight: 16}}
                                        />
                                        <ListItemText
                                            primary={exercise.exerciseName}
                                            secondary={`Sets: ${exercise.sets} Reps: ${exercise.reps} Weight: ${exercise.weight} kg`}
                                        />
                                    </ListItem>
                                ))}
                            </List>
                        </AccordionDetails>
                    </Accordion>
                </Box>
            ))}
        </div>
    );
}

export default WorkoutHistoryPage;
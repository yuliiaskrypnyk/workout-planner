import {Card, CardContent, Grid, Typography} from "@mui/material";
import {Link} from "react-router-dom";
import {ExerciseData} from "../../types/Workout.ts";

interface Props {
    exercise: ExerciseData;
}

function WorkoutExerciseCard({exercise}: Readonly<Props>) {
    return (
        <Card key={exercise.exerciseId} sx={{marginBottom: 2, borderRadius: 3, boxShadow: 3}}>
            <CardContent>
                <Grid container spacing={2} alignItems="center">
                    <Grid component="div">
                        <img
                            src={`/images/exercises/${exercise.exerciseImage}`}
                            alt={exercise.exerciseName}
                            style={{width: 120, height: 70}}
                        />
                    </Grid>
                    <Grid component="div">
                        <Link to={`/exercise/${exercise.exerciseId}`} target="_blank" style={{textDecoration: 'none'}}>
                            <Typography sx={{
                                marginRight: 1,
                                marginBottom: 0.5,
                                color: 'primary.main'
                            }}>{exercise.exerciseName}</Typography>
                        </Link>
                        <Typography sx={{display: 'inline', marginRight: 1}}>
                            Sets: {exercise.sets}
                        </Typography>
                        <Typography sx={{display: 'inline', marginRight: 1}}>
                            Reps: {exercise.reps}
                        </Typography>
                        <Typography sx={{display: 'inline', marginRight: 1}}>
                            Weight: {exercise.weight} kg
                        </Typography>
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}

export default WorkoutExerciseCard;
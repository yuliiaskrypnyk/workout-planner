import {Card, CardContent, Grid, Typography} from "@mui/material";
import {Link} from "react-router-dom";
import {ExerciseData} from "../../types/Workout.ts";

interface Props {
    exercise: ExerciseData;
}

function WorkoutExerciseCard({exercise}: Props) {
    return (
        <Card key={exercise.exerciseId} sx={{marginBottom: 1}}>
            <CardContent>
                <Grid container spacing={2} alignItems="center">
                    <Grid component="div">
                        <img
                            src={`/images/exercises/${exercise.exerciseImage}`}
                            alt={exercise.exerciseName}
                            style={{width: 125, height: 75}}
                        />
                    </Grid>
                    <Grid component="div">
                        <Link to={`/exercise/${exercise.exerciseId}`} target="_blank" style={{textDecoration: 'none'}}>
                            <Typography sx={{
                                marginRight: 1,
                                width: 220,
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
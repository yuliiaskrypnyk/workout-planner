import {Route, Routes} from "react-router-dom";
import {Box} from "@mui/material";
import ExercisesPage from "./pages/exercise/ExercisesPage.tsx";
import Footer from "./components/Footer.tsx";
import Header from "./components/Header.tsx";
import ExerciseDetails from "./pages/exercise/ExerciseDetails.tsx";
import NewWorkout from "./pages/NewWorkout.tsx";
import WorkoutDetails from "./pages/WorkoutDetails.tsx";
import EditWorkout from "./pages/EditWorkout.tsx";
import StartWorkoutPage from "./pages/StartWorkoutPage.tsx";
import WorkoutsPage from "./pages/WorkoutsPage.tsx";

function App() {
    return (
        <Box sx={{display: "flex", flexDirection: "column", minHeight: "100vh"}}>
            <Header/>
            <Box sx={{flexGrow: 1, p: 2}}>
                <Routes>
                    <Route path="/" element={<WorkoutsPage/>}/>
                    <Route path="/workout/new" element={<NewWorkout/>}/>
                    <Route path="/workout/:id/edit" element={<EditWorkout/>}/>
                    <Route path="/workout/:id" element={<WorkoutDetails/>}/>
                    <Route path="/workout/:id/start" element={<StartWorkoutPage/>}/>
                    <Route path="/exercises" element={<ExercisesPage/>}/>
                    <Route path="/exercise/:id" element={<ExerciseDetails/>}/>
                </Routes>
            </Box>
            <Footer/>
        </Box>
    )
}

export default App
import {Route, Routes} from "react-router-dom";
import WorkoutsPage from "./pages/WorkoutsPage.tsx";
import ExercisesPage from "./pages/ExercisesPage.tsx";
import Footer from "./components/Footer.tsx";
import Header from "./components/Header.tsx";
import ExerciseDetails from "./pages/ExerciseDetails.tsx";
import NewWorkout from "./pages/NewWorkout.tsx";
import WorkoutDetails from "./pages/WorkoutDetails.tsx";
import {Box} from "@mui/material";

function App() {

    return (
        <Box sx={{display: "flex", flexDirection: "column", minHeight: "100vh"}}>
            <Header/>
            <Box sx={{flexGrow: 1, p: 2}}>
                <Routes>
                    <Route path="/workouts" element={<WorkoutsPage/>}/>
                    <Route path="/workouts/new" element={<NewWorkout/>}/>
                    <Route path="/workouts/:id" element={<WorkoutDetails/>}/>
                    <Route path="/exercises" element={<ExercisesPage/>}/>
                    <Route path="/exercises/:id" element={<ExerciseDetails/>}/>
                </Routes>
            </Box>
            <Footer/>
        </Box>
    )
}

export default App
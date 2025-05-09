import {Route, Routes} from "react-router-dom";
import {Box} from "@mui/material";
import Footer from "./components/Footer.tsx";
import Header from "./components/Header.tsx";
import HomePage from "./pages/HomePage.tsx";
import WorkoutsPage from "./pages/workout/WorkoutsPage.tsx";
import NewWorkout from "./pages/workout/NewWorkout.tsx";
import EditWorkout from "./pages/workout/EditWorkout.tsx";
import WorkoutDetails from "./pages/workout/WorkoutDetails.tsx";
import StartWorkoutPage from "./pages/workout/StartWorkoutPage.tsx";
import WorkoutHistoryPage from "./pages/WorkoutHistoryPage.tsx";
import ExercisesPage from "./pages/exercise/ExercisesPage.tsx";
import ExerciseDetails from "./pages/exercise/ExerciseDetails.tsx";
import ProtectedRoutes from "./components/ProtectedRoutes.tsx";
import {AuthProvider} from "./context/AuthContext.tsx";

function App() {
    return (
        <Box sx={{display: "flex", flexDirection: "column", minHeight: "100vh"}}>
            <AuthProvider>
                <Header/>
                <Box sx={{flexGrow: 1, p: 2}}>
                    <Routes>
                        <Route path="/" element={<HomePage/>}/>
                        <Route element={<ProtectedRoutes/>}>
                            <Route path="/workouts" element={<WorkoutsPage/>}/>
                            <Route path="/workout/new" element={<NewWorkout/>}/>
                            <Route path="/workout/:id/edit" element={<EditWorkout/>}/>
                            <Route path="/workout/:id" element={<WorkoutDetails/>}/>
                            <Route path="/workout/:id/start" element={<StartWorkoutPage/>}/>
                            <Route path="/history" element={<WorkoutHistoryPage/>}/>
                            <Route path="/exercises" element={<ExercisesPage/>}/>
                            <Route path="/exercise/:id" element={<ExerciseDetails/>}/>
                        </Route>
                    </Routes>
                </Box>
                <Footer/>
            </AuthProvider>
        </Box>
    )
}

export default App
import './App.css'
import { Route, Routes } from "react-router-dom";
import WorkoutsPage from "./pages/WorkoutsPage.tsx";
import ExercisesPage from "./pages/ExercisesPage.tsx";

function App() {

  return (
        <Routes>
          <Route path="/workouts" element={<WorkoutsPage />} />
          <Route path="/exercises" element={<ExercisesPage />} />
        </Routes>
  )
}

export default App
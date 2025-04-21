import {Navigate} from "react-router-dom";
import {useAuth} from "../context/AuthContext.tsx";

function HomePage() {
    const {user} = useAuth();

    if (user) {
        return <Navigate to="/workouts"/>;
    }

    return (
        <div></div>
    );
}

export default HomePage;
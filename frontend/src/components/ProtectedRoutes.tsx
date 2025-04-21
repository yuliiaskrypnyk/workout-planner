import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "../context/AuthContext.tsx";
import LoadingIndicator from "./LoadingIndicator.tsx";

function ProtectedRoutes() {
    const {user, loading} = useAuth()

    if (loading) return <LoadingIndicator />;

    if (!user) {
        return <Navigate to="/" />;
    }
    return <Outlet />;
}

export default ProtectedRoutes;
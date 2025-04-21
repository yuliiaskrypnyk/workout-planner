import {createContext, useContext, useEffect, useState} from "react";
import {AppUser} from "../types/AppUser.ts";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type AuthContextType = {
    user: AppUser | null;
    getUser: () => void;
    loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};

export const AuthProvider = ({children}: { children: React.ReactNode }) => {
    const [user, setUser] = useState<AppUser | null>(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    const getUser = () => {
        axios.get("/api/auth/me")
            .then(response => {
                setUser(response.data);
                if (!loading && response.data) {
                    navigate("/workouts");
                }
            })
            .catch(error => {
                setUser(null);
                console.error(error);
            })
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        getUser();
    }, []);

    return (
        <AuthContext.Provider value={{user, getUser, loading}}>
            {children}
        </AuthContext.Provider>
    );
};
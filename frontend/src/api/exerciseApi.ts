import axios from "axios";

const EXERCISES_URL = "/api/exercises";

export const getExercises = async () => {
    try {
        const response = await axios.get(EXERCISES_URL)
        return response.data;
    } catch (error) {
        console.error("Error fetching exercises:", error);
        throw error;
    }
};

export const getExerciseById = async (id: string) => {
    try {
        const response = await axios.get(`${EXERCISES_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching exercise:", error);
        throw error;
    }
};
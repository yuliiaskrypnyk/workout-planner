import axios from "axios";
import {Workout} from "../types/Workout.ts";

const WORKOUTS_URL = "/api/workouts";
const EXERCISES_URL = "/api/workouts/exercises";

export const getWorkouts = async () => {
    try {
        const response = await axios.get(WORKOUTS_URL)
        return response.data;
    } catch (error) {
        console.error("Error fetching workouts:", error);
        throw error;
    }
};

export const getWorkoutById = async (id: string) => {
    try {
        const response = await axios.get(`${WORKOUTS_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching workout:", error);
        throw error;
    }
};

export const createWorkout = async (newWorkout: Workout) => {
    try {
        const response = await axios.post(WORKOUTS_URL, newWorkout)
        return response.data;
    } catch (error) {
        console.error("Error adding workout:", error);
        throw error;
    }
};

export const editWorkout = async (id: string, editedWorkout: Workout) => {
    try {
        const response = await axios.put(`${WORKOUTS_URL}/${id}`, editedWorkout)
        return response.data;
    } catch (error) {
        console.error("Error updating workout:", error);
        throw error;
    }
};

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
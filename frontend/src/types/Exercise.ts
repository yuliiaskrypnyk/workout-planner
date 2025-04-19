export type Exercise = {
    id: string;
    name: string;
    description?: string;
    image?: string;
    type: ExerciseType;
}

export enum ExerciseType {
    UPPER_BODY = "UPPER_BODY",
    LOWER_BODY = "LOWER_BODY",
}

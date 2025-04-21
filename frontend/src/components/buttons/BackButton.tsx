import {useNavigate} from "react-router-dom";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import StyledButton from "./StyledButton.tsx";

type BackButtonProps = Readonly<{
    text?: string;
    to?: string;
}>;

function BackButton({text = "All Workouts", to = "/workouts"}: BackButtonProps) {
    const navigate = useNavigate();

    const handleGoBack = () => {
        navigate(to);
    };

    return (
        <StyledButton onClick={handleGoBack} startIcon={<ArrowBackIcon/>}>Go back to {text}</StyledButton>
    );
}

export default BackButton;
import {useNavigate} from "react-router-dom";
import {Button} from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

type BackButtonProps = {
    text: string;
}

function BackButton({text}: BackButtonProps) {
    const navigate = useNavigate();

    const handleGoBack = () => {
        navigate(-1);
    };

    return (
        <Button onClick={handleGoBack} variant="contained" sx={{margin: 2}} startIcon={<ArrowBackIcon/>}>
            Go back to the {text}
        </Button>
    );
}

export default BackButton;
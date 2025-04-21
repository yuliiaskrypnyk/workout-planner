import {Box, Card, IconButton, Menu, MenuItem, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import DeleteButton from "../buttons/DeleteButton.tsx";
import {WorkoutDTO} from "../../types/Workout.ts";
import {useState} from "react";
import EditIcon from "@mui/icons-material/Edit";

interface Props {
    workout: WorkoutDTO;
    onDelete: (id: string) => void;
}

function WorkoutCard({workout, onDelete}: Readonly<Props>) {
    const navigate = useNavigate();
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);

    const handleCardClick = () => {
        navigate(`/workout/${workout.id}`);
    };
    const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
        event.stopPropagation();
        setAnchorEl(event.currentTarget);
    };
    const handleMenuClose = () => {
        setAnchorEl(null);
    };
    const handleEdit = () => {
        navigate(`/workout/${workout.id}/edit`);
        handleMenuClose();
    };

    const workoutItemStyle = {
        width: 250,
        height: 120,
        borderRadius: 6,
        boxShadow: '0 8px 15px rgba(0, 0, 0, 0.2)',
        position: 'relative',
        padding: 2,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #1e3a8a, #2563eb)',
        color: 'white',
        '&:hover': {
            background: 'linear-gradient(135deg, #1d4ed8, #2563eb)',
        }
    };

    return (
        <Card sx={workoutItemStyle} onClick={handleCardClick}>
            <Box sx={{position: 'absolute', top: 8, right: 8}}>
                <IconButton onClick={handleMenuOpen}>
                    <MoreVertIcon sx={{color: 'white'}}/>
                </IconButton>
                <Menu
                    anchorEl={anchorEl}
                    open={open}
                    onClose={handleMenuClose}
                    onClick={(e) => e.stopPropagation()}
                    anchorOrigin={{vertical: 'top', horizontal: 'right'}}
                    transformOrigin={{vertical: 'top', horizontal: 'right'}}
                >
                    <MenuItem onClick={handleEdit}>
                        <EditIcon sx={{marginLeft: 1}}/>
                    </MenuItem>
                    <MenuItem>
                        <DeleteButton
                            id={workout.id}
                            handleDelete={(id) => {
                                handleMenuClose();
                                onDelete(id);
                            }}
                            itemType="workout"
                        />
                    </MenuItem>
                </Menu>
            </Box>

            <Typography sx={{fontSize: '1.2rem', paddingX: 2}}>{workout.name}</Typography>
            <Typography sx={{paddingX: 2}}>
                {workout.exerciseCount} {workout.exerciseCount === 1 ? "exercise" : "exercises"}
            </Typography>
        </Card>
    );
}

export default WorkoutCard;
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import {useState} from "react";

type DeleteItemType = 'workout' | 'exercise';

interface DeleteButtonProps {
    handleDelete: (exerciseId: string) => void;
    id: string;
    itemType?: DeleteItemType;
    isAddingExercise?: boolean;
}

const DeleteButton = ({handleDelete, id, itemType, isAddingExercise}: DeleteButtonProps) => {
    const [open, setOpen] = useState(false);

    const handleDeleteClick = () => {
        if (!isAddingExercise) {
            setOpen(true);
        } else {
            handleDelete(id);
        }
    };

    const handleDeleteConfirm = () => {
        handleDelete(id);
        setOpen(false);
    };

    const handleDeleteCancel = () => {
        setOpen(false);
    };

    return (
        <>
            <IconButton onClick={handleDeleteClick} color="secondary">
                <DeleteIcon/>
            </IconButton>
            {!isAddingExercise && (
                <Dialog open={open} onClose={handleDeleteCancel}>
                    <DialogTitle>Deleting a {itemType}</DialogTitle>
                    <DialogContent>
                        <p>Are you sure you want to delete this {itemType}?</p>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleDeleteCancel} color="primary">
                            Cancel
                        </Button>
                        <Button onClick={handleDeleteConfirm} color="secondary">
                            Delete
                        </Button>
                    </DialogActions>
                </Dialog>
            )}
        </>
    )
}

export default DeleteButton;
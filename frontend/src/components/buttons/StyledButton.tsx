import {Button, ButtonProps} from "@mui/material";

const StyledButton = ({variant = "contained", ...props}: ButtonProps) => (
    <Button
        variant={variant}
        sx={{
            background: variant === "contained" ? "#42a5f5" : "transparent",
            color: variant === "contained" ? "white" : "#42a5f5",
            border: variant === "outlined" ? "1px solid #42a5f5" : undefined,
            borderRadius: 3,
            textTransform: 'none',
            marginRight: 1,
            '&:hover': {
                background: variant === "contained" ? "#2196f3" : "rgba(66, 165, 245, 0.1)",
            },
        }}
        {...props}
    />
);

export default StyledButton;

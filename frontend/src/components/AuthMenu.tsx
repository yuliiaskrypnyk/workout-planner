import {useState} from "react";
import {Avatar, Button, ListItemIcon, Menu, MenuItem, Typography} from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import LoginIcon from "@mui/icons-material/Login";
import {useAuth} from "../context/AuthContext.tsx";

function getHost() {
    return window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
}

export default function AuthMenu() {
    const {user} = useAuth();
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const openMenu = Boolean(anchorEl);

    const handleLogin = () => {
        window.open(getHost() + '/oauth2/authorization/github', '_self');
    };

    const handleLogout = () => {
        window.open(getHost() + '/logout', '_self');
    };

    const handleAvatarClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    if (!user) {
        return (
            <Button color="inherit" onClick={handleLogin} startIcon={<LoginIcon/>} sx={{ textTransform: "none" }}>
                Sign in with GitHub
            </Button>
        );
    }

    return (
        <>
            <Typography variant="body1" sx={{marginRight: 2}}>
                {user.name}
            </Typography>
            <Avatar
                alt={user.name}
                src={user.avatarUrl}
                onClick={handleAvatarClick}
                sx={{width: 32, height: 32, cursor: "pointer"}}
            />
            <Menu
                anchorEl={anchorEl}
                open={openMenu}
                onClose={handleMenuClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                <MenuItem
                    onClick={() => {
                        handleMenuClose();
                        handleLogout();
                    }}
                >
                    <ListItemIcon>
                        <LogoutIcon fontSize="small"/>
                    </ListItemIcon>
                    Sign out
                </MenuItem>
            </Menu>
        </>
    );
}
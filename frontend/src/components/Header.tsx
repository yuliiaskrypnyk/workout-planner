import {useState} from "react";
import {Link} from "react-router-dom";
import {AppBar, Box, Drawer, IconButton, List, ListItem, ListItemIcon, Toolbar, Typography} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import ListAltIcon from '@mui/icons-material/ListAlt';
import HistoryIcon from '@mui/icons-material/History';
import AuthMenu from "./AuthMenu.tsx";
import {useAuth} from "../context/AuthContext.tsx";

function Header() {
    const {user} = useAuth();
    const [open, setOpen] = useState(false);

    const toggleDrawer = (open: boolean): void => {
        setOpen(open);
    };

    const DrawerList = (
        <List sx={{width: 220}}>
            <ListItem component="li" onClick={() => toggleDrawer(false)}>
                <ListItemIcon>
                    <FitnessCenterIcon/>
                </ListItemIcon>
                <Link to="/workouts" style={{textDecoration: 'none', color: 'inherit'}}>Workouts</Link>
            </ListItem>
            <ListItem component="li" onClick={() => toggleDrawer(false)}>
                <ListItemIcon>
                    <ListAltIcon/>
                </ListItemIcon>
                <Link to="/exercises" style={{textDecoration: 'none', color: 'inherit'}}>Exercises</Link>
            </ListItem>
            <ListItem component="li" onClick={() => toggleDrawer(false)}>
                <ListItemIcon>
                    <HistoryIcon/>
                </ListItemIcon>
                <Link to="/history" style={{textDecoration: 'none', color: 'inherit'}}>Workouts History</Link>
            </ListItem>
        </List>
    );

    return (
        <Box sx={{display: 'flex'}}>
            <AppBar position="static">
                <Toolbar>
                    {user && (
                        <IconButton size="large" edge="start" color="inherit" aria-label="menu" sx={{mr: 2}}
                                    onClick={() => toggleDrawer(true)}>
                            <MenuIcon/>
                        </IconButton>
                    )}
                    <Typography variant="h6" component="div" sx={{flexGrow: 1}}>Workout Planner</Typography>

                    <AuthMenu/>

                </Toolbar>
            </AppBar>
            <Drawer anchor="left" open={open} onClose={() => toggleDrawer(false)}
                    slotProps={{
                        paper: {
                            sx: {
                                borderTopRightRadius: 10,
                                borderBottomRightRadius: 10,
                                overflow: 'hidden',
                                backgroundColor: 'background.paper'
                            }
                        }
                    }}
            >{DrawerList}</Drawer>
        </Box>
    );
}

export default Header;
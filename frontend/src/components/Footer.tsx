import {Box, Typography} from "@mui/material";

function Footer() {
    return (
        <Box component="footer" sx={{
            width: '100%',
            py: 2,
            textAlign: 'center',
            backgroundColor: 'primary.main',
            color: 'white',
            mt: 'auto',
        }}>
            <Typography variant="body2">
                © {new Date().getFullYear()} Workout Planner. All rights reserved.
            </Typography>
        </Box>
    );
}

export default Footer;
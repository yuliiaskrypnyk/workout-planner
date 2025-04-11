import {Box, CircularProgress, Typography} from '@mui/material';

interface LoadingIndicatorProps {
    text?: string;
}

const LoadingIndicator = ({text = 'Loading...'}: LoadingIndicatorProps) => (
    <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', mt: 4}}>
        <CircularProgress />
        <Typography variant="body2" sx={{mt: 2}}>{text}</Typography>
    </Box>
);

export default LoadingIndicator;
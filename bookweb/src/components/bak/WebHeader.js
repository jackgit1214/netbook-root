import React from 'react';
import clsx from 'clsx';
import Toolbar from '@material-ui/core/Toolbar/Toolbar';
import IconButton from '@material-ui/core/IconButton/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import Typography from '@material-ui/core/Typography/Typography';
import Badge from '@material-ui/core/Badge/Badge';
import AppBar from '@material-ui/core/AppBar/AppBar';
import NotificationsIcon from '@material-ui/icons/Notifications';

import useStyles from '../../style/style';

function WebHeader(props) {
    let classes = useStyles();

    let open = props.open;

    let handleMenu = () => {
        props.openMethod();
    }
    return (
        <AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
            <Toolbar className={classes.toolbar}>
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="open drawer"
                    onClick={handleMenu}
                    className={clsx(classes.menuButton)} //open && classes.menuButtonHidden
                >
                    <MenuIcon/>
                </IconButton>
                <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                    网站后台监控系统
                </Typography>
                <IconButton color="inherit">
                    <Badge badgeContent={4} color="secondary">
                        <NotificationsIcon/>
                    </Badge>
                </IconButton>
            </Toolbar>
        </AppBar>
    )
}

export default WebHeader;

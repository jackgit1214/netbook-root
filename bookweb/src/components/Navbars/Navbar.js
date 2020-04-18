import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
// @material-ui/core components
import {makeStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Hidden from '@material-ui/core/Hidden';
import Typography from '@material-ui/core/Typography/Typography';
// @material-ui/icons
import Menu from '@material-ui/icons/Menu';
// core components
import AdminNavbarLinks from './AdminNavbarLinks.js';
import RTLNavbarLinks from './RTLNavbarLinks.js';

import styles from 'assets/jss/material-dashboard-react/components/headerStyle.js';

const useStyles = makeStyles(styles);

export default function Header(props) {
    const classes = useStyles();

    const {color} = props;
    const appBarClasses = classNames({
        [' ' + classes[color]]: color,
    });
    return (
        <AppBar className={classes.appBar + appBarClasses}>
            <Toolbar className={classes.container}>

                {/* Here we create navbar brand, based on route name */}
                <Hidden smDown implementation="css">
                    <div className={classes.flex}>
                        <IconButton
                            color="inherit"
                            aria-label="open drawer"
                            onClick={props.menuControler}
                        >
                            <Menu/>
                        </IconButton>
                    </div>
                </Hidden>

                <div className={classes.flex}>
                    <Typography color="inherit" href="#" className={classes.title}>
                        后台管理系统
                    </Typography>
                </div>
                <Hidden smDown implementation="css">
                    <AdminNavbarLinks/>
                </Hidden>
                <Hidden mdUp implementation="css">
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={props.handleDrawerToggle}
                    >
                        <Menu/>
                    </IconButton>
                </Hidden>
            </Toolbar>
        </AppBar>
    );
}

Header.propTypes = {
    color: PropTypes.oneOf(['purple', 'blue', 'green', 'orange', 'red']),
    rtlActive: PropTypes.bool,
    handleDrawerToggle: PropTypes.func,
    routes: PropTypes.arrayOf(PropTypes.object),
};

import React from 'react';
import clsx from 'clsx';
import IconButton from '@material-ui/core/IconButton/IconButton';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import Divider from '@material-ui/core/Divider/Divider';
import { NavLink } from "react-router-dom";
import List from '@material-ui/core/List/List';
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Icon from "@material-ui/core/Icon";
import {mainListItems, secondaryListItems} from './listItems';
import useStyles from '../../style/style';
import Drawer from '@material-ui/core/Drawer/Drawer';

import {BrowserRouter as Router, Link} from 'react-router-dom';



export default function Menu(props) {
    const { open, routes } = props;
    const classes = useStyles();
    let links = (
        <List>
            {routes.map((prop, key) => {
                var activePro = " ";
                var listItemClasses;
                return (
                    <NavLink
                        to={ prop.path}
                        // className={activePro + classes.item}
                        activeClassName="active"
                        key={key}
                    >
                        <ListItem button >
                                <prop.icon
                                    className={classes.itemIcon}
                                />
                            <ListItemText
                                primary={ prop.label}
                                disableTypography={true}
                            />
                        </ListItem>
                    </NavLink>
                );
            })}
        </List>
    );

    return (
        <Drawer
            variant="permanent"
            classes={{
                paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
            }}
            open={open}>
            { /*增加顶部空间，左侧菜单不靠近顶部 */}
            <div className={classes.toolbar} />
            {links}
        </Drawer>
        );
}


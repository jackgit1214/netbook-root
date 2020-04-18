import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import PeopleIcon from '@material-ui/icons/People';
import BarChartIcon from '@material-ui/icons/BarChart';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';
import Copyright from './Copyright';
import Chart from './page/Chart';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";

function customMenuLink({ label, to, activeOnlyWhenExact }) {


    return (
        <div >

            <Link to={to}>{label}</Link>
        </div>
    );
}

export default function CustomLinkExample() {
    return (
        <Router>
            <div>
                <customMenuLink
                    activeOnlyWhenExact={true}
                    to="/"
                    label="Home"
                />
                <customMenuLink to="/about" label="About" />

                <hr />

                <Switch>
                    <Route exact path="/">
                        <Copyright/>
                    </Route>
                    <Route path="/about">
                        <Chart />
                    </Route>
                </Switch>
            </div>
        </Router>
    );
}

export const mainListItems = (

  <Router>
      <List component="nav" aria-label="打开首页">
    <ListItem  >
      <ListItemIcon>
        <DashboardIcon />
      </ListItemIcon>
        <Link to="/" >
            <ListItemText primary="首页" />
        </Link>

    </ListItem>
    <ListItem button >
      <ListItemIcon>
        <ShoppingCartIcon />
      </ListItemIcon>
        <Link to="/test" >
            <ListItemText primary="Charts" />
        </Link>

    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <PeopleIcon />
      </ListItemIcon>
      <ListItemText primary="Customers" />
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <BarChartIcon />
      </ListItemIcon>
      <ListItemText primary="Reports" />
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <LayersIcon />
      </ListItemIcon>
      <ListItemText primary="Integrations" />

    </ListItem>
      </List>
  </Router>
);

export const secondaryListItems = (
  <div>
    <ListSubheader inset>Saved reports</ListSubheader>
    <ListItem button>
      <ListItemIcon>
        <AssignmentIcon />
      </ListItemIcon>
      <ListItemText primary="Current month" />
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <AssignmentIcon />
      </ListItemIcon>
      <ListItemText primary="Last quarter" />
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <AssignmentIcon />
      </ListItemIcon>
      <ListItemText primary="Year-end sale" />
    </ListItem>
  </div>
);

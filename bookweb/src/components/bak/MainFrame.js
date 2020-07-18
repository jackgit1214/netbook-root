import React from 'react';
import clsx from 'clsx';
import CssBaseline from '@material-ui/core/CssBaseline/CssBaseline';
import Container from '@material-ui/core/Container/Container';
import Header from './WebHeader';
import useStyles from '../../style/style';
import Copyright from './Copyright';
import Menu from './Menu_Hook';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    useRouteMatch
} from "react-router-dom";
import {routes} from './routes'

const switchRoutes = (
    <Switch>
        {routes.map((prop, key) => {
            console.log(prop.path);
            return (
                <Route
                    path={prop.path}
                    component={prop.component}
                    key={key}
                />
            );

        })}

    </Switch>
);

function Home() {
    let classes = useStyles();
    let [open, setOpen] = React.useState(true);

    let handleDrawer = () => {
        if (open)
            setOpen(false);
        else
            setOpen(true);
    }
    return (
        <div className={classes.root}>
            <CssBaseline/>
            <Header open={open} openMethod={handleDrawer}/>
            <Menu open={open} routes={routes}/>

            <main className={classes.content}>
                <div className={classes.appBarSpacer}/>
                <Container maxWidth="lg" className={classes.container}>
                    {switchRoutes}
                </Container>
                <Copyright/>
            </main>

        </div>
    );

}

export default Home;

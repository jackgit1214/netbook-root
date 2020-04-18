import React from 'react';
import {Route, Switch} from 'react-router-dom';
// creates a beautiful scrollbar
import PerfectScrollbar from 'perfect-scrollbar';
import 'perfect-scrollbar/css/perfect-scrollbar.css';
// @material-ui/core components
import {makeStyles} from '@material-ui/core/styles';
// core components
import Navbar from 'components/Navbars/Navbar.js';
import Footer from 'components/Footer/Footer.js';
import Sidebar from 'components/Sidebar/Sidebar.js';
import routes from 'routes/routes.js';
import styles from 'assets/jss/material-dashboard-react/layouts/adminStyle.js';
import ImportContactsIcon from '@material-ui/icons/ImportContacts';
import bgImage from 'assets/img/sidebar-2.jpg';
import classNames from 'classnames';


let ps;

const switchRoutes = (
    <Switch>
        {routes.map((prop, key) => {
            if (prop.layout === '/admin') {
                return (
                    <Route
                        path={prop.layout + prop.path}
                        component={prop.component}
                        key={key}
                    />
                );
            }
            return null;
        })}
        {/*<Redirect from="/" to="/admin/dashboard" />*/}
    </Switch>
);

const useStyles = makeStyles(styles);

export default function App({...rest}) {
    // styles
    const classes = useStyles();
    let [menuStatus, setMenuStatus] = React.useState(true);
    let mainClasses = classNames(classes.mainPanel, !menuStatus ? classes.mainPanel_expand : '');
    // ref to help us initialize PerfectScrollbar on windows devices
    const mainPanel = React.createRef();
    // states and functions
    const [image, setImage] = React.useState(bgImage);
    const [color, setColor] = React.useState('blue');
    const [fixedClasses, setFixedClasses] = React.useState('dropdown show');
    const [mobileOpen, setMobileOpen] = React.useState(false);
    const handleImageClick = image => {
        setImage(image);
    };
    const handleColorClick = color => {
        setColor(color);
    };
    const handleFixedClick = () => {
        if (fixedClasses === 'dropdown') {
            setFixedClasses('dropdown show');
        } else {
            setFixedClasses('dropdown');
        }
    };
    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };
    const menuControler = (e) => {
        e.preventDefault();
        setMenuStatus(!menuStatus);
    };
    let makeBrand = () => {
        var name;
        routes.map(prop => {
            if (window.location.href.indexOf(prop.layout + prop.path) !== -1) {
                name = prop.name;
            }
            return null;
        });
        if (name == undefined) {
            name = '';
        }
        return name;
    };
    const resizeFunction = () => {
        if (window.innerWidth >= 960) {
            setMobileOpen(false);
        }
    };
    // initialize and destroy the PerfectScrollbar plugin
    React.useEffect(() => {
        if (navigator.platform.indexOf('Win') > -1) {
            ps = new PerfectScrollbar(mainPanel.current, {
                suppressScrollX: true,
                suppressScrollY: false,
            });
            document.body.style.overflow = 'hidden';
        }
        window.addEventListener('resize', resizeFunction);
        // Specify how to clean up after this effect:
        return function cleanup() {
            if (navigator.platform.indexOf('Win') > -1) {
                ps.destroy();
            }
            window.removeEventListener('resize', resizeFunction);
        };
    }, [mainPanel]);
    return (
        <div className={classes.wrapper}>
            <Sidebar
                routes={routes}
                logoText={'书'}
                logo={ImportContactsIcon}
                image={image}
                handleDrawerToggle={handleDrawerToggle}
                open={mobileOpen}
                color={color}
                menuStatus={menuStatus}
                menuControler={menuControler}
                {...rest}
            />
            <div className={mainClasses} ref={mainPanel}>
                <Navbar
                    routes={routes}
                    handleDrawerToggle={handleDrawerToggle}
                    menuControler={menuControler}
                    color={color}
                    {...rest}
                />
                <div className={classes.mainContent}>
                    {/*<Paper elevation={0} className={classes.paper}>*/}
                    {/*<Breadcrumbs maxItems={2} aria-label="breadcrumb">*/}
                    {/*/!*<Link color="inherit" href="#" onClick={handleClick}>*!/*/}
                    {/*/!*{makeBrand()}*!/*/}
                    {/*/!*</Link>*!/*/}
                    {/*<Typography color="textPrimary"> {makeBrand()}</Typography>*/}
                    {/*</Breadcrumbs>*/}
                    {/*</Paper>*/}
                    <div className={classes.container}>{switchRoutes}</div>
                </div>

                <Footer/>
                {/*<FixedPlugin*/}
                {/*handleImageClick={handleImageClick}*/}
                {/*handleColorClick={handleColorClick}*/}
                {/*bgColor={color}*/}
                {/*bgImage={image}*/}
                {/*handleFixedClick={handleFixedClick}*/}
                {/*fixedClasses={fixedClasses}*/}
                {/*/>*/}
            </div>
        </div>
    );
}

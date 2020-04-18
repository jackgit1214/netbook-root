import {container, drawerWidth, headerBarHeight,footerBarHeight,transition} from 'assets/jss/material-dashboard-react.js';

const appStyle = theme => ({
    wrapper: {
        position: 'relative',
        top: '0',
        height: '100vh',
    },
    mainPanel: {
        [theme.breakpoints.up('md')]: {
            width: `calc(100% - ${drawerWidth}px)`,
        },
        overflow: 'auto',
        position: 'relative',
        float: 'right',
        ...transition,
        maxHeight: '100%',
        width: '100%',
        overflowScrolling: 'touch',
    },
    mainPanel_expand:{
        width: `calc(100% - 60px)`,
    },
    mainContent: {
        marginTop: `${headerBarHeight}px`, //header 区域高度
        padding: '5px 5px',


    },
    container:{
        ...container,
        paddingLeft:"1px",
        paddingRight:"1px",
        height: `calc(100vh - ${headerBarHeight}px - ${footerBarHeight}px -  ${10}px)`, //需要计算 去除bar,footer以及paddingTop,paddingBottom值
        overflowY:"auto",
    },
    map: {
        marginTop: '70px',
    },
    paper: {
        backgroundColor: 'transparent',
        padding: "3px 5px"
    },
});

export default appStyle;

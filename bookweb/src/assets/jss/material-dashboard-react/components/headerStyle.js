import {
    container,
    dangerColor,
    defaultBoxShadow,
    defaultFont,
    grayColor,
    infoColor,
    primaryColor,
    successColor,
    warningColor,
    whiteColor,
} from 'assets/jss/material-dashboard-react.js';

const headerStyle = () => ({
    appBar: {
        backgroundColor: 'transparent !important',
        boxShadow: 'none',
        borderBottom: '0',
        marginBottom: '0',
        position: 'absolute',
        width: '100%',
        paddingTop: '10px',
        zIndex: '1029',
        color: grayColor[7],
        border: '0',
        borderRadius: '3px',
        padding: '10px 0',
        transition: 'all 150ms ease 0s',
        minHeight: '50px',
        display: 'block',
    },
    container: {
        ...container,
        minHeight: '50px',
    },
    flex: {
        flex: 1,
    },
    title: {
        ...defaultFont,
        letterSpacing: 'unset',
        lineHeight: '30px',
        fontSize: '18px',
        padding: '12px 10px',
        borderRadius: '3px',
        textTransform: 'none',
        color: 'inherit',
        margin: '0',
        '&:hover,&:focus': {
            background: 'transparent',
        },
    },
    appResponsive: {
        top: '8px',
    },
     purple : {
        backgroundColor: primaryColor[0]+"!important",
        color: whiteColor+"!important",
        ...defaultBoxShadow,
    },
    blue: {
        backgroundColor: infoColor[0] +"!important",
        color: whiteColor+"!important",
        ...defaultBoxShadow,
    },
    green: {
        backgroundColor: successColor[0]+"!important",
        color: whiteColor+"!important",
        ...defaultBoxShadow,
    },
    orange: {
        backgroundColor: warningColor[0]+"!important",
        color: whiteColor+"!important",
        ...defaultBoxShadow,
    },
    red: {
        backgroundColor: dangerColor[0]+"!important",
        color: whiteColor+"!important",
        ...defaultBoxShadow,
    },

});

export default headerStyle;

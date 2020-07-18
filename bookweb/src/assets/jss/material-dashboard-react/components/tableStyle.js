import {
    dangerColor,
    defaultFont,
    grayColor,
    infoColor,
    primaryColor,
    roseColor,
    successColor,
    warningColor,
} from 'assets/jss/material-dashboard-react.js';

const tableStyle = theme => ({

    checkboxLabel: {
        width: 35
    },

    warningTableHeader: {
        color: warningColor[0],
    },
    primaryTableHeader: {
        color: primaryColor[0],
    },
    dangerTableHeader: {
        color: dangerColor[0],
    },
    successTableHeader: {
        color: successColor[0],
    },
    infoTableHeader: {
        color: infoColor[0],
    },
    roseTableHeader: {
        color: roseColor[0],
    },
    grayTableHeader: {
        color: grayColor[0],
    },
    table: {
        marginBottom: '0',
        width: '100%',
        maxWidth: '100%',
        backgroundColor: 'transparent',
        borderSpacing: '0',
        borderCollapse: 'collapse',
    },
    tableHeadCell: {
        color: 'inherit',
        ...defaultFont,
        '&, &$tableCell': {
            fontSize: '1em',
        },
    },
    tableCell: {
        ...defaultFont,
        lineHeight: '1.42857143',
        padding: '5px 5px',
        verticalAlign: 'middle',
        border: '1px solid #eFeFeF',
        fontSize: '0.8125rem',
    },
    tableResponsive: {
        width: '100%',
        marginTop: theme.spacing(3),
        overflowX: 'auto',
    },
    tableHeadRow: {
        height: '20px',
        color: 'inherit',
        display: 'table-row',
        outline: 'none',
        verticalAlign: 'middle',
    },
    tableBodyRow: {
        height: '20px',
        color: 'inherit',
        display: 'table-row',
        outline: 'none',
        verticalAlign: 'middle',
    },
    tableTurnPage: {
        backgroundColor: '#f0e5c7',
        minHeight: "35px"
    },
    tableContainer: {
        width: '100%',
        overflowX: 'auto',
        minHeight: 340,
    },

});

export default tableStyle;

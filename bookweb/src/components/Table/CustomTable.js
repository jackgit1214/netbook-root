import React from 'react';
import PropTypes from 'prop-types';
// @material-ui/core components
import {withStyles,makeStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import IconButton from '@material-ui/core/IconButton/IconButton';
import Icon from '@material-ui/core/Icon';
import LastPageIcon from '@material-ui/icons/LastPage';
import FirstPageIcon from '@material-ui/icons/FirstPage';
import KeyboardArrowLeft from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRight from '@material-ui/icons/KeyboardArrowRight';
import TablePagination from '@material-ui/core/TablePagination/TablePagination';
import TableFooter from '@material-ui/core/TableFooter/TableFooter';
import Tooltip from '@material-ui/core/Tooltip';
import Card from 'components/Card/Card.js';
import CardBody from 'components/Card/CardBody.js';
import CardFooter from 'components/Card/CardFooter';
import Fab from '@material-ui/core/Fab';
import CircularProgress from '@material-ui/core/CircularProgress';
import {useSelector} from 'react-redux';
// core components
import styles from 'assets/jss/material-dashboard-react/components/tableStyle.js';

const useStyles = makeStyles(styles);
const useStyles1 = makeStyles(theme => ({
    root: {
        flexShrink: 0,
        marginLeft: theme.spacing(2.5),
    },
    pageButton: {
        padding: '2px 2px',
    },
}));

const StyledTableCell = withStyles(theme => ({
    head: {
        color: theme.palette.common.black,
    },
    body: {
        fontSize: 14,
    },
    stickyHeader: {
        backgroundColor: '#cccccc',
    },
}))(TableCell);

const StyledTableRow = withStyles(theme => ({
    root: {
        '&:nth-of-type(even)': {
            backgroundColor: theme.palette.background.default,
        },
    },
}))(TableRow);

export default function CustomPaginationTable(props) {
    const classes = useStyles();
    const {tableHead, tableData, page, tableHeaderColor, Sequence, Selections, tableHeight, physicalPageHandle, primaryKey, actions} = props;
    const [checked, setChecked] = React.useState([]);
    const pageInfo = useSelector(() => page);

    const handleRowsPerPageChange = (event) => {
        let rows = parseInt(event.target.value, 10);
        if (rows == -1) {
            rows = page.totalRows;
        }
        if (physicalPageHandle) {
            physicalPageHandle({pageSize: rows, curPage: 1});
        }
    };
    const handlePageChange = (event, tmpPage) => {
        if (physicalPageHandle) {
            physicalPageHandle({...pageInfo, curPage: tmpPage + 1});
        }
    };

    //选择checkbox变更
    const handleChanged = (isChecked, item) => {
        const currentIndex = checked.indexOf(item);
        const newChecked = [...checked];
        if (currentIndex === -1 && isChecked) {
            newChecked.push(item);
        } else {
            newChecked.splice(currentIndex, 1);
        }
        setChecked(newChecked);
    };
    //选择所有
    const handleAllChanged = (isChecked) => {
        const newChecked = [];
        if (isChecked) {
            tableData.forEach(element => newChecked.push(element));
        }
        setChecked(newChecked);
    };
    return (
        <Card plain>
            <CardBody plain>
                <div className={classes.tableContainer}
                     style={{height: `calc(100vh - ${tableHeight}px - 75px)`}}> {/*75为表格的，翻页条的高度以及部分padding,margin*/}
                    <Table className={classes.table} stickyHeader aria-label="sticky table">
                        {tableHead !== undefined ? (
                            <TableHead>
                                <TableRow className={classes.tableHeadRow}>
                                    <PreviousColumn Sequence={Sequence} Selections={Selections} index={0}
                                                    checked={checked}
                                                    keyValue={tableData != null ? tableData.length : 123} subject={true}
                                                    value={checked.length}
                                                    handleChanged={handleAllChanged}/>
                                    {tableHead.map(column => (
                                        <StyledTableCell className={classes.tableCell + ' ' + classes.tableHeadCell}
                                                         key={column.id}
                                                         align={column.align}
                                                         style={{minWidth: column.minWidth, width: column.width}}
                                        >
                                            {column.label}
                                        </StyledTableCell>
                                    ))}
                                    {actions !== undefined ? (
                                        <StyledTableCell className={classes.tableCell + ' ' + classes.tableHeadCell}
                                                         key={'000'}
                                                         align={'center'}
                                                         style={{minWidth: 60}}
                                        > 操作</StyledTableCell>
                                    ) : null}
                                </TableRow>
                            </TableHead>
                        ) : null}
                        <TableBody>
                            {(page.rowsPerPage > 0 && !physicalPageHandle
                                ? tableData.slice(page.curPage * page.rowsPerPage, page.curPage * page.rowsPerPage + page.rowsPerPage)
                                : tableData).map((row, index) => {
                                return (
                                    <StyledTableRow hover role="checkbox" tabIndex={-1} key={row[primaryKey]}
                                                    className={classes.tableBodyRow}>
                                        <PreviousColumn Sequence={Sequence} Selections={Selections} checked={checked}
                                                        handleChanged={handleChanged} index={index}
                                                        keyValue={index} value={row}/>
                                        {
                                            tableHead.map(column => {
                                                const value = row[column.id];
                                                const dataAlign = column.dataAlign ? column.dataAlign : 'center';

                                                return (
                                                    <TableCell key={column.id} align={dataAlign}
                                                               className={classes.tableCell}>
                                                        {column.format ? column.format(value) : value}
                                                    </TableCell>
                                                );
                                            })

                                        }
                                        <OperatorColumn actions={actions} rowData={row} index={index}/>
                                    </StyledTableRow>
                                );
                            })}

                        </TableBody>
                    </Table>
                </div>
            </CardBody>
            <CardFooter>
                <Table>
                    <TableFooter className={classes[tableHeaderColor + 'TableHeader']}>
                        <TableRow className={classes.tableHeadRow}>
                            <TablePagination classes={{toolbar: classes.tableTurnPage}}
                                             rowsPerPageOptions={[10, 20, 50, {label: '所有行', value: -1}]}
                                             colSpan={6}
                                             count={page.totalRows ? page.totalRows : 0}
                                             rowsPerPage={pageInfo.pageSize && pageInfo.pageSize != page.totalRows ? pageInfo.pageSize : -1}
                                             labelRowsPerPage={'每页'}
                                             labelDisplayedRows={({from, to, count}) => {
                                                 let totalPage = page.totalRows > 0 ? Math.ceil(page.totalRows / page.pageSize) : 0;
                                                 return (
                                                     <span>第{page.curPage + 1}页，共{totalPage}页</span>
                                                 );
                                             }}
                                             page={page.totalRows > 0 ? page.curPage : 0}
                                             SelectProps={{
                                                 inputProps: {'aria-label': '每页'},
                                                 native: false,
                                             }}
                                             onChangePage={handlePageChange}
                                             onChangeRowsPerPage={handleRowsPerPageChange}
                                             ActionsComponent={TablePaginationActions}
                            />
                        </TableRow>
                    </TableFooter>
                </Table>
            </CardFooter>
        </Card>
    );
}

CustomPaginationTable.defaultProps = {
    tableHeaderColor: 'gray',
    physicalPageHandle: false,
};

CustomPaginationTable.propTypes = {
    tableHeaderColor: PropTypes.oneOf([
        'warning',
        'primary',
        'danger',
        'success',
        'info',
        'rose',
        'gray',
    ]),
    physicalPageHandle: PropTypes.func,
    tableHead: PropTypes.arrayOf(PropTypes.object),
    tableData: PropTypes.arrayOf(PropTypes.object),
    page: PropTypes.object,
};

/**分页工具条*/
function TablePaginationActions(props) {
    const defaultStartPage = 0;
    const classes = useStyles1();
    const {count, page, rowsPerPage, onChangePage} = props;
    const handleFirstPageButtonClick = event => {
        onChangePage(event, defaultStartPage);
    };

    const handleBackButtonClick = event => {
        onChangePage(event, page - 1);
    };

    const handleNextButtonClick = event => {
        let tmpPage = page + 1;
        if (tmpPage > (count / rowsPerPage - 1)) {
            tmpPage = Math.ceil(count / rowsPerPage) - 1;
        }
        onChangePage(event, tmpPage);
    };

    const handleLastPageButtonClick = event => {
        onChangePage(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
    };

    return (
        <div className={classes.root}>
            <IconButton className={classes.pageButton}
                        onClick={handleFirstPageButtonClick}
                        disabled={page === defaultStartPage}
                        aria-label="first page"
            >
                {<FirstPageIcon/>}
            </IconButton>
            <IconButton className={classes.pageButton} onClick={handleBackButtonClick}
                        disabled={page === defaultStartPage}
                        aria-label="previous page">
                {<KeyboardArrowLeft/>}
            </IconButton>
            <IconButton className={classes.pageButton}
                        onClick={handleNextButtonClick}
                        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                        aria-label="next page"
            >
                {<KeyboardArrowRight/>}
            </IconButton>
            <IconButton className={classes.pageButton}
                        onClick={handleLastPageButtonClick}
                        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                        aria-label="last page"
            >
                {<LastPageIcon/>}
            </IconButton>
        </div>
    );
}

TablePaginationActions.propTypes = {
    count: PropTypes.number.isRequired,
    onChangePage: PropTypes.func.isRequired,
    page: PropTypes.number.isRequired,
    rowsPerPage: PropTypes.number.isRequired,
};

/* checkbox 与 序号 */

const CustomCheckbox = withStyles(theme => ({
    root: {
        padding: 1,
    },
}))(Checkbox);

const CustomFormControlLabel = withStyles((theme) => {
    return ({
        root: {
            marginLeft: 1,
            marginRight: 1,
        },
        label: {
            color: 'black',
        },
    });
})(FormControlLabel);


/**
 * {
    key:'del',
        icon: 'delete',
    tooltip: '删除',
    onClick: (rowData) => {
    console.log(rowData);
    },
},
 * 按钮结构
 * @param props
 * @returns {*}
 * @constructor
 */

const useStyles_progress = makeStyles(theme => ({
    root: {
        display: 'flex',
        alignItems: 'center',
    },
    wrapper: {
        marginLeft:1,
        marginRight:1,
        marginTop:0,
        marginBottom:0,
        position: 'relative',
    },
    fabProgress: {
        color: "green",
        position: 'absolute',
        top: 2,
        left: 2,
        zIndex: 1,
    },
    fabButton:{
      width:26,
      height:26,
        minHeight:28,
        boxShadow:"none",
        backgroundColor:"transparent",
        "&:hover": {
            backgroundColor: "transparent",
            boxShadow: "none"
        }
    },
}));
function OperatorColumn(props) {
    const classes = useStyles();
    const curStyle = useStyles_progress();
    const {actions, rowData, index} = props;

    return (actions ? (<TableCell key="opeartor" align="center"
                                  className={classes.tableCell}>
        <div className={curStyle.root}>
            {actions.map(action => {
                let isLoad = action.actionSuccess[action.key+rowData[action.id]];
                const tmpClick = () => {
                    action.onClick(rowData);
                };
                return (
                    <div className={curStyle.wrapper }  key={action.key}>
                        <Fab
                            aria-label="save"
                            color="primary"
                            size="small" onClick={tmpClick} key={action.key}
                            classes={{root:curStyle.fabButton}}
                        >
                        <Tooltip title={action.tooltip}>
                            <Icon  color="primary">{action.icon}</Icon>
                        </Tooltip>
                        </Fab>
                        {isLoad && <CircularProgress size={23} className={curStyle.fabProgress} />}
                    </div>);
            })}</div>
        </TableCell>
    ) : (<TableCell><span/></TableCell>));
}

function PreviousColumn(props) {
    const {Sequence, Selections, index, keyValue, subject, value, handleChanged, checked} = props;

    const classes = useStyles();
    const showColumn = Sequence || Selections;
    let label = index + 1;
    if (subject) {
        label = '序号';
    }

    let width = 40;
    label = Sequence ? label : ''; //不显示顺序号
    if (Sequence && Selections) {
        width = 70;
    }//两者都显示
    else if (Selections) {
        width = 25;
    }  //
    return (
        showColumn ? (<StyledTableCell className={classes.tableCell + ' ' + classes.tableHeadCell}
                                       key={'000'}
                                       align={'center'}
                                       style={{minWidth: width, width: width}}
        >
            {Selections ? (<CustomFormControlLabel
                value={keyValue}
                control={
                    subject ?
                        <CustomCheckbox checked={checked.length === keyValue && checked.length !== 0}
                                        onChange={event => {
                                            let isChecked = event.target.checked;
                                            handleChanged(isChecked, value);
                                        }} indeterminate={checked.length !== keyValue && checked.length > 0}
                                        color="primary"/> :
                        <CustomCheckbox checked={checked.indexOf(value) !== -1} onChange={event => {
                            let isChecked = event.target.checked;
                            handleChanged(isChecked, value);
                        }} color="primary"/>}
                label={label}
                classes={Sequence ? {label: classes.checkboxLabel} : {}}
                labelPlacement="end"
            />) : (<span>{label}</span>)}

        </StyledTableCell>) : (<td><span/></td>)
    );
}

PreviousColumn.defaultProps = {
    Sequence: true,
    Selections: false,
    subject: false,
    checked: [],
};

PreviousColumn.propTypes = {
    Sequence: PropTypes.bool.isRequired,
    Selections: PropTypes.bool.isRequired,
    subject: PropTypes.bool.isRequired,
    index: PropTypes.number,
    checked: PropTypes.arrayOf(PropTypes.object),
    keyValue: PropTypes.number,
    handleChanged: PropTypes.func,
};


import React from 'react';

// @material-ui/core components
import {makeStyles} from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';
import Icon from '@material-ui/core/Icon';

// core components
import GridItem from 'components/Grid/GridItem.js';
import GridContainer from 'components/Grid/GridContainer.js';
import MaterialTable, {MTableToolbar} from 'material-table';

import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as crawler from './crawlerAction';

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


const styles = {

    cardCategoryWhite: {
        '&,& a,& a:hover,& a:focus': {
            color: 'rgba(255,255,255,.62)',
            margin: '0',
            fontSize: '14px',
            marginTop: '0',
            marginBottom: '0',
        },
        '& a,& a:hover,& a:focus': {
            color: '#FFFFFF',
        },
    },
    cardTitleWhite: {
        color: '#FFFFFF',
        marginTop: '0px',
        minHeight: 'auto',
        fontWeight: '300',
        fontFamily: '\'Roboto\', \'Helvetica\', \'Arial\', sans-serif',
        marginBottom: '3px',
        textDecoration: 'none',
        '& small': {
            color: '#777',
            fontSize: '65%',
            fontWeight: '400',
            lineHeight: '1',
        },
    },

};

const useStyles = makeStyles(styles);

function CrawlerPage(props) {

    const classes = useStyles();
    const crawlerList = React.createRef();
    const [selectedRow, setSelectedRow] = React.useState();
    let crawlerActions = props.crawlerActions;
    let columns = props.crawlerPages.columnTitles;
    let records = props.crawlerPages.datas;
    React.useEffect(() => {
        crawlerActions.retrieveCrawler();
    }, []); //空数据，useEffect只执行一次

    return (
        <GridContainer>
            <GridItem xs={12} sm={12} md={12}>

                <MaterialTable size="small" aria-label="a dense table"
                               title="爬虫数据："
                               columns={columns}
                               data={records.map((prop, i) => {
                                   let tmpTime = new Date(prop.crawlerStartTime);
                                   let formatDate = tmpTime.toLocaleString('en-GB', {timeZone: 'UTC'});
                                   //formatDate = tmpTime.toISOString();
                                   return Object.assign({}, prop, {'No.': i + 1, 'crawlerStartTime': formatDate});
                               })}

                               onRowClick={((evt, selectedRow) => {
                                       setSelectedRow(selectedRow);
                                   }
                               )}
                               options={{
                                   actionsColumnIndex: 999,
                                   rowStyle: rowData => {
                                       return ({
                                           backgroundColor: selectedRow && selectedRow.tableData.id === rowData.tableData.id ? '#aaa' : '#FFF',
                                       });
                                   },
                                   cellStyle: {
                                       color: '#000000',
                                   },
                                   search: false,
                                   padding: 'dense',
                                   minBodyHeight: 440,
                                   maxBodyHeight: 600,
                                   //selection: true,
                               }}
                               actions={[
                                   {
                                       icon: 'delete',
                                       tooltip: '删除',
                                       onClick: (event, rowData) => {
                                           crawlerActions.delCrawlerConfig(rowData);
                                       },
                                   },
                                   {
                                       icon: 'add',
                                       tooltip: '增加',
                                       isFreeAction: true,
                                       onClick: (event, rowData) => {
                                           crawlerActions.addCrawlerConfig('tests');
                                       },
                                   },
                                   {
                                       icon: 'delete',
                                       tooltip: '批量删除',
                                       isFreeAction: true,
                                       onClick: (event, rowData) => {
                                           crawlerActions.delCrawlerConfig(rowData);
                                       },
                                   },
                                   {
                                       icon: 'refresh',
                                       tooltip: '刷新数据',
                                       isFreeAction: true,
                                       onClick: (event, rowData) => {
                                           crawlerActions.retrieveCrawler();
                                       },
                                   },
                               ]}

                               localization={{
                                   pagination: {
                                       labelDisplayedRows: '{from}-{to} 共{count}条',
                                       firstTooltip: '第一页',
                                       previousTooltip: '前一页',
                                       nextTooltip: '下一页',
                                       lastTooltip: '末一页'
                                   },
                                   body: {
                                       emptyDataSourceMessage: '暂无数据....'
                                   }
                               }}
                               components={{
                                   Toolbar: props => {
                                       return (
                                           <Box style={{backgroundColor: '#e8eaf5', height: "50px"}} ml={2}>
                                               {props.actions.filter(prop => prop.position === "toolbar").map((prop, key) => {
                                                   return (
                                                       <Button style={{marginLeft: 10}}
                                                               variant="contained"
                                                               color="primary"
                                                               startIcon={<Icon>{prop.icon}</Icon>}
                                                               key={key}
                                                               onClick={(event, args) => {
                                                                   prop.onClick();
                                                               }}
                                                       >
                                                           {prop.tooltip}
                                                       </Button>
                                                   );
                                               })}
                                           </Box>
                                       )
                                   }
                               }}

                />

            </GridItem>

        </GridContainer>
    );
}

const mapStateToProps = (state) => {
    return {
        crawlerPages: state.crawler,
    };
};

const mapDispatchToProps = dispatch => {

    const crawlerActions = bindActionCreators(crawler, dispatch);
    return {
        crawlerActions,
        dispatch,
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(CrawlerPage);

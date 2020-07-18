import {actionType} from './webPageAction'
import React from 'react';
import LinesEllipsis from 'react-lines-ellipsis'
import responsiveHOC from 'react-lines-ellipsis/lib/responsiveHOC'
import Switch from '@material-ui/core/Switch';
import Chip from '@material-ui/core/Chip';
import Box from '@material-ui/core/Box';


const ResponsiveEllipsis = responsiveHOC()(LinesEllipsis)

/**
 * 数据初始化状态，
 *
 * @type {{loading: boolean, 是否加载完成，
 *  dataRecords: Array, 加载的数据
 *  columnTitles: *[] 数据列标题
 *  }}
 */
const handleReflow = (rleState) => {
    //console.log(rleState);
}

const initialDataState = {
    loading: false,
    code: 0,
    message: '',
    dataRecords: [],
    pageInfo: {
        //totalRows:1111,
        curPage: 1, //当前页码
        // lastPage:11111, //总页数、最后一页面页码
        pageRows: 10, //每页行数
    },

    columnTitles: [
        {
            id: 'crawlerUrl', label: 'URL地址', minWidth: 120, width: 350,
            align: 'center',
            dataAlign: "left",
            format: value => {
                return (<LinesEllipsis text={value} maxLine={1} winWidth={200}
                                       ellipsis='...' trimRight={true} basedOn='letters' onReflow={handleReflow}/>)
            }
        },
        {
            id: 'crawlerStartTime',
            label: '时间',
            minWidth: 50,
            width: 130,
            align: 'center',
            dataAlign: 'left',
            format: value => {
                let time = new Date(value);
                return time.toLocaleDateString("cn", {
                    year: 'numeric',
                    month: 'numeric',
                    day: 'numeric',
                    hour: 'numeric',
                    minute: 'numeric',
                    second: 'numeric',
                    hour12: false
                })
            }
        },
        {
            id: 'urlContent',
            label: '内容',
            align: 'center',
            dataAlign: 'left',
            format: value => {
                return (<div style={{width: 500, whiteSpace: 'nowrap'}}><Box
                    component="div" style={{marginTop: 2, marginBottom: 2}}
                    my={2}
                    textOverflow="ellipsis"
                    overflow="hidden"
                >
                    {value}
                </Box></div>)
            }
        },
        {
            id: 'isFinished',
            label: '状态',
            minWidth: 60,
            width: 60,
            align: 'center',
            dataAlign: 'center',
            format: value => value === '2' ? '已处理' : value === '1' ? '未处理' : '抓取错误',
        }
    ]
};

/**
 * 业务处理之前的状态的处理
 * @param state
 * @param action
 * @returns {*}
 */
const webPageReducers = (state = initialDataState, action) => {
    switch (action.type) {
        case actionType.DEL_PAGE_RECORD:
            let tmpid = action.params.otherParams.ids;
            let single = action.params.otherParams.single;
            let tmpLoad11 = state.actionLoading;
            if (single)
                tmpLoad11['del_' + tmpid] = true;
            return Object.assign({}, state, {actionLoading: tmpLoad11, handleState: false, handleNumber: 0});
        case actionType.GET_PAGE_RECORD:
            return state;
        case actionType.PAGE_HANDLE: {
            let tmpid = action.params.otherParams.ids;
            let single = action.params.otherParams.single;
            let tmpLoad11 = state.actionLoading;

            if (single)
                tmpLoad11['handle_' + tmpid] = true;

            let tmpHandleState = false;
            let handleNumber = 0;
            // let handleTotalNumber=action.params.otherParams.ids.length; //记录总数量
            return Object.assign({}, state, {
                actionLoading: tmpLoad11,
                handleState: tmpHandleState,
                handleNumber: 0,
                code: 0,
                message: "",
            });
        }

        case actionType.RETRIEVE_DATA:

            return Object.assign({}, state, {loading: false});
        case actionType.REFRESH_PAGE: {
            let dataList = action.dataResult;
            if (dataList == null) {
                return Object.assign({}, state, {loading: true});
            }

            let actionLoading = {};
            dataList.map(rowdata => {
                actionLoading["craw_" + rowdata.logId] = false;
                actionLoading["del_" + rowdata.logId] = false;
                actionLoading["handle_" + rowdata.logId] = false;
            })
            let tmpPage = action.pageInfo;
            return Object.assign({}, state, {
                actionLoading: actionLoading,
                loading: true,
                code: action.code,
                dataRecords: dataList,
                pageInfo: tmpPage
            });
        }
        case actionType.PAGE_RESPONSE: {
            let tmpResult = action.result;
            let tmpLoad = state.actionLoading;
            tmpLoad[tmpResult.id] = false;
            return Object.assign({}, state, {
                actionLoading: tmpLoad,
                code: tmpResult.code,
                message: tmpResult.message,
                errorInfo: tmpResult.errorInfo
            });
        }
        case actionType.RESTART_CRAWLER_PAGE:
            let id = action.params.otherParams.id;
            let tmpLoad = state.actionLoading;
            tmpLoad['craw_' + id] = true;
            return Object.assign({}, state, {actionLoading: tmpLoad, message: ""});
        default:
            return state;
    }
};

export default webPageReducers;

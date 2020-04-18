import React from 'react';
import {takeEvery, put, take, call, fork } from 'redux-saga/effects'

import {request,jsonRequest,requestGet,requestPost} from 'utils/request';

const actionType={
    CREATE_TASK:'createTask', //创建一个任务
    DEL_TASK:'deleteTask', //
    HANDLE_TASK_ERROR_RECORD:'handleTaskErrorRecord', //
    START_TASK:'startTask', //开始任务
    STOP_TASK:'stopTask', //停止任务
    ANALYSIS_TASK:'analysisTaskPage', //处理任务的所有抓取页面
    REFRESH_PAGE:'refreshTaskPage', //刷新页面
    RETRIEVE_DATA:'retrieveTask', //检索数据
    TASK_PAGE_RESPONSE:'taskPageResponse',//页面响应
    SHOW_EDIT_DIALOG:'showEditDialog',//显示编辑窗口
    CLOSE_EDIT_DIALOG:'closeEditDialog',
    PAGE_INTERVAL_REFRESH:"pageIntervalRefreshTaskPage",
    HANDLE_WEI_XIN:'handleTaskToWeiXin'
}

const initDataTaskState = {
    isLoading:false,
    code:0,
    message:'',
    dataRecords: [],
    dialogStatus:false,
    pageInfo:{
        curPage:1, //当前页码
        pageRows:10, //每页行数
    },

}

/**
 * reduces方法，处理状态的变换
 * @param state
 * @param action
 * return  state
 */
const taskReducers = (state=initDataTaskState,action)=>{
    switch (action.type) {
        case actionType.CREATE_TASK:
            return state;
        case actionType.START_TASK:
            return state;
        case actionType.STOP_TASK:
            return state;
        case actionType.ANALYSIS_TASK:
            return state;
        case actionType.SHOW_EDIT_DIALOG: //显示弹出窗口，更改页面窗口状态
            let task =action.task;
            return Object.assign({},state,{dialogStatus:true,dataInfo:task})
        case actionType.CLOSE_EDIT_DIALOG: //显示弹出窗口，更改页面窗口状态
            return Object.assign({},state,{dialogStatus:false})
        case actionType.REFRESH_PAGE:
            let dataList = action.dataResult;
            if (dataList==null){
                return Object.assign({}, state, {loading:true});
            }
            let tmpPage = action.pageInfo;
            return Object.assign({}, state, {message:action.message,loading:true,dataRecords: dataList,pageInfo:tmpPage});
        case actionType.RETRIEVE_DATA: {
            return state;// Object.assign({}, state, {loading: false});
        }
        case actionType.TASK_PAGE_RESPONSE:{
            let tmpResult = action.result;
            let tmpRecords = state.dataRecords;
            const isEqual = (element) => element.taskId ==tmpResult.task.taskId;

            if (tmpResult && tmpResult !=undefined &&  tmpResult!=null){
                let tmpIndex = -1;
                tmpIndex = state.dataRecords.findIndex(isEqual);
                if ( tmpIndex >= 0 && tmpResult.operatorType=="U"){ //更新操作
                    tmpRecords.splice(tmpIndex,1,tmpResult.task);
                }
                if  (tmpIndex >= 0 && tmpResult.operatorType == "D") //更新或增加操作
                    tmpRecords.splice(tmpIndex,1);

                if (tmpIndex < 0 && tmpResult.operatorType=="U")
                    tmpRecords.push(tmpResult.task);
            }
            return Object.assign({}, state, {dialogStatus:false,dataRecords:tmpRecords,task:tmpResult.task,code:tmpResult.code ,message:tmpResult.message,errorInfo:tmpResult.errorInfo});
        }
        default:
            return state;
    }
}

export default taskReducers;

/**
 * action部分
 */

/**
 * 显示编辑窗口
 * @param task 可以是一个ID，从后台取数，也可以是一个任务
 */
export function showEditDialog(task){
    return {
        type:actionType.SHOW_EDIT_DIALOG,
        task
    }
}

/**
 * 关闭编辑窗口
 * @param task 可以是一个ID，从后台取数，也可以是一个任务
 */
export function closeEditDialog(){

    return {
        type:actionType.CLOSE_EDIT_DIALOG
    }
}

/**
 * 创建一个抓取任务
 * @returns {{type: string}}
 */
export function createTask(task){
   // console.log("----------action create Task--------------")

    return {
        type:actionType.CREATE_TASK,
        params:{
            customModel:task,
        }
    }
}

/**
 *  无刷新页面响应
 * @param responseResult  响应结果数据，后端返回结果，错误代码，错误文字，错误信息
 * @returns {{type: string, dataList: *}}
 */
export function pageResponse(responseResult) {
    let tmpTask = responseResult.resultData?responseResult.resultData:null;
    return {
        type: actionType.TASK_PAGE_RESPONSE,
        result:{
            code:responseResult.code,
            message:responseResult.message,
            errorInfo:responseResult.errorInfo,
            task:tmpTask,
            operatorType:responseResult.operatorType,
        }

    };
}

/**
 * 根据任务ID,开始任务抓取，
 */
export function startTask(taskId) {

    return {
        type:actionType.START_TASK,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}

/**
 * 根据任务ID,开始任务抓取，
 */
export function deleteTask(taskId) {

    return {
        type:actionType.DEL_TASK,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}

/**
 * 根据任务ID,处理错误记录
 */
export function handleErrorRecord(taskId) {

    return {
        type:actionType.HANDLE_TASK_ERROR_RECORD,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}
/**
 * 根据任务ID，停止抓取任务
 */
export function stopTask(taskId) {
    return {
        type: actionType.STOP_TASK,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}

/**
 * 任务网站分析，对此次抓取网页进行分析
 */

export function analysisWebPage(taskId) {
    return {
        type:actionType.ANALYSIS_TASK,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}

/**
 * 任务网站分析，对此次抓取网页进行分析
 */

export function handleWeiInfo(taskId) {
    return {
        type:actionType.HANDLE_WEI_XIN,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}



/**
 * 刷新页面，根据已有数据，触发reduces
 * @param dataList
 * @returns {{type: string, dataList: *}}
 */
export  function refreshPage(responseResult) {

    let resultData = responseResult.resultData;
    return {
        type: actionType.REFRESH_PAGE,
        dataResult:resultData.pageDatas,
        code:responseResult.code,
        message:responseResult.message,
        pageInfo:{
            totalRows:resultData.totalSize,
            curPage:resultData.curPage, //当前页码
            lastPage:resultData.lastPage, //总页数、最后一页面页码
            pageRows:resultData.pageSize, //每页行数

        }
    };

}

/**
 * 检索数据
 * @param retrieveArgs
 * @returns {{retrieveArgs: *, type: string}}
 */
export function retrieveData(retrieveArgs){
    return {
        type:actionType.RETRIEVE_DATA,
        retrieveArgs
    }
}

export function pageIntervalRefresh(taskId){
    return {
        type:actionType.PAGE_INTERVAL_REFRESH,
        params:{
            otherParams:{
                taskId
            },
        }
    }
}


//以下sagas内容
export function* watchTask() {
    yield takeEvery(actionType.RETRIEVE_DATA,getData);
    yield takeEvery(actionType.ANALYSIS_TASK,analysisData);
    yield takeEvery(actionType.HANDLE_TASK_ERROR_RECORD,handleErrorData);
    yield takeEvery(actionType.CREATE_TASK,createTaskControl);
    yield takeEvery(actionType.STOP_TASK,stopTaskControl);
    yield takeEvery(actionType.START_TASK,startTaskControl);
    yield takeEvery(actionType.DEL_TASK,delTaskData);
    yield takeEvery(actionType.PAGE_INTERVAL_REFRESH,getTaskStateByTaskId);
    yield takeEvery(actionType.HANDLE_WEI_XIN,weiXinController);
}

function* getTaskStateByTaskId(action){
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/getTaskState', 'post',args);
    if (responseResult.code=="0"){
        console.log("尚未完成..................");
        return ;
    }
    yield put(pageResponse(responseResult));
}

function* analysisData(action){
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/analysisTask', 'post',args);
    yield put(pageResponse(responseResult));
}

function* createTaskControl(action){
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/addCrawlerTask', 'post',args);
    yield put(pageResponse(responseResult));
}

function* startTaskControl(action){
    console.log("startTask"+action.taskId);
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/startTask', 'post',args);
    yield put(pageResponse(responseResult));
}

function* stopTaskControl(action){
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/stopTask', 'post',args);
    yield put(pageResponse(responseResult));
}
function* delTaskData(action){
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/deleteTask', 'post',args);
    yield put(pageResponse(responseResult));
}
function* handleErrorData(action){
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/errorHandle', 'post',args);
    yield put(pageResponse(responseResult));
}

function* getData(action){
    let args = action.retrieveArgs;
    const responseResult = yield call(jsonRequest, 'task/getTaskRecord', 'post',args);

    yield put(refreshPage(responseResult));

}
function* weiXinController(action){
    let  appid ="wxed2ebd39d7b4ba7e";
    let  secret= "74f84a06dc574b91a7162753e226ec6c";
    let  getTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
    const responseResult = yield call(requestGet, getTokenUrl,{});
    console.log(responseResult)
    let token = responseResult.access_token;
    let testUrl = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+token;
    const materials = yield call(requestPost, testUrl,{
        "type":'news',
        "offset":0,
        "count":10
    });
    console.log(materials)

}



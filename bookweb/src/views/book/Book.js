import React from 'react';
import {takeEvery, put, call} from 'redux-saga/effects';
import config from '../../utils/wx_config';
import wxChat from '../../utils/wechat';
import {jsonRequest, WX_DEVELOP, requestPost, requestGet} from 'utils/request';

const actionType = {
    DEL_BOOK: 'deleteBook', //
    RECAPTURE_BOOK: 'recaptureBook', //重新取得书籍
    CAPTURE_NEW_CHAPTER: 'captureNewChapter', //抓取新章
    SHARE_BOOK: 'shareBook', //发布书籍到公众号
    REFRESH_PAGE: 'refreshBookPage', //刷新页面
    RETRIEVE_DATA: 'retrieveBook', //检索数据
    BOOK_PAGE_RESPONSE: 'bookPageResponse',//页面响应
    SHOW_EDIT_DIALOG: 'showEditDialog',//显示编辑窗口
    CLOSE_EDIT_DIALOG: 'closeEditDialog',
};

const initBookState = {
    isLoading: false,
    code: 0,
    message: '',
    dataRecords: [],
    dialogStatus: false,
    pageInfo: {
        curPage: 1, //当前页码
        pageRows: 10, //每页行数
    },
    wxChat: new wxChat(config.wechat)

};

/**
 * reduces方法，处理状态的变换
 * @param state
 * @param action
 * return  state
 */
const bookReducers = (state = initBookState, action) => {
    switch (action.type) {
        case actionType.BOOK_PAGE_RESPONSE:
            return state;
        case actionType.DEL_BOOK:
            return state;
        case actionType.RECAPTURE_BOOK:
            return state;
        case actionType.SHARE_BOOK:
            // let tt = state.wxChat.getAccessToken();
            // console.log(tt);
            // state.wxChat = tt;
            return state;
        case actionType.CAPTURE_NEW_CHAPTER:
            return state;
        case actionType.SHOW_EDIT_DIALOG: //显示弹出窗口，更改页面窗口状态
            return state;
        case actionType.CLOSE_EDIT_DIALOG:
            return state;
        case actionType.REFRESH_PAGE:
            let dataList = action.dataResult;
            if (dataList == null) {
                return Object.assign({}, state, {loading: true});
            }
            let tmpPage = action.pageInfo;
            return Object.assign({}, state, {
                message: action.message,
                loading: true,
                dataRecords: dataList,
                pageInfo: tmpPage
            });

        case actionType.RETRIEVE_DATA: {
            // state.wxChat.getAccessToken();
            return state;// Object.assign({}, state, {loading: false});
        }
        default:

            return state;
    }
};

export default bookReducers;

/**
 * action部分
 * action命名：方法描述 +Action
 */

/**
 * 显示编辑窗口
 * @param book 可以是一个ID，从后台取数，也可以是一个任务
 */
export function showEditDialogAction(book) {
    return {
        type: actionType.SHOW_EDIT_DIALOG,
        book
    }
}

/**
 * 关闭编辑窗口
 *
 */
export function closeEditDialogAction() {

    return {
        type: actionType.CLOSE_EDIT_DIALOG
    }
}

/**
 * 抓取书籍最新章节
 * @returns {{type: string}}
 */
export function captureNewChapterAction(book) {
    return {
        type: actionType.CAPTURE_NEW_CHAPTER,
        params: {
            customModel: book,
        }
    }
}

/**
 *  无刷新页面响应
 * @param responseResult  响应结果数据，后端返回结果，错误代码，错误文字，错误信息
 * @returns {{type: string, dataList: *}}
 */
export function pageResponseAction(responseResult) {
    let tmpBOOK = responseResult.resultData ? responseResult.resultData : null;
    return {
        type: actionType.BOOK_PAGE_RESPONSE,
        result: {
            code: responseResult.code,
            message: responseResult.message,
            errorInfo: responseResult.errorInfo,
            BOOK: tmpBOOK,
            operatorType: responseResult.operatorType,
        }

    };
}

/**
 * 重新抓取当前书籍
 */
export function recaptureBOOKAction(bookId) {

    return {
        type: actionType.RECAPTURE_BOOK,
        params: {
            otherParams: {
                bookId
            },
        }
    }
}

/**
 * 删除书籍
 */
export function deleteBookAction(bookId) {

    return {
        type: actionType.DEL_BOOK,
        params: {
            otherParams: {
                bookId
            },
        }
    }
}

/**
 * 根据任务ID,处理错误记录
 */
export function shareBookToWeiXinAction(bookId, wxchat) {

    return {
        type: actionType.SHARE_BOOK,
        params: {
            otherParams: {
                bookId,
                wxChat: wxchat
            },
        }
    }
}

/**
 * 刷新页面，根据已有数据，触发reduces
 *
 * @returns {{type: string, dataList: *}}
 */
export function refreshPageAction(responseResult) {

    let resultData = responseResult.resultData;
    return {
        type: actionType.REFRESH_PAGE,
        dataResult: resultData.pageDatas,
        code: responseResult.code,
        message: responseResult.message,
        pageInfo: {
            totalRows: resultData.totalSize,
            curPage: resultData.curPage, //当前页码
            lastPage: resultData.lastPage, //总页数、最后一页面页码
            pageRows: resultData.pageSize, //每页行数

        }
    };

}

/**
 * 检索数据
 * @param retrieveArgs
 * @returns {{retrieveArgs: *, type: string}}
 */
export function retrieveDataAction(retrieveArgs) {
    return {
        type: actionType.RETRIEVE_DATA,
        retrieveArgs
    }
}

export function pageIntervalRefreshAction(BOOKId) {
    return {
        type: actionType.PAGE_INTERVAL_REFRESH,
        params: {
            otherParams: {
                BOOKId
            },
        }
    }
}


//以下sagas内容
//sagas方法命名：方法描述
export function* watchBOOK() {
    yield takeEvery(actionType.RETRIEVE_DATA, getBookData);
    yield takeEvery(actionType.SHARE_BOOK, shareBookToWeiXin);
    yield takeEvery(actionType.CAPTURE_NEW_CHAPTER, captureNewChapter);
    yield takeEvery(actionType.RECAPTURE_BOOK, recaptureBook);
    yield takeEvery(actionType.DEL_BOOK, delBook);
}

function* getBookData(action) {
    let args = action.retrieveArgs;
    const responseResult = yield call(jsonRequest, 'book/books', 'post', args);
    yield put(refreshPageAction(responseResult));
}

function* shareBookToWeiXin(action) {
    // let args = action.params;
    // const responseResult = yield call(jsonRequest, 'book/shareBook', 'post', args);
    // yield put(pageResponseAction(responseResult));
    //config.wechat.getAccessToken();
    //console.log(action);
    let wxApi = action.params.otherParams.wxChat;
    let bookFile = action.params.otherParams.bookId;
    // let getTokenUrl = WX_DEVELOP.appTokenUrl;
    // const responseResult = yield call(requestGet, getTokenUrl, {});
    // let token = responseResult.access_token;
    //
    // let testUrl = WX_DEVELOP.addNews + token;
    //console.log(wxApi);
    // wxApi.getBatchMaterial('image').then(function(data){
    //     console.log(data);
    // });
    console.log(wxApi);
    // console.log(bookFile);
    //wxApi.uploadPermMaterial('pic',"/public/logo192.png")


    wxApi.uploadPermMaterialTest('other', bookFile);
    // let testurl1 = WX_DEVELOP.addMaterial+token+"&type=image";
    // const mat = yield call(requestPost,testurl1,{
    //
    // });
    // console.log(mat);
    // const materials = yield call(requestPost, testUrl, {
    //     "articles": [{
    //         "title": "test_addTitle",
    //         "thumb_media_id": "HrZ5Z9-SgitEYsVP_1VfgX5FqMI9Wg9anC0dmYnLRHM",
    //         "author": "llij",
    //         "digest": "DIGEST",
    //         "show_cover_pic": "0",
    //         "content": "imya a prograph content ",
    //         "content_source_url": "",
    //         "need_open_comment":0,
    //         "only_fans_can_comment":0
    //     },
    //         //若新增的是多图文素材，则此处应还有几段articles结构
    //     ]
    // });
    // console.log(materials)
}

function* captureNewChapter(action) {
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'book/captureNewChapterByBook', 'post', args);
    yield put(pageResponseAction(responseResult));
}

function* recaptureBook(action) {
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'book/recaptureBook', 'post', args);
    yield put((responseResult));
}

function* delBook(action) {
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'book/delBook', 'post', args);
    yield put(pageResponseAction(responseResult));
}



import {takeEvery, put, take, call, fork} from 'redux-saga/effects'

import {request, jsonRequest} from 'utils/request';
import {refreshPage, getPageRecord, pageResponse, actionType} from './webPageAction'

export function* watchWebPage() {
    yield takeEvery(actionType.RETRIEVE_DATA, getData);
    yield takeEvery(actionType.DEL_PAGE_RECORD, delData);
    yield takeEvery(actionType.RESTART_CRAWLER_PAGE, crawlerPage);
    yield takeEvery(actionType.PAGE_HANDLE, handlePage);
}

function* delData(action) {
    let delData = action.params;
    //let param = JSON.stringify(delData)
    const result = yield call(jsonRequest, 'task/delCrawlerRecord', 'post', delData);
    yield put(pageResponse(result));
}

function* getData(action) {
    let args = action.retrieveArgs;
    const responseResult = yield call(jsonRequest, 'task/getPageRecord', 'post', args);
    yield put(refreshPage(responseResult));
}

function* crawlerPage(action) {
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/restartUrl', 'post', args);
    yield put(pageResponse(responseResult));
}

function* handlePage(action) {
    let args = action.params;
    const responseResult = yield call(jsonRequest, 'task/dataHandler', 'post', args);
    yield put(pageResponse(responseResult));
}

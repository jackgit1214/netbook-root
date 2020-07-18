import {takeEvery, put, take, call, fork} from 'redux-saga/effects'

import {request} from '../../utils/request';
import {refreshPage, retrieveCrawler} from './crawlerAction'

export function* watchRequestTypeList() {

    //while ((true)){
    yield takeEvery("RETRIEVE_DATA", getData);
    yield takeEvery("DEL", delData);

    //}

    // while (true) {
    //     console.log("-----------------------1---------------------------")
    //     yield take("GET_DATA_BY_SAGAS");
    //     console.log("----------------------2---------------------------")
    //     yield fork(getData);
    // }
}

function* delData(action) {
    let delData = action.rowDatas;
    let param = JSON.stringify(delData)
    const result = yield call(request, 'fetch/delCrawlerRecord', 'post', param);
    yield put(retrieveCrawler());
}

function* getData() {
    const dataList = yield call(request, 'fetch/getPageRecord', 'post', '');
    yield put(refreshPage(dataList));
}

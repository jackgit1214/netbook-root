import {all, fork} from 'redux-saga/effects';

import {watchRequestTypeList} from '../views/crawler/crawlerSagas';
import {watchWebPage} from '../views/crawler/webpage/webPageSagas';
import {watchTask} from '../views/crawler/task/CrawlerTask';
import {watchBOOK} from '../views/book/Book';

export default function* rootSaga() {
    //yield all([fork(watchRequestTypeList), fork(watchRequestArticleList)]);
    yield all([fork(watchRequestTypeList)]);
    yield all([fork(watchWebPage)]);
    yield all([fork(watchTask)]);
    yield all([fork(watchBOOK)]);
}

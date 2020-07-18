import {combineReducers} from 'redux';

import crawler from '../../views/crawler/crawlerReducers'
import task from '../../views/crawler/task/CrawlerTask'
import webPage from '../../views/crawler/webpage/webPageReducers'
import book from '../../views/book/Book'

const rootReducer = combineReducers({
    crawler: crawler,
    task: task,
    webPage: webPage,
    book: book
});

export default rootReducer;

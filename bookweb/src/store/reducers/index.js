import { combineReducers } from 'redux';

import crawler from '../../views/crawler/crawlerReducers'
import task from '../../views/crawler/task/CrawlerTask'
import webPage from '../../views/crawler/webpage/webPageReducers'
const rootReducer = combineReducers({
    crawler:crawler,
    task:task,
    webPage:webPage
});

export default rootReducer;

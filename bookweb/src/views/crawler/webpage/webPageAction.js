/**
 * 固定的ACTION类型
 * @type {{GET_PAGE_RECORD: string, PAGE_HANDLE: string, DEL_PAGE_RECORD: string, REFRESH_PAGE: string, RETRIEVE_DATA: string}}
 */
export  const actionType={
    GET_PAGE_RECORD:'getPageRecord',
    PAGE_HANDLE:'pageHandle',
    RESTART_CRAWLER_PAGE:'restartCrawlerPage',
    DEL_PAGE_RECORD:'deletePageRecord',
    REFRESH_PAGE:'refreshPage',
    PAGE_RESPONSE:'pageResponse',
    RETRIEVE_DATA:'retrieveData',
}


/**
 * 取得已抓取页面的记录
 * @param retrieveArgs 页面检索参数
 * @returns {{type: string, retrieveArgs: *}}
 */
export function getPageRecord(retrieveArgs) {
    return {
        type: actionType.RETRIEVE_DATA,
        retrieveArgs
    };
}

/**
 * 根据返回的内容刷新页面
 * @param dataList  返回的数据
 * @returns {{type: string, dataList: *}}
 */
export function refreshPage(responseResult) {

    let result = responseResult.resultData;
    return {
        type: actionType.REFRESH_PAGE,
        dataResult:result.pageDatas,
        code:responseResult.code,
        pageInfo:{
            totalRows:result.totalSize,
            curPage:result.curPage, //当前页码
            lastPage:result.lastPage, //总页数、最后一页面页码
            pageRows:result.pageSize, //每页行数
        }
    };
}

/**
 *  无刷新页面响应
 * @param responseResult  响应结果数据，后端返回结果，错误代码，错误文字，错误信息
 * @returns {{type: string, dataList: *}}
 */
export function pageResponse(responseResult) {
    return {
        type: actionType.PAGE_RESPONSE,
        result:{
            code:responseResult.code,
            message:responseResult.message,
            errorInfo:responseResult.errorInfo,
            id:responseResult.resultData
        }

    };
}

/**
 * 删除抓取页面记录
 * @param records 要删除的数据记录
 * @returns {{type: string, records: *}}
 */
export function delPageRecord(records) {
     return {
        type: actionType.DEL_PAGE_RECORD,
         params:records
    };
}

/**
 * 处理页面内容，将页面内容处理到指定表中
 * @param records 记录IDS数组
 * @returns {{type: string, records: *}}
 */
export function handlePageContent(records) {
    return {
        type: actionType.PAGE_HANDLE,
        params:records
    };
}

/**
 * 对指定URL重新抓取，只抓取一个页面
 * @param url
 * @returns {{type: string, url: *}}
 */
export function restartCrawlerPage(params) {
    return {
        type: actionType.RESTART_CRAWLER_PAGE,
        params
    };
}







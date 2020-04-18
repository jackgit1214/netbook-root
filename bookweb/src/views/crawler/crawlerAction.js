export function addCrawlerConfig(args) {
    //console.log("----------add-----Action----------------------")
    return {
        args,
        type: 'ADD'
    };
}
export function delCrawlerConfig(rowDatas) {
    //console.log(rowDatas);
     return {
        type: 'DEL',
         rowDatas
    };
}
export function saveCrawlerConfig() {

    console.log("----------save-----Action----------------------")
    return {
        type: 'SAVE'
    };
}
export function refreshPage(dataList) {
    return {
        type: 'REFRESHPAGE',//''types.RECEIVE_TYPE_LIST,
        dataList
    };

}
export function retrieveCrawler(retrieveArgsment) {
    return {
        type: 'RETRIEVE_DATA',//''types.RECEIVE_TYPE_LIST,
        retrieveArgsment
    };
}






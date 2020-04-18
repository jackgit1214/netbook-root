
const initialDataState = {
    loading: false,
    datas: [],
    columnTitles: [
        {title: '序号', field: 'No.',cellStyle: {
                backgroundColor: '#039be5',
                color: '#FFF',
                width:'70px'
            },
            headerStyle: {
                backgroundColor: '#039be5',
                width:'70px'
            }},
        {title: '网站URL', field: 'crawlerUrl'},
        {title: '开始时间', field: 'crawlerStartTime', type: 'date'},
        {title: '是否完成', field: 'isFinished', lookup: { 1: '异常', 2: '完成' },}

    ]
};

const crawlerReducers = (state = initialDataState, action) => {
    switch (action.type) {
        case 'ADD':
            let newDatas = {
                'No.': state.datas.length+1,
                crawlerUrl: 'www.test.com',
                crawlerStartTime: '2019-12-31',
                isFinished: 1,
            };
            let tmpDatas = state.datas;
            tmpDatas.push(newDatas);
            return Object.assign({}, state, {datas: tmpDatas});
        case 'SAVE':
            return state;
        case 'REFRESHPAGE': {
            let dataList = action.dataList;
            return Object.assign({}, state, {datas: dataList});
        }
        default:
            return state;
    }
};
export default crawlerReducers;

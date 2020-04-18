import * as Yup from 'yup';

export const initDataInfo={ //新增时的初始数据
    taskName: '',
        seedUrl: '',
        taskType:1,
        resume:1,
        taskState:0,
        threadNum:20,
        urlDepth:3,
        hasImage:0,
        startDate: "",
        endDate: "",
        matchRules: "",
        actualNumber: 0,
        remark: "",
        maxNumber:500,
        tempDir:"/temp/"
};


Yup.setLocale({
    mixed:{
        default: '${path}必须输入',
    },
    number: {
        min: '不能小于 ${min}！',
        max:'不能大于${max}！',
        required:'必须输入！'
    },
    string:{
        min: '长度不能小于 ${min}',
        max:'长度不能大于${max}',
        required:'${path}必须输入！'
    }
});
const taskValidatorSchema = Yup.object().shape({
    taskName: Yup.string()
        .min(2, '任务名称长度必须大于2!')
        .max(100, '任务名称长度不能大于100!')
        .required('任务名称必须输入！'),
    seedUrl: Yup.string()
        .max(200, '种子地址长度不能大于200!')
        .required('种子地址必须输入'),
    matchRules: Yup.string()
        .max(200, '种子地址长度不能大于200!'),
    maxNumber:Yup.number().min(1).max(99999),
    urlDepth:Yup.number().min(0).max(5),
    threadNum:Yup.number().min(1).max(30)
});

export default  taskValidatorSchema;
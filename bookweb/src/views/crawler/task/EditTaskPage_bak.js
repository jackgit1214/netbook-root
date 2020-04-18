import {makeStyles} from '@material-ui/core/styles';
import {grayColor, primaryColor} from '../../../assets/jss/material-dashboard-react';
import useId from 'react-use-uuid';
import {useForm,Controller } from 'react-hook-form';
import Draggable from "react-draggable";
import Button from 'components/CustomButtons/Button.js';
import Paper from '@material-ui/core/Paper/Paper';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
import Divider from '@material-ui/core/Divider';
import {KeyboardDateTimePicker, MuiPickersUtilsProvider} from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import DialogActions from '@material-ui/core/DialogActions';
import React from 'react';


const dislogStyles = makeStyles(theme=>({
    root: {
        width: '100%',
    },
    dialogPaper:{
        margin:10,

    },
    dialogTitle:{
        padding:"5px 10px",
        backgroundColor:grayColor[5],
        "& .MuiTypography-root":{
            fontSize:"12px !important",
        }
    },
    dialogContent:{
        padding: "5px 10px",
        "& .MuiInput-input,& .MuiInputLabel-root":{
            fontSize:13,
            paddingBottom:3,
        },
        "& .MuiButtonBase-root":{ //目前不起作用
            padding:2,
            color:primaryColor[1]
        }
    },
    dialogActions:{
        padding:"2px 2px",
        justifyContent:"center"
    },
    divideMiddle:{
        marginLeft:0,
        marginRight:0,
        height:2,
        borderColor:grayColor[1]
    },
    textFieldRoot: {
        marginTop:3,
        marginBottom:1,
    },
    menuItemRoot:{
        paddingLeft:8,
        paddingRight:8,
        paddingTop:2,
        fontSize:13,
    },

    gridItem:{
        padding:1,
    }
}));


export default  function EditDialog(props){
    const classes = dislogStyles();

    const {dataInfo,dialogStatus,dialogControlFun} = props;
    const dialogId = useId();

    const {register, handleSubmit, triggerValidation, watch, control,errors,getValues} = useForm({defaultValues: {
            taskState: "0", //任务状态
            endDate: "", //结束时间
            startDate:"",  //开始时间
            actualNumber:0, //实际数量
            maxNumber:0,//网页最大数量
            urlDepth:0,//网页深度
            hasImage:0,//获取图片
            resume:0,//续传
            tempDir:"c:/temp/",// 临时目录,暂时不能更改
            threadNum:10,//线程数量，默认为10
            matchRules:"",// 匹配规则
            seedUrl:"",// 种子网址
            taskType:0,// 任务类型
            taskName:"测试任务",// 任务名称
        }});

    const handleClose = () => {
        dialogControlFun(false);
    };

    const editError = (obj,oriName) => {
        //let formValues = getValues();
        //console.log(formValues);
        if (errors[obj]) {
            let errorInfo = errors[obj];
            return oriName + errorInfo.message;
        } else {
            return oriName;
        }
    };

    function handleSave(data, event) {

        alert(JSON.stringify(data))
        //alert('提交的名字: ');
       // console.log(arguments);
        //console.log(data);
        //event.preventDefault();
    }
    const PaperComponent = (props) => {
        return (<Draggable handle={'#taskDialog' + dialogId} cancel={'[class*="MuiDialogContent-root"]'}>
                <Paper {...props} />
            </Draggable>
        );
    };
    return (
        <Dialog open={dialogStatus} onClose={handleClose}  classes={{paper:classes.dialogPaper}}               disableBackdropClick={true}
                disableEscapeKeyDown={true} maxWidth={"sm"} PaperComponent={PaperComponent} aria-labelledby="form-dialog-title">
            <DialogTitle classes={{root:classes.dialogTitle}}  id={'taskDialog' + dialogId}  style={{cursor: 'move'}}>编辑抓取任务</DialogTitle>
            <DialogContent classes={{root:classes.dialogContent}}>
                <form className={classes.root} noValidate autoComplete="off" >
                    <Grid container>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                id='taskName'
                                name='taskName'
                                classes={{root:classes.textFieldRoot}}
                                label={editError('taskName','任务名称')}
                                //helperText="任务名称可以为网址或自拟"
                                size="small"
                                margin="dense"
                                InputProps={{id: 'taskName', name: 'taskName',}}
                                inputRef={register({ required: {value: true, message: '必须输入！'} ,
                                            maxLength:{value:100,message:'长度不能大于100'} ,
                                            minLength: {value:10,message:'长度不能小于10'}})}
                            />
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                label={editError('seedUrl',"种子地址")}
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'seedUrl', name: 'seedUrl',type:"url"}}
                                inputRef={register({ required: {value:true,message:"必须输入"} ,
                                                maxLength:{value:200,message:'长度不能大于200'}})}
                            />
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                label="任务类型"
                                style={{width:120}}
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                select
                                InputProps={{id: 'taskType', name: 'taskType',}}
                                inputRef={register}
                            >
                                <MenuItem classes={{root:classes.menuItemRoot}} key={0} value={0}>网站</MenuItem>
                                <MenuItem classes={{root:classes.menuItemRoot}} key={1} value={0}>网页</MenuItem>
                                <MenuItem classes={{root:classes.menuItemRoot}} key={2} value={0}>部分网页</MenuItem>
                            </TextField>
                        </Grid>
                        <Grid item xs={8}className={classes.gridItem}>
                            <TextField
                                label="临时目录"
                                fullWidth disabled
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'tempDir', name: 'tempDir',}}
                                inputRef={register}
                            />
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                label="匹配规则"
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'matchRules', name: 'matchRules',}}
                                inputRef={register}
                            />
                        </Grid>

                        <Grid item xs={4}className={classes.gridItem}>
                            <TextField
                                label="是否续传"
                                style={{width:120}}
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                select
                                InputProps={{id: 'resume', name: 'resume',}}
                                inputRef={register}
                            >
                                <MenuItem key={1} classes={{root:classes.menuItemRoot}} value={1}>是</MenuItem>
                                <MenuItem key={0} classes={{root:classes.menuItemRoot}} value={0}>否</MenuItem>
                            </TextField>
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                classes={{root:classes.textFieldRoot}}
                                label={editError('maxNumber',"最大抓取页面")}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'maxNumber', name: 'maxNumber',type:"number"}}
                                inputRef={register({max:{value:99999,message:'不能大于9999！'} ,
                                            min: {value:1,message:'不能小于1！'}})}
                            />
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                label="获取图片"
                                style={{width:120}}
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                select
                                margin="dense"
                                InputProps={{id: 'hasImage', name: 'hasImage',}}
                                inputRef={register}
                            >
                                <MenuItem key={1} classes={{root:classes.menuItemRoot}} value={1}>是</MenuItem>
                                <MenuItem key={0} classes={{root:classes.menuItemRoot}} value={0}>否</MenuItem>
                            </TextField>
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                label={editError("urlDepth","页面深度")}
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'urlDepth', name: 'urlDepth',type:'number'}}
                                inputRef={register({max:{value:5,message:'不能大于5！'} })}
                            />
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <TextField
                                label= {editError("threadNum","线程数")}
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'threadNum', name: 'threadNum',type:'number'}}
                                inputRef={register({ min: {value:1,message:'不能小于1'}, max: {value:30,message:'不能大于30！'} })}
                            />
                        </Grid>
                        <Grid item xs={4}className={classes.gridItem}>
                            <TextField
                                label="任务状态"
                                style={{width:120}}
                                select
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'taskState', name: 'taskState',}}
                                inputRef={register}
                            >
                                <MenuItem classes={{root:classes.menuItemRoot}} key={0} value={0}>未开始</MenuItem>
                                <MenuItem classes={{root:classes.menuItemRoot}} key={1} value={0}>进行中</MenuItem>
                                <MenuItem classes={{root:classes.menuItemRoot}} key={2} value={0}>已完成</MenuItem>
                            </TextField>
                        </Grid>

                    </Grid>
                    <Divider variant="middle" classes={{middle:classes.divideMiddle}} />
                    <Grid container>
                        <Grid item xs={4}className={classes.gridItem}>
                            <TextField
                                label="实际数量" disabled
                                classes={{root:classes.textFieldRoot}}
                                //helperText="Some important text"
                                margin="dense"
                                InputProps={{id: 'actualNumber', name: 'actualNumber',}}
                                inputRef={register}
                            />
                        </Grid>
                        <Grid item xs={4}className={classes.gridItem}>
                            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <KeyboardDateTimePicker
                                    disableToolbar
                                    classes={{root:classes.textFieldRoot}}
                                    style={{width:180}}
                                    variant="inline"
                                    format="yyyy年MM月dd日 hh:mm:ss"
                                    margin="normal"
                                    id="date-picker-inline"
                                    label="开始时间"
                                    InputProps={{id: 'startDate', name: 'startDate',}}
                                    inputRef={register}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change date',
                                    }}
                                />
                            </MuiPickersUtilsProvider>
                        </Grid>
                        <Grid item xs={4} className={classes.gridItem}>
                            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                <KeyboardDateTimePicker
                                    disableToolbar
                                    classes={{root:classes.textFieldRoot}}
                                    style={{width:180}}
                                    variant="inline"
                                    format="yyyy年MM月dd日 hh:mm:ss"
                                    margin="normal"
                                    id="date-picker-inline"
                                    label="结束时间"
                                    InputProps={{id: 'endDate', name: 'endDate',}}
                                    inputRef={register}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change date',
                                    }}
                                />
                            </MuiPickersUtilsProvider>
                        </Grid>
                    </Grid>
                    <Button type={"submit"}  color="primary" size={'sm'}>
                        保存
                    </Button>
                </form>

            </DialogContent>
            <DialogActions classes={{root:classes.dialogActions}}>
                <Button onClick={handleClose} color="primary"  size={'sm'}>
                    关闭
                </Button>
                <Button onClick={handleSubmit(handleSave)}  color="primary" size={'sm'}>
                    保存
                </Button>
            </DialogActions>
        </Dialog>
    )
}

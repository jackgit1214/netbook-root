import {makeStyles} from '@material-ui/core/styles';
import {grayColor, primaryColor} from '../../../assets/jss/material-dashboard-react';
import useId from 'react-use-uuid';
import Draggable from "react-draggable";
import Button from 'components/CustomButtons/Button.js';
import Paper from '@material-ui/core/Paper/Paper';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import Grid from '@material-ui/core/Grid';
import MenuItem from '@material-ui/core/MenuItem';
import Divider from '@material-ui/core/Divider';
import { LinearProgress } from '@material-ui/core';
import {KeyboardDateTimePicker,DateTimePicker, MuiPickersUtilsProvider} from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import DialogActions from '@material-ui/core/DialogActions';
import React from 'react';
import { useFormikContext,Formik,connect, getIn, Form, Field,errors } from 'formik';

import { TextField } from 'formik-material-ui';
import taskValidatorSchema,{initDataInfo} from "./TaskValidatorSchema";
import clsx from 'clsx';
import { useDispatch } from 'react-redux'

const dialogStyles = makeStyles(theme=>({
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
        "&:first-child":{
            paddingTop:1,
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
        marginTop:4,
        marginLeft:0,
        marginRight:0,
        height:2,
        borderColor:grayColor[1]
    },
    menuItemRoot:{
        paddingLeft:8,
        paddingRight:8,
        paddingTop:2,
        fontSize:13,
    },

    gridItem:{
        padding:1,
        "& .MuiTextField-root":{
            marginTop:3,
            marginBottom:1,
        },
        "& .MuiFormHelperText-root":{ //对验证及帮助进行隐藏
            visibility:"hidden",
            height:0
        }
    }
}));

 function EditDialog(props){
    const classes = dialogStyles();
    const {task,dialogStatus,dialogControlFun} = props;
    const [editStatus,setEditStatus] =React.useState(false);
    //const [ediValues,setEditValues] =React.useState(initDataInfo);
    const oldValues =  task?task:initDataInfo;
    const dispatch = useDispatch();
    let editValues = task?task:initDataInfo;
    const dialogId = useId();

    const handleClose = () => {
        if (JSON.stringify(editValues)!=JSON.stringify(oldValues)){ //比较JSON对象的值
            let  confirm = window.confirm("数据已经修改，确定不保存吗？");
            if (confirm){
                dispatch({type:"closeEditDialog"});
            }
        }else
            dispatch({type:"closeEditDialog"});
    };


    function handleSave(data, {setSubmitting}) {
        setSubmitting(false);
        dispatch({type:"createTask",params:{
                customModel:data,
            }});
     }
    const PaperComponent = (props) => {
        return (<Draggable handle={'#taskDialog' + dialogId} cancel={'[class*="MuiDialogContent-root"]'}>
                <Paper {...props} />
            </Draggable>
        );
    };

     const FormMonitor = () => {
         const { values,initialValues } = useFormikContext();
         editValues = values;
         return null;
     };
    return (
        <Dialog open={dialogStatus} onClose={handleClose}  classes={{paper:classes.dialogPaper}}               disableBackdropClick={true}
                disableEscapeKeyDown={true} maxWidth={"sm"} PaperComponent={PaperComponent} aria-labelledby="form-dialog-title">
            <DialogTitle classes={{root:classes.dialogTitle}}  id={'taskDialog' + dialogId}  style={{cursor: 'move'}}>编辑抓取任务</DialogTitle>
            <Formik
                initialValues={task?task:initDataInfo}
                validationSchema={taskValidatorSchema}
                onSubmit={handleSave}
            >
                {({ submitForm, values,errors,isSubmitting }) => (
                    <Form>
                        <DialogContent classes={{root:classes.dialogContent}}>
                            <Grid container>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        name="taskName"
                                        label={errors.taskName? errors.taskName : "任务名称"}
                                        size="small"
                                        margin="dense"
                                        InputProps={{id: 'taskName', name: 'taskName',}}
                                    />
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                        <Field
                                            component={TextField}
                                            label={errors.seedUrl? errors.seedUrl : "种子地址"}
                                            name="seedUrl"
                                            //helperText="输入网址"
                                            size="small"
                                            margin="dense"
                                        />
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        style={{width:120}}
                                        component={TextField}
                                        label="任务类型"
                                        //helperText="选择任务类型"
                                        name="taskType"

                                        select
                                        size="small"
                                        margin="dense"
                                    >
                                        <MenuItem classes={{root:classes.menuItemRoot}} key={0} value={0}>网站</MenuItem>
                                        <MenuItem classes={{root:classes.menuItemRoot}} key={1} value={1}>网页</MenuItem>
                                        <MenuItem classes={{root:classes.menuItemRoot}} key={2} value={2}>部分网页</MenuItem>
                                    </Field>
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label="临时目录"
                                        fullWidth disabled
                                        size="small"
                                        margin="dense"
                                        //helperText="爬虫工具临时存放目录"
                                        name="tempDir"
                                    />
                                </Grid>
                                <Grid item xs={8} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        fullWidth
                                        label="匹配规则"
                                        //helperText="网址匹配规则"
                                        name="matchRules"
                                        size="small"
                                        margin="dense"
                                    />
                                </Grid>

                                <Grid item xs={4}className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label="是否续传"
                                        //helperText="任务未完成时是否续传"
                                        name="resume"
                                        style={{width:120}}
                                        size="small"
                                        select
                                        margin="dense"
                                    >
                                        <MenuItem key={1} classes={{root:classes.menuItemRoot}} value={1}>是</MenuItem>
                                        <MenuItem key={0} classes={{root:classes.menuItemRoot}} value={0}>否</MenuItem>
                                    </Field>
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label={errors.maxNumber? errors.maxNumber : "最大抓取页面"}
                                        type={"number"}
                                        name="maxNumber"
                                        //helperText="抓取页面数量"
                                        size="small"
                                        margin="dense"
                                    />
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label="获取图片"
                                        name="hasImage"
                                        select
                                        //helperText="是否取图片"
                                        size="small"
                                        margin="dense"
                                        style={{width:120}}
                                    >
                                        <MenuItem key={1} classes={{root:classes.menuItemRoot}} value={1}>是</MenuItem>
                                        <MenuItem key={0} classes={{root:classes.menuItemRoot}} value={0}>否</MenuItem>
                                    </Field>
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label={errors.urlDepth? errors.urlDepth : "页面深度"}
                                        name="urlDepth"
                                        type={"number"}
                                        //helperText="抓取页面层级"
                                        size="small"
                                        margin="dense"
                                    />
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label={errors.threadNum? errors.threadNum : "线程数"}
                                        name="threadNum"
                                        type={"number"}
                                        //helperText="使用多少个线程"
                                        size="small"
                                        margin="dense"
                                    />
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label="任务状态"
                                        select
                                        style={{width:120}}
                                        name="taskState"
                                        //helperText="任务状态"
                                        size="small"
                                        margin="dense"
                                    >
                                        <MenuItem classes={{root:classes.menuItemRoot}} key={0} value={0}>未开始</MenuItem>
                                        <MenuItem classes={{root:classes.menuItemRoot}} key={1} value={1}>进行中</MenuItem>
                                        <MenuItem classes={{root:classes.menuItemRoot}} key={2} value={2}>已完成</MenuItem>
                                    </Field>
                                </Grid>
                            </Grid>
                            <Divider variant="middle" classes={{middle:classes.divideMiddle}} style={{display:clsx(task?"":"none")}} />
                            <Grid container style={{display:clsx(task?"":"none")}}>

                                <Grid item xs={4} className={classes.gridItem}>
                                    <Field
                                        component={TextField}
                                        label="实际数量"
                                        name="actualNumber"
                                        type={"number"}
                                        disabled
                                        //helperText="Some important text"
                                        size="small"
                                        margin="dense"
                                    />
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                        <DateTimePicker
                                            disableToolbar
                                            style={{width:160}}
                                            disabled
                                            variant="inline"
                                            format="yyyy年MM月dd日 hh:mm:ss"
                                            margin="normal"
                                            id="date-picker-inline"
                                            label="开始时间"
                                            InputProps={{id: 'startDate', name: 'startDate',}}
                                        />
                                    </MuiPickersUtilsProvider>
                                </Grid>
                                <Grid item xs={4} className={classes.gridItem}>
                                    <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                        <DateTimePicker
                                            disableToolbar
                                            disabled
                                            style={{width:160}}
                                            variant="inline"
                                            format="yyyy年MM月dd日 hh:mm:ss"
                                            margin="normal"
                                            id="date-picker-inline"
                                            label="结束时间"
                                            InputProps={{id: 'endDate', name: 'endDate',}}
                                        />
                                    </MuiPickersUtilsProvider>
                                </Grid>
                            </Grid>

                        </DialogContent>
                        <DialogActions classes={{root:classes.dialogActions}}>
                            <Button onClick={handleClose} color="primary"  size={'sm'}>
                                关闭
                            </Button>
                            <Button onClick={submitForm} color="primary" size={'sm'}>
                                保存
                            </Button>
                        </DialogActions>
                        <FormMonitor />
                    </Form>
                )}
            </Formik>
        </Dialog>
    )
}

export default EditDialog;
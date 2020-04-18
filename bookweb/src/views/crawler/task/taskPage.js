import React from 'react';
import {connect, useSelector} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as taskActions from './CrawlerTask';
import Paper from '@material-ui/core/Paper/Paper';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Button from 'components/CustomButtons/Button.js';
import Search from '@material-ui/icons/Search';
import RotateLeftIcon from '@material-ui/icons/RotateLeft';
import {useForm} from 'react-hook-form';
import {footerBarHeight, headerBarHeight, queryHeight, tableHeight} from 'assets/jss/material-dashboard-react.js';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { Alert, AlertTitle } from '@material-ui/lab';
import Slide from '@material-ui/core/Slide';
import AddIcon from '@material-ui/icons/Add';
import DeleteIcon from '@material-ui/icons/Delete';
import PlayCircleOutlineIcon from '@material-ui/icons/PlayCircleOutline';
import WebIcon from '@material-ui/icons/Web';
import SaveAltIcon from '@material-ui/icons/SaveAlt';
import EditIcon from '@material-ui/icons/Edit';
import StopIcon from '@material-ui/icons/Stop';
import SpellcheckIcon from '@material-ui/icons/Spellcheck';
import Snackbar from '@material-ui/core/Snackbar';
import CheckIcon from '@material-ui/icons/Check';
import {grayColor, primaryColor, successColor} from '../../../assets/jss/material-dashboard-react';
import { makeStyles } from "@material-ui/core/styles";
import EditTaskDialog from './EditTaskPage';
import Tooltip from '@material-ui/core/Tooltip';

const useStyles = makeStyles(theme => ({
    root: {
        width: '100%',
    },
    textFieldRoot: {
        marginLeft: 10,
    },
    labelRoot: {
        fontWeight: '400',
        fontSize: '14px',
        marginLeft: 3,
        marginTop: 3,
        lineHeight: '0.62857',
        letterSpacing: 'unset',
    },
    marginTop: {
        marginTop: '10px !important',
        marginLeft: 3,
    },
    queryWrapper: {
        minHeight: queryHeight,
        padding: 3,
        margin: 3,
    },
    mainWrapper: {
        padding: 2,
        margin: 2,
        backgroundColor: grayColor[5],
    },
    cardWrapper: {
        maxWidth: 295,
        minWidth:295,
        minHeight:140,
    },

    cardLastWrapper: {
        minWidth:150,
        minHeight:120,
        backgroundColor: grayColor[5],
    },
    cardHeaderRoot: {
        padding: 5,
        "& .MuiCardHeader-action":{
            marginTop:-4,
            marginRight:-4
        }
    },
    cardContentRoot: {
        padding: 5,
        '&:last-child': {
            textAlign: 'center',
            align: 'center',
        },
    },
    cardActionRoot: {
        padding: 5,

    },
    cardActionButton: {
        padding: 5,
        color:primaryColor[1],

    },
    listContentWrapper: {
        overflowX: 'auto',
    },
    avatar: {
        backgroundColor: successColor[1],
    },
    gridItem: {
        margin: 8,
    },
    heading: {
        fontSize: theme.typography.pxToRem(14),
        flexBasis: '33.33%',
        flexShrink: 0,
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(14),
        color: theme.palette.text.secondary,
    },
}));

function SlideTransition(props) {
    return <Slide {...props} direction="down" />;
}
function TaskPage(props) {
    const classes = useStyles();
    const {register, handleSubmit, triggerValidation, watch, errors} = useForm();
    let pageActions = props.taskAction;
    const pageInfo = props.task.pageInfo;
    const [queryInfo,setQueryInfo] = React.useState({taskName:"",seedUrl:""});
    const taskList = useSelector(state => state.task.dataRecords);
    const dialogStatus=useSelector(state=>state.task.dialogStatus);
    const dataInfo = useSelector(state => state.task.dataInfo);
    //定义显示信息，当变化并且有值时，显示
    const message = useSelector(state => state.task.message);
    const [snackBarStatus,setSnackBarStatus] = React.useState(false);
    const code = useSelector(state=> state.task.code);
    const errorInfo = useSelector(state=> state.task.errorInfo);
    /**
     * 打开编辑窗口
     * @param edit 是编辑，还是新增,0 编辑，1新增
     */
    const openEditTask=(task) =>{
       pageActions.showEditDialog(task);
    }

    React.useEffect(() => {
       if (message!="" && message!=undefined){
           setSnackBarStatus(true);
       }
       if (code=="99"){
            console.log("终止 定时器"+dataInfo.taskId);
            clearInterval(dataInfo.taskId);
       }
    }, [message]);

    React.useEffect(() => {
        retrieveData({
            curPage:1,
            pageSize:pageInfo.pageRows
        })

    }, []);

    const retrieveData = (pageParams,queryParam)=>{
        let tmpQuery = {
            otherParams: {
                taskName: queryParam?queryParam.taskName : queryInfo.taskName,
                seedUrl: queryParam?queryParam.seedUrl:queryInfo.seedUrl,
            },
            pageInfo: {
                curPage: pageParams.curPage,
                pageSize: pageParams.pageSize,
            },
        };
       pageActions.retrieveData(tmpQuery);
    }
    function onSubmit(data, event) {
        //alert('提交的名字: ');
       // console.log(arguments);
       // console.log(data);
       // event.preventDefault();
    }

    const testWeiXin = ()=>{
        pageActions.handleWeiInfo("ddd");
    }
    const handleSubmitError = (obj,oriName) => {
        //console.log(errors[obj])
        if (errors[obj]) {
            let errorInfo = errors[obj];
            return oriName+errorInfo.message;
        } else {
            return oriName;
        }
    };

    return (
        // style={{backgroundColor:"blue",height:`calc(100vh - 134px)`}}

        <div className={classes.listContentWrapper}>
            <Snackbar open={snackBarStatus} autoHideDuration={3000}
                      TransitionComponent={SlideTransition}
                      onClose={()=>{setSnackBarStatus(false)}}
                      anchorOrigin={{ vertical:'top',horizontal:"center" }} >
                <Alert elevation={6} variant="filled" severity="success">
                    {/*<AlertTitle>消息提示</AlertTitle>*/}
                    {message}
                </Alert>
            </Snackbar>
            <EditTaskDialog task={dataInfo} dialogStatus={dialogStatus} />
              <Paper className={classes.queryWrapper}>
                <form className={classes.root} noValidate autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
                    <Grid container>
                        <Grid item xs={10}>
                            <TextField id="taskName" name="taskName"
                                       label={handleSubmitError('taskName','名称')}
                                       classes={{root: classes.textFieldRoot}}
                                       size="small"
                                       InputLabelProps={{classes: {root: classes.labelRoot}}}
                                       InputProps={{classes: {root: classes.marginTop}}}
                                       inputRef={register}/>
                            <TextField id="seedUrl" name="seedUrl" label={handleSubmitError('seedUrl',"种子地址")}
                                       size="small" classes={{root: classes.textFieldRoot}}
                                       InputLabelProps={{classes: {root: classes.labelRoot}}}
                                       InputProps={{classes: {root: classes.marginTop}}}
                                       inputRef={register}/>
                        </Grid>
                        <Grid item xs={2}>
                            <Button color="primary" aria-label="edit" size={'sm'} type="submit" startIcon={<Search/>}>
                                查询
                            </Button>
                            <Button color="info" aria-label="edit" size={'sm'} type="reset" onClick={testWeiXin}
                                    startIcon={<RotateLeftIcon/>}>
                                重置
                            </Button>
                        </Grid>
                    </Grid>
                </form>
            </Paper>
            <Paper className={classes.mainWrapper}>
                <Grid container alignItems={'center'} >
                    {taskList.map((task, i) => {
                        const handleStartTask = () =>{
                            pageActions.startTask(task.taskId);
                            let test = task.taskId;
                            test = setInterval(function(){
                                pageActions.pageIntervalRefresh(task.taskId);
                            },60000);
                        }
                        const handleStopTask = () =>{
                            pageActions.stopTask(task.taskId);
                        }
                        const handleDeleteTask = () =>{
                            pageActions.deleteTask(task.taskId);
                        }
                        const handleTaskToBook=()=>{
                            pageActions.analysisWebPage(task.taskId);
                        }
                        const handleErrorRecord=()=>{
                            pageActions.handleErrorRecord(task.taskId);
                        }
                        const handleWeiInfo =()=>{
                            pageActions.handleWeiInfo(task.taskId);
                        }
                        return (
                            <Grid item  className={classes.gridItem} key={"parent"+i} >
                                <Card className={classes.cardWrapper}>
                                    <CardHeader className={classes.cardHeaderRoot}
                                                avatar={
                                                    <Avatar aria-label="recipe" style={{fontSize:12,color:"red"}} className={classes.avatar}>
                                                        {task.actualNumber}
                                                    </Avatar>
                                                }
                                                action={
                                                    <IconButton  aria-label="settings" className={classes.cardActionButton} onClick={()=>{
                                                        openEditTask(task)}
                                                    }>
                                                        <EditIcon/>
                                                    </IconButton>
                                                }
                                                title={task.taskName}
                                                subheader={task.startDate}
                                    />
                                    <CardContent className={classes.cardContentRoot}>
                                        <Grid container key={"child"+i}>
                                            <Grid item xs={3}>
                                                <Typography align={"right"} className={classes.heading}>地址：</Typography>
                                            </Grid>
                                            <Grid item xs={7}>
                                                <Typography  className={classes.secondaryHeading}>{task.seedUrl}</Typography>
                                            </Grid>
                                            <Grid item xs={3}>
                                                <Typography align={"right"}   className={classes.heading}>匹配规则：</Typography>
                                            </Grid>
                                            <Grid item xs={7}>
                                                <Typography  className={classes.secondaryHeading}>{task.matchRules}</Typography>
                                            </Grid>
                                        </Grid>
                                    </CardContent>
                                    <CardActions disableSpacing className={classes.cardActionRoot}>

                                         <IconButton aria-label="taskHandle" onClick={task.taskState==1?handleStopTask:handleStartTask} className={classes.cardActionButton}>
                                            <Tooltip title={task.taskState==1?"终止任务":"开始任务"}>
                                               {task.taskState==1?<StopIcon fontSize="small"/>:<PlayCircleOutlineIcon fontSize="small"/>}
                                            </Tooltip>
                                         </IconButton>
                                        <IconButton aria-label="taskHandle"   onClick={handleErrorRecord} className={classes.cardActionButton} >
                                            <Tooltip title="重新抓取错误页">
                                                <SpellcheckIcon fontSize="small" color="error" />
                                            </Tooltip>
                                        </IconButton>
                                            <IconButton aria-label="taskHandle"  onClick={handleTaskToBook} className={classes.cardActionButton} >
                                                <Tooltip title="任务处理,生成书籍">
                                                    <SaveAltIcon fontSize="small" color="primary" />
                                                </Tooltip>
                                        </IconButton>
                                        <IconButton style={{marginLeft: 'auto'}} onClick={handleDeleteTask} aria-label="delete task" className={classes.cardActionButton}>
                                            <Tooltip title="删除任务">
                                                <DeleteIcon/>
                                            </Tooltip>
                                        </IconButton>
                                        <IconButton style={{marginLeft: 'auto'}} onClick={handleWeiInfo} aria-label="delete task" className={classes.cardActionButton}>
                                            <Tooltip title="测试">
                                                <DeleteIcon/>
                                            </Tooltip>
                                        </IconButton>
                                    </CardActions>
                                </Card>
                            </Grid>

                        );
                    })}
                    <Grid item className={classes.gridItem}>
                        <Card className={classes.cardLastWrapper}>
                            <CardContent className={classes.cardContentRoot}>
                                <IconButton aria-label="share" className={classes.cardActionButton} onClick={()=>{
                                    openEditTask();
                                }}>
                                    <AddIcon style={{fontSize: 90}} />
                                </IconButton>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Paper>
        </div>

    );
}


const mapStateToProps = (state) => {
    return {
        task: state.task,
    };
};

const mapDispatchToProps = dispatch => {
    const taskAction = bindActionCreators(taskActions, dispatch);
    return {
        taskAction,
        dispatch,
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(TaskPage);

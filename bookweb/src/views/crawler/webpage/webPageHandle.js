import React from 'react';
import clsx from 'clsx';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as webActions from './webPageAction';
import {makeStyles} from '@material-ui/core/styles';
import CustomTable from 'components//Table/CustomTable';
import Paper from '@material-ui/core/Paper/Paper';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import Fab from '@material-ui/core/Fab';
import { useSelector ,useStore } from 'react-redux'
import Button from 'components/CustomButtons/Button.js';
import CustomDialog from 'components/Dialog/CustomDialog';
import Search from '@material-ui/icons/Search';
import CheckIcon from '@material-ui/icons/Check';
import SaveIcon from '@material-ui/icons/Save';
import CircularProgress from '@material-ui/core/CircularProgress';
import RotateLeftIcon from '@material-ui/icons/RotateLeft';
import {useForm} from 'react-hook-form';
import {footerBarHeight, headerBarHeight, queryHeight, tableHeight} from 'assets/jss/material-dashboard-react.js';
import Modal from '@material-ui/core/Modal';
import Backdrop from '@material-ui/core/Backdrop';
import Fade from '@material-ui/core/Fade';
import Snackbar from "@material-ui/core/Snackbar";
import {Alert} from "@material-ui/lab";
import Slide from "@material-ui/core/Slide";

const useStyles = makeStyles({
    root: {
        width: '100%',
    },
    textFieldRoot: {
        marginLeft: 10,
    },
    inputMargin:{
        paddingTop: 10,
        paddingBottom: 1,
    },
    labelRoot: {
        fontWeight: '400',
        fontSize: '13px',
        marginLeft: 3,
        marginTop: 3,
        lineHeight: '0.62857',
        letterSpacing: 'unset',
    },
    marginTop: {
        marginTop: '10px !important',
        marginLeft: 3,
    },
    selectPadding: {
        paddingTop: '4px !important',
        paddingBottom: '4px !important',
        fontSize: '13px',
    },
    queryWrapper: {
        minHeight: queryHeight,
        padding: 3,
        margin: 3,
    },
    tableMainWrapper: {
        padding: 2,
        margin: 2,
    },
    listContentWrapper: {
        overflowX: 'auto',
    },
    wrapper: {
        margin: 0,
        position: 'relative',
    },
    buttonSuccess: {
        backgroundColor: "blue",
        '&:hover': {
            backgroundColor: "red",
        },
    },
    modal: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    fabProgress: {
        color: "red",
        position: 'absolute',
        left:12,
        top:0,
        zIndex: 1,
    }
});

function SlideTransition(props) {
    return <Slide {...props} direction="down" />;
}

function WebPageHandle(props) {

    const classes = useStyles();
    let pageActions = props.webPageAction;
    const {register, handleSubmit, triggerValidation, watch, errors,getValues} = useForm({defaultValues: {
            isFinished: "99",
            crawlerUrl: "",
        }});
    const pageInfo = props.webPage.pageInfo;
    const [queryInfo,setQueryInfo] = React.useState({isFinished:"",crawlerUrl:""});
    const [dialogStatus, setDialogStatus] = React.useState({
        open:false,
        winType:1,
        infoType:'Info',
        infoContent:'默认提示信息'
    });
    const code= useSelector(state => state.webPage.code);
    const message= useSelector(state => state.webPage.message);
    const loading=useSelector(state=>state.webPage.loading);
    const actionLoading = useSelector(state=>state.webPage.actionLoading);
    const [snackBarStatus,setSnackBarStatus] = React.useState(false);
    let columns = props.webPage.columnTitles;
    let records = props.webPage.dataRecords;
    React.useEffect(() => {


    }, [loading]); //监控页面响应，有错误时显示错误信息

    React.useEffect(() => {
        //监控消息变化，如何需要显示，即弹出提示
        if (message!="" && message!=undefined){
            setSnackBarStatus(true);
        }
        if (code && code==1){ //需要刷新页面时
            retrieveData({
                curPage:pageInfo.curPage,
                pageSize:pageInfo.pageRows
            })
        }
    }, [message]);

    React.useEffect(() => {
        retrieveData({
            curPage:1,
            pageSize:pageInfo.pageRows
        })
    }, []);

    const retrieveData = (pageParams,queryParam)=>{
        //console.log("-----------"+new Date())
        let tmpQuery = {
            otherParams: {
                isFinished: queryParam?queryParam.isFinished : queryInfo.isFinished,
                crawlerUrl: queryParam?queryParam.crawlerUrl:queryInfo.crawlerUrl,
            },
            pageInfo: {
                curPage: pageParams.curPage,
                pageSize: pageParams.pageSize,
            },
        };
        pageActions.getPageRecord(tmpQuery);
    }

    const searchContent = (queryArgs) => {
        let formValue = getValues(); //重置查询条件
        let tmpQueryInfo = ({
            isFinished: formValue.isFinished === '99' ? '' : formValue.isFinished,
            crawlerUrl: formValue.crawlerUrl,
        })
        setQueryInfo(tmpQueryInfo);
        retrieveData({pageSize:pageInfo.pageRows,curPage:1},tmpQueryInfo);
    };

    const handleSubmitError = (obj) => {
        if (errors[obj]) {
            let errorInfo = errors[obj];
            return errorInfo.message;
        } else {
            return obj;
        }
    };

    const restartCrawlerUrl = (id, url) => {
        function handleAlertClose (status,param){
            let params = {
                otherParams: {
                    url: url,
                    id:id
                }
            };
            if (param)
                pageActions.restartCrawlerPage(params)
            setDialogStatus(Object.assign({},dialogStatus,{open:status}));
        }
        let tmpWin = {
            open:true,
            winType:21,
            infoContent: "确定要重新抓取网页吗？",
            handleClose:handleAlertClose
        }
        setDialogStatus(Object.assign({},dialogStatus,tmpWin));
    };

    const handlePage=(rows)=>{

        let params = {
            otherParams:{
                ids:[rows.logId.toString()],
                single:true,
            }

        };
        let conf = window.confirm("确认要处理页面吗？")
        if (conf){
            pageActions.handlePageContent(params)
        }
    };
    const pageDataDelete=(rows)=>{
        let params = {
            otherParams:{
                ids:[rows.logId],
                single:true,
            }

        };
        let conf = window.confirm("确定要删除当前记录吗？")
        if (conf){
            pageActions.delPageRecord(params)
        }
    }
    return (
        // style={{backgroundColor:"blue",height:`calc(100vh - 134px)`}}
        <div className={classes.listContentWrapper}>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                className={classes.modal}
                open={!loading}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <CircularProgress color="secondary" />
            </Modal>
            <CustomDialog status={dialogStatus.open} handleClose={dialogStatus.handleClose}  windowType={dialogStatus.winType} infoContent={dialogStatus.infoContent} infoType={dialogStatus.infoType}/>
            <Snackbar open={snackBarStatus} autoHideDuration={3000}
                      TransitionComponent={SlideTransition}
                      onClose={()=>{setSnackBarStatus(false)}}
                      anchorOrigin={{ vertical:'top',horizontal:"center" }} >
                <Alert elevation={6} variant="filled" severity="success">
                    {/*<AlertTitle>消息提示</AlertTitle>*/}
                    {message}
                </Alert>
            </Snackbar>
            <Paper className={classes.queryWrapper}>
                <form className={classes.root} noValidate autoComplete="off">
                    <Grid container>
                        <Grid item xs={10}>
                            <FormControl classes={{root: classes.textFieldRoot}}>
                                <InputLabel classes={{root: classes.labelRoot}} htmlFor="isFinished">状态</InputLabel>
                                <Select
                                    native
                                    classes={{select: classes.selectPadding}}
                                    inputProps={{
                                        name: 'isFinished',
                                        id: 'isFinished',
                                    }}
                                    inputRef={register}
                                >
                                    <option value={99}>全部</option>
                                    <option value={2}>内容已处理</option>
                                    <option value={1}>内容未处理</option>
                                    <option value={0}>抓取错误</option>
                                </Select>
                            </FormControl>
                            <TextField label={handleSubmitError('网址')}
                                       classes={{root: classes.textFieldRoot}}
                                       size="small"
                                       InputLabelProps={{classes: {root: classes.labelRoot}}}
                                       InputProps={{id: 'crawlerUrl', name: 'crawlerUrl', classes: {root: classes.marginTop,inputMarginDense:classes.inputMargin}}}
                                       inputRef={register}/>
                        </Grid>
                        <Grid item xs={2}>
                            <Button color="primary" aria-label="edit" size={'sm'} onClick={handleSubmit(searchContent)}
                                    startIcon={<Search/>}>
                                查询
                            </Button>
                            <Button color="info" aria-label="edit" size={'sm'}
                                    startIcon={<RotateLeftIcon/>}>
                                重置
                            </Button>
                        </Grid>
                    </Grid>
                </form>
            </Paper>
            <Paper className={classes.tableMainWrapper}>
                 <CustomTable
                    tableHeaderColor="primary"
                    tableHead={columns}
                    tableData={records!=null?records:[]}
                    physicalPageHandle={retrieveData}
                    primaryKey={'primaryKey'}
                    Sequence={true}
                    Selections={false}
                    tableHeight={tableHeight}
                    actions={[
                        {
                            key: 'craw_',
                            id:"logId",
                            icon: 'refresh',
                            tooltip: '重新抓取',
                            actionSuccess:actionLoading,
                            onClick: (rowData) => {
                                restartCrawlerUrl(rowData.logId, rowData.crawlerUrl)
                            },
                        },
                        {
                            key:'handle_',
                            id:"logId",
                            icon: 'archive',
                            actionSuccess:actionLoading,
                            tooltip: '内容处理',
                            onClick: (rowData) => {
                                handlePage(rowData);
                            },
                        },
                        {
                            key:'del_',
                            id:"logId",
                            icon: 'delete',
                            actionSuccess:actionLoading,
                            tooltip: '删除',
                            onClick: (rowData) => {
                                pageDataDelete(rowData)
                            },
                        },
                    ]}
                    page={{
                        totalRows: pageInfo.totalRows,
                        curPage: pageInfo.curPage - 1, //分页工具从0开始
                        pageSize: pageInfo.pageRows,
                    }}
                />
            </Paper>
        </div>

    );
}

const mapStateToProps = (state) => {
    return {
        webPage: state.webPage,
    };
};

const mapDispatchToProps = dispatch => {
    const webPageAction = bindActionCreators(webActions, dispatch);
    return {
        webPageAction,
        dispatch,
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(WebPageHandle);

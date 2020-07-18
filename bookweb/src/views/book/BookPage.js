import React from 'react';
import {connect, useSelector} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as bookActions from './Book';
import Paper from '@material-ui/core/Paper/Paper';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Button from 'components/CustomButtons/Button.js';
import Search from '@material-ui/icons/Search';
import RotateLeftIcon from '@material-ui/icons/RotateLeft';
import {useForm} from 'react-hook-form';
import {
    footerBarHeight,
    headerBarHeight,
    queryHeight,
    tableHeight,
    grayColor,
    primaryColor,
    successColor
} from 'assets/jss/material-dashboard-react';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import CardActions from '@material-ui/core/CardActions';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import {Alert, AlertTitle} from '@material-ui/lab';
import Slide from '@material-ui/core/Slide';
import AddIcon from '@material-ui/icons/Add';
import Book from '@material-ui/icons/Book';
import DeleteIcon from '@material-ui/icons/Delete';
import PublishIcon from '@material-ui/icons/Publish';
import ShareIcon from '@material-ui/icons/Share';
import SaveAltIcon from '@material-ui/icons/SaveAlt';
import EditIcon from '@material-ui/icons/Edit';
import StopIcon from '@material-ui/icons/Stop';
import SpellcheckIcon from '@material-ui/icons/Spellcheck';
import Snackbar from '@material-ui/core/Snackbar';
import CheckIcon from '@material-ui/icons/Check';
import {makeStyles} from "@material-ui/core/styles";
import Tooltip from '@material-ui/core/Tooltip';
import base64js from 'base64-js';

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
        maxWidth: 300,
        minWidth: 235,
        maxHeight: 220,
        minHeight: 220
    },
    cardHeaderRoot: {
        padding: 5,

        "& .MuiCardHeader-action": {
            marginTop: -4,
            marginRight: -4
        },
        "& .MuiCardHeader-avatar": {
            color: primaryColor[2],
        }
    },
    cardContentRoot: {
        padding: 5,
        "& .paragraphDetail": {
            display: "-webkit-box",
            marginBottom: 5,
            width: 120,
            WebkitLineClamp: 5,
            boxOrient: "vertical",
            textOverflow: "ellipsis",
            overflow: "hidden"
        }
    },
    cardActionRoot: {
        padding: 5,
    },
    cardActionButton: {
        padding: 5,
        color: primaryColor[1],

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
    details: {
        display: 'flex',
        flexDirection: 'row',
    },
    cover: {
        width: 90,
    },
    controls: {
        display: 'flex',
        alignItems: 'center',
        paddingLeft: theme.spacing(1),
        paddingBottom: theme.spacing(1),
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
    return <Slide {...props} direction="down"/>;
}

function BookPage(props) {
    const classes = useStyles();
    const {register, handleSubmit, triggerValidation, watch, errors, getValues} = useForm();
    let pageActions = props.bookAction;
    const pageInfo = props.book.pageInfo;
    const [queryInfo, setQueryInfo] = React.useState({bookName: "", seedUrl: ""});
    const books = useSelector(state => state.book.dataRecords);
    const dialogStatus = useSelector(state => state.book.dialogStatus);
    const dataInfo = useSelector(state => state.book.dataInfo);
    const wxChat = useSelector(state => state.book.wxChat);
    //console.log(wxConfig);
    //定义显示信息，当变化并且有值时，显示
    const message = useSelector(state => state.book.message);
    const [snackBarStatus, setSnackBarStatus] = React.useState(false);
    const code = useSelector(state => state.book.code);
    const errorInfo = useSelector(state => state.book.errorInfo);

    /**
     * 打开编辑窗口
     * @param edit 是编辑，还是新增,0 编辑，1新增
     */
    const openEditBook = (book) => {
        pageActions.showEditDialog(book);
    };

    React.useEffect(() => {
        if (message != "" && message != undefined) {
            setSnackBarStatus(true);
        }
    }, [message]);

    const retrieveData = (pageParams, queryParam) => {
        let tmpQuery = {
            otherParams: {
                bookName: queryParam ? queryParam.bookName : queryInfo.bookName,
                category: ''
            },
            pageInfo: {
                curPage: pageParams.curPage,
                pageSize: pageParams.pageSize,
            },
        };
        pageActions.retrieveDataAction(tmpQuery);
    };

    React.useEffect(() => {
        retrieveData({
            curPage: 1,
            pageSize: pageInfo.pageRows
        })
    }, []);
    const handleSubmitError = (obj, oriName) => {
        if (errors[obj]) {
            let errorInfo = errors[obj];
            return oriName + errorInfo.message;
        } else {
            return oriName;
        }
    };

    function base64ToBlob({b64data = '', contentType = '', sliceSize = 512} = {}) {
        return new Promise((resolve, reject) => {
            // 使用 atob() 方法将数据解码
            let byteCharacters = atob(b64data);
            let byteArrays = [];
            for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                let slice = byteCharacters.slice(offset, offset + sliceSize);
                let byteNumbers = [];
                for (let i = 0; i < slice.length; i++) {
                    byteNumbers.push(slice.charCodeAt(i));
                }
                // 8 位无符号整数值的类型化数组。内容将初始化为 0。
                // 如果无法分配请求数目的字节，则将引发异常。
                byteArrays.push(new Uint8Array(byteNumbers));
            }
            let result = new Blob(byteArrays, {
                type: contentType
            })
            result = Object.assign(result, {
                // jartto: 这里一定要处理一下 URL.createObjectURL
                preview: URL.createObjectURL(result),
                name: `图片示例.png`
            });
            resolve(result)
        })
    }

    function onSubmit(data, event) {
        let formValue = getValues();
        console.log(formValue);
        let tmpQueryInfo = ({
            bookName: formValue.bookName,
        })
        retrieveData({
            curPage: 1,
            pageSize: pageInfo.pageRows
        }, tmpQueryInfo)
        event.preventDefault();
    }

    const testWeiXin = () => {
        pageActions.handleWeiInfo("ddd");
    };

    const shareTestBook = (event) => {
        let tmpFile = new File()
        //
        //  console.log(event.target.files[0]);
        //  //const formData = new FormData();
        // // formData.append('file', event.target.files[0]);
        pageActions.shareBookToWeiXinAction(tmpFile, wxChat);
    }
    return (
        <div className={classes.listContentWrapper}>
            <Snackbar open={snackBarStatus} autoHideDuration={3000}
                      TransitionComponent={SlideTransition}
                      onClose={() => {
                          setSnackBarStatus(false)
                      }}
                      anchorOrigin={{vertical: 'top', horizontal: "center"}}>
                <Alert elevation={6} variant="filled" severity="success">
                    {/*<AlertTitle>消息提示</AlertTitle>*/}
                    {message}
                </Alert>
            </Snackbar>
            <Paper className={classes.queryWrapper}>
                <form className={classes.root} noValidate autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
                    <Grid container>
                        <Grid item xs={10}>
                            <TextField id="bookName" name="bookName"
                                       label={handleSubmitError('bookName', '名称')}
                                       classes={{root: classes.textFieldRoot}}
                                       size="small"
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
                <Grid container alignItems={'center'}>
                    {books.map((book, i) => {
                        const getNewChapter = () => {
                            //pageActions.deleteTask(book.idBook);
                            let article = [{
                                "title": "第N章",
                                "thumb_media_id": "HrZ5Z9-SgitEYsVP_1VfgTbNnUu6p9-Q1j_8iL0j21o",
                                "author": "N",
                                "show_cover_pic": 0,
                                "content": "N",
                                "digest": "第N章",
                                "content_source_url": "",
                                "need_open_comment": 0,
                                "only_fans_can_comment": 0
                            }]
                            wxChat.updatePermMaterial('HrZ5Z9-SgitEYsVP_1VfgYR7F5uCwg6fhKV42LZHjYo', article, 0).then(function (data) {
                                console.log(data);
                            });
                        };
                        const deleteBook = () => {
                            //pageActions.analysisWebPage(book.idBook);
                            wxChat.getBatchMaterial("news").then(function (data) {
                                console.log(data);
                            })
                        };
                        const shareBook = () => {
                            //pageActions.shareBookToWeiXinAction(book.idBook,wxChat);
                            let base64 = book.coverBase64.split(',')[1]
                            base64ToBlob({b64data: base64, contentType: 'image/png'}).then(res => {
                                let file = new File([res], book.bookName + ".png", {
                                    type: 'image/png',
                                    lastModified: Date.now()
                                });
                                wxChat.uploadPermMaterialTest('image', file).then(function (data) {
                                    // console.log(data);
                                    let news = {
                                        "articles": [{
                                            "title": book.bookName,
                                            "thumb_media_id": data.media_id,
                                            "author": book.author,
                                            "show_cover_pic": 0,
                                            "content": "mwwpw",
                                            "content_source_url": "",
                                            "need_open_comment": 0,
                                            "only_fans_can_comment": 0
                                        }, {
                                            "title": "第一章",
                                            "thumb_media_id": data.media_id,
                                            "author": "1",
                                            "show_cover_pic": 0,
                                            "content": "1",
                                            "digest": "第一章",
                                            "content_source_url": "",
                                            "need_open_comment": 0,
                                            "only_fans_can_comment": 0
                                        }, {
                                            "title": "第二章",
                                            "thumb_media_id": data.media_id,
                                            "author": "1",
                                            "show_cover_pic": 0,
                                            "content": "2",
                                            "digest": "第二章",
                                            "content_source_url": "",
                                            "need_open_comment": 0,
                                            "only_fans_can_comment": 0
                                        }]
                                    }
                                    wxChat.uploadPermMaterial("news", news);
                                });

                            });
                        }
                        return (
                            <Grid item className={classes.gridItem} key={"parent" + i}>
                                <Card className={classes.cardWrapper}>
                                    <CardHeader className={classes.cardHeaderRoot}
                                                avatar={
                                                    <Book/>
                                                }
                                                title={book.bookName}
                                                subheader={book.isFinished}
                                    />
                                    <div className={classes.details}>
                                        <CardContent className={classes.cardContentRoot}>
                                            <Typography component="span" variant="inherit">
                                                {book.author}
                                            </Typography>
                                            <Typography component="div" variant="caption" display={"block"}
                                                        paragraph={true} className={"paragraphDetail"}
                                                        color="textSecondary">
                                                {book.bookAbstract}
                                            </Typography>
                                        </CardContent>
                                        <CardMedia
                                            className={classes.cover}
                                            component="img"
                                            image={book.coverBase64}
                                            title="封面"
                                        />
                                    </div>

                                    <CardActions disableSpacing className={classes.cardActionRoot}>
                                        <IconButton aria-label="newChapter" onClick={getNewChapter}
                                                    className={classes.cardActionButton}>
                                            <Tooltip title="获取新章节">
                                                <SaveAltIcon fontSize="small" color="primary"/>
                                            </Tooltip>
                                        </IconButton>
                                        <IconButton onClick={shareBook}
                                                    aria-label="publish book" className={classes.cardActionButton}>
                                            <Tooltip title="发布书籍">
                                                <ShareIcon/>
                                            </Tooltip>
                                        </IconButton>
                                        <IconButton style={{marginLeft: 'auto'}} onClick={deleteBook}
                                                    aria-label="delete book" className={classes.cardActionButton}>
                                            <Tooltip title="删除书籍">
                                                <DeleteIcon/>
                                            </Tooltip>
                                        </IconButton>
                                    </CardActions>
                                </Card>
                            </Grid>
                        );
                    })}
                    <Grid item className={classes.gridItem} key={"parent" + 123}>
                        <Card className={classes.cardWrapper}>
                            <CardHeader className={classes.cardHeaderRoot}
                                        avatar={
                                            <Book/>
                                        }
                                        title="test"
                                        subheader="aaa"
                            />
                            <div className={classes.details}>
                                <CardContent className={classes.cardContentRoot}>
                                    <Typography component="span" variant="inherit">
                                        aaa
                                    </Typography>
                                    <Typography component="div" variant="caption" display={"block"} paragraph={true}
                                                className={"paragraphDetail"} color="textSecondary">
                                        bbb
                                    </Typography>
                                </CardContent>
                                <CardMedia
                                    className={classes.cover}
                                    component="img"
                                    image=""
                                    title="封面"
                                />
                            </div>

                            <CardActions disableSpacing className={classes.cardActionRoot}>
                                <IconButton aria-label="newChapter"
                                            className={classes.cardActionButton}>
                                    <Tooltip title="获取新章节">
                                        <SaveAltIcon fontSize="small" color="primary"/>
                                    </Tooltip>
                                </IconButton>
                                <IconButton onClick={shareTestBook}
                                            aria-label="publish book" className={classes.cardActionButton}>
                                    <Tooltip title="发布书籍">
                                        <ShareIcon/>
                                    </Tooltip>
                                </IconButton>
                                <IconButton style={{marginLeft: 'auto'}}
                                            aria-label="delete book" className={classes.cardActionButton}>
                                    <Tooltip title="删除书籍">
                                        <DeleteIcon/>
                                    </Tooltip>
                                </IconButton>
                            </CardActions>
                        </Card>
                    </Grid>
                </Grid>
            </Paper>
        </div>

    );
}


const mapStateToProps = (state) => {
    return {
        book: state.book,
    };
};

const mapDispatchToProps = dispatch => {
    const bookAction = bindActionCreators(bookActions, dispatch);
    return {
        bookAction,
        dispatch,
    };
};

export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(BookPage);

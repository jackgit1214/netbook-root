import React from 'react';
import {makeStyles, withStyles} from '@material-ui/core/styles';
import clsx from 'clsx';
import {
    grayColor,
    primaryColor,
    infoColor,
    successColor,
    warningColor,
    dangerColor,
    roseColor,
    whiteColor,
    blackColor,
    hexToRgb
} from "assets/jss/material-dashboard-react.js";
import Draggable from 'react-draggable';
import DialogTitle from '@material-ui/core/DialogTitle/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent/DialogContent';
import DialogActions from '@material-ui/core/DialogActions/DialogActions';
import Dialog from '@material-ui/core/Dialog/Dialog';
import Button from 'components/CustomButtons/Button.js';
import WarningIcon from '@material-ui/icons/Warning';
import ErrorIcon from '@material-ui/icons/Error';
import InfoIcon from '@material-ui/icons/Info';
import DoneIcon from '@material-ui/icons/Done';
import Paper from '@material-ui/core/Paper/Paper';
import useId from 'react-use-uuid';
import PropTypes from 'prop-types';
import Snackbar from '@material-ui/core/Snackbar';
import Alert from '@material-ui/lab/Alert';
import Box from '@material-ui/core/Box/Box';

/**
 * windowType说明
 * 1、提示窗口，即：alter窗口
 *    1）窗口允许拖动
 *    2）提示内容需要传入，title默认为"提示信息"
 *    3）ese键关闭及窗口背景关闭起作用
 *    4）功能按钮区域只有一个【确定】
 * 2、询问窗口
 *    1）窗口允许拖动
 *    2）内容需要传入，title默认为“提示信息”
 *    3）ese键关闭及窗口背景关闭起作用
 *    4）按钮区域分为两种类型，一种是两个按钮（是、否），一种是三个按钮（是、否、取消）且有回调事件
 *
 *  3、普通窗口
 *     1、所有参数自定义
 *
 * @param props
 * @returns {*}
 * @constructor
 */
CustomDialog.defaultProps = {
    windowType: 1,
    draggable: true,
    escKeyDown: false,
    backdropClick: false,
    title: '提示信息',
    infoType: 'Info',

};

CustomDialog.propTypes = {
    draggable: PropTypes.bool,
    escKeyDown: PropTypes.bool,
    backdropClick: PropTypes.bool,
    windowType: PropTypes.number,
    infoType: PropTypes.oneOf(['Info', 'Error', 'Warning', 'Success']),
};

export default function CustomDialog(props) {

    const dialogId = useId();
    const {
        status,
        handleClose,
        draggable, //是否允许拖动
        children, //内容
        title, //标题
        escKeyDown, //ese是否关闭窗口
        backdropClick, //点击背景，是否关闭窗口
        windowType, //窗口类型，1:提示窗口，21,22、询问窗口，3、普通内容窗口
        infoType,
        infoContent,
        ...rest
    } = props;
    //默认打开状态
    const [open, setOpen] = React.useState(status);

    const [tmpEscKeyDown, setTmpEscKeyDown] = React.useState(escKeyDown);
    const [tmpBackdropClick, setTmpBackdropClick] = React.useState(backdropClick);

    React.useEffect(() => {
        if (windowType == 21 || windowType == 22) {
            setTmpBackdropClick(true);
            setTmpEscKeyDown(true);
        }
        setOpen(status);
    }, [status]);

    //窗口关闭事件
    const handleCloseEvent = () => {
        handleClose(status);
    };
    const handleYes = () => {
        handleClose(false, true);
    };
    const handleNo = () => {
        handleClose(false, false);
    };

    const PaperComponent = (props) => {
        const tmpId = draggable ? dialogId : '';
        return (<Draggable handle={'#customDialog' + tmpId} cancel={'[class*="MuiDialogContent-root"]'}>
                <Paper {...props} />
            </Draggable>
        );
    };

    return (
        <StyleDialog open={open} onClose={handleCloseEvent}
                     aria-labelledby="alert-dialog-title"
                     disableBackdropClick={tmpBackdropClick}
                     disableEscapeKeyDown={tmpEscKeyDown}
                     aria-describedby="alert-dialog-description"
                     PaperComponent={PaperComponent}>
            <StyleDialogTitle disableTypography={true} style={{cursor: clsx(draggable && 'move')}}
                              id={'customDialog' + dialogId}>{title}</StyleDialogTitle>
            <StyleDialogContent>
                {/*<DialogContentText id="alert-dialog-description">*/}

                {/*</DialogContentText>*/}

                <StyleAlert variant="outlined" severity={infoType.toLowerCase()}>{infoContent}</StyleAlert>


            </StyleDialogContent>
            <StyleDialogActions>
                <CustomDialogButton alertType={windowType}
                                    cancelEvent={handleCloseEvent} okEvent={handleYes} noEvent={handleNo}
                />
            </StyleDialogActions>
        </StyleDialog>
    );
}

/**
 * 定义默认按钮的显示，
 * alertType 1为提示信息，21 为交互确认信息，22交互确认，带取消按钮
 * @param props
 * @returns {*}
 * @constructor
 */
function CustomDialogButton(props) {
    const {alertType, okEvent, noEvent, cancelEvent} = props;

    return (
        alertType == 21 ?
            (<div>
                <Button onClick={okEvent} color="primary" size={'sm'}>
                    是
                </Button>
                <Button onClick={noEvent} color="primary" size={'sm'} autoFocus>
                    否
                </Button>
            </div>) : alertType == 22 ? (<div>
                <Button onClick={okEvent} color="primary" size={'sm'}>
                    是
                </Button>
                <Button onClick={noEvent} color="primary" size={'sm'} autoFocus>
                    否
                </Button>
                <Button onClick={cancelEvent} color="primary" size={'sm'} autoFocus>
                    取消
                </Button>
            </div>) : (<div>
                <Button onClick={cancelEvent} color="primary" size={'sm'}>
                    确定
                </Button>
            </div>)
    );
}

CustomDialogButton.defaultProps = {
    alertType: 1,
};

CustomDialogButton.propTypes = {
    noEvent: PropTypes.func,
    cancelEvent: PropTypes.func,
    okEvent: PropTypes.func,
};

const StyleDialog = withStyles({
    paper: {
        margin: 10
    }
})(Dialog);

const StyleDialogTitle = withStyles({
    root: {
        padding: "5px 10px",
        fontSize: "12px !important",
        backgroundColor: grayColor[5],
    }
})(DialogTitle);

const StyleDialogContent = withStyles({
    root: {
        padding: "5px 10px",
    }
})(DialogContent);
const StyleDialogActions = withStyles({
    root: {
        padding: "2px 2px",
        justifyContent: "center"
    }
})(DialogActions);

const StyleAlert = withStyles({
    root: {
        padding: "2px 2px",
        border: "0px",
        boxShadow: "none"
    }
})(Alert);

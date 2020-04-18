import React,{Component,useState} from 'react';
import IconButton from '@material-ui/core/IconButton/IconButton';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import Divider from '@material-ui/core/Divider/Divider';
import List from '@material-ui/core/List/List';
import {mainListItems, secondaryListItems} from './listItems';
import useStyles from '../../style/style';

class Menu extends Component {
    constructor(props){
        super(props);
        //console.log(this.props.state);
    };

    componentDidMount(){

    };

    componentDidUpdate(){

    }
    handleDrawerOpen = () => {
        //useState(true);
    };
    componentWillUnmount(){

    };

    handleDrawerClose = () => {

        console.log("------------------------------");
        console.log(this.state)
        this.props.state=false;
        //useState(false);
    };
    render() {
        console.log(useStyles());
        return (
            <span>
                <div className={useStyles.toolbarIcon}>
                    菜单
                    <IconButton onClick={()=>{this.handleDrawerClose()}}>
                        <ChevronLeftIcon />
                    </IconButton>
                </div>
                <Divider />
                        <List>{mainListItems}</List>
                <Divider />
                    <List>{secondaryListItems}</List>
            </span>
        );
    }
}

export default  Menu;

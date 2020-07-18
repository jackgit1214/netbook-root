import React from "react";
import Chart from './page/Chart'
import Test from './page/Test';
import CustomHome from './page/Home'
import DashboardIcon from '@material-ui/icons/Dashboard';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import PeopleIcon from '@material-ui/icons/People';
import BarChartIcon from '@material-ui/icons/BarChart';
import LayersIcon from '@material-ui/icons/Layers';
import AssignmentIcon from '@material-ui/icons/Assignment';

export const routes = [
    {
        path: "/admin",
        component: CustomHome,
        label: "首页",
        icon: DashboardIcon,
        layout: "/admin"
    },
    {
        path: "/test",
        component: Test,
        label: "测试",
        icon: PeopleIcon,
        layout: "/admin"
        // routes: [
        //     {
        //         path: "/tacos/bus",
        //         component: Bus
        //     },
        //     {
        //         path: "/tacos/cart",
        //         component: Cart
        //     }
        // ]
    },
    {
        path: "/chart",
        component: Chart,
        label: "聊天",
        icon: PeopleIcon,
        layout: "/admin"

    },


];

/*
   路由数据，配置路由及界面
 */
// @material-ui/icons
import Dashboard from '@material-ui/icons/Dashboard';
import Person from '@material-ui/icons/Person';
import LibraryBooks from '@material-ui/icons/LibraryBooks';
import BubbleChart from '@material-ui/icons/BubbleChart';
import Notifications from '@material-ui/icons/Notifications';
import Language from '@material-ui/icons/Language';

// core components/views for Admin layout
import CrawlerPage from 'views/crawler/CrawlerPage';
import WebPage from 'views/crawler/webpage/webPageHandle';
import taskPage from 'views/crawler/task/taskPage';
import BookPage from 'views/book/BookPage'

const dashboardRoutes = [
    {
        path: '/index',
        name: '首页',
        icon: Dashboard,
        component: '',
        layout: '/admin',
    },
    {
        path: '/task',
        name: '抓取任务',
        icon: Language,
        component: taskPage,
        layout: '/admin',
    },
    {
        path: '/webPage',
        name: '抓取页面处理',
        icon: Language,
        component: WebPage,
        layout: '/admin',
    },
    {
        path: '/bookManage',
        name: '书籍管理',
        icon: LibraryBooks,
        component: BookPage,
        layout: '/admin',
    },
    {
        path: '/icons',
        name: 'Icons',
        icon: BubbleChart,
        component: '',
        layout: '/admin',
    },
    {
        path: "/icons111",
        name: "divider",
        component: '',
        icon: BubbleChart,
        layout: "/admin"
    },

    {
        path: '/setup',
        name: '设置',
        icon: Notifications,
        component: '',
        layout: '/admin',
    },
    {
        path: '/user',
        name: '用户',
        icon: Person,
        component: '',
        layout: '/admin',
    },
    {
        path: '/userManager',
        name: '网站用户管理',
        icon: 'content_paste',
        component: '',
        layout: '/admin',
    },
];

export default dashboardRoutes;

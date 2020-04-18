import React from 'react';
import ReactDOM from 'react-dom';
import {createBrowserHistory} from 'history';
import {Route, Router, Switch} from 'react-router-dom';
// core components
import App from 'layouts/App.js';
import 'assets/css/material-dashboard-react.css?v=1.8.0';
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
import * as serviceWorker from './serviceWorker';
import {createMuiTheme, ThemeProvider} from '@material-ui/core/styles';
//配置redux 以及 sagas
import {Provider} from 'react-redux';
import {StoreContext} from 'redux-react-hook';
import ConfigureStore from 'store/store';
import rootSagas from 'store/sagas';

const hist = createBrowserHistory();
const theme = createMuiTheme({
    status: {
        menuOpen: true,
    },
    typography: {
        DialogTitle: {

            backgroundColor: 'red',

        },
    },
    props: {
        MuiButton: {
            size:'small'
        }

    },
    overrides: {
        // MuiDialogTitle: {
        //     root:{
        //         padding:"5px 10px",
        //         backgroundColor:grayColor[5],
        //     }
        // },
        // MuiDialogActions:{
        //   root:{
        //       padding:"2px 2px",
        //       justifyContent:"center"
        //   }
        // },
        // MuiDialogContent: {
        //     root: {
        //         padding: "5px 10px",
        //     }
        // },
        // MuiDialog:{
        //     paper:{
        //       margin:10
        //   }
        // }
    },

});

const store = ConfigureStore();
store.runSaga(rootSagas);

ReactDOM.render(
    <Provider store={store}>
        <Router history={hist}>
            <ThemeProvider theme={theme}>
                <Switch>
                    <Route path="/" component={App}/>
                    {/*<Route path="/rtl" component={RTL} />*/}
                    {/*<Redirect from="/" to="/admin/dashboard" />*/}
                </Switch>

            </ThemeProvider>
        </Router>
    </Provider>
    ,
    document.getElementById('root'),
);
serviceWorker.unregister();

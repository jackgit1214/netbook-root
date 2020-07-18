/*
 * 配置文件
 * 
 */
'use strict'
//import path from 'path';
import fs from 'fs-extra';
import {readFileAsync, writeFileAsync} from './file_util';
import Wechat from './wechat';

const store = require('store')

const config = {
    wechat: {
        appID: 'wxed2ebd39d7b4ba7e',
        appSecret: '74f84a06dc574b91a7162753e226ec6c',
        token: 'wxToken',
        getAccessToken: function () {
            //store.set('user', { name:'Marcus' })
            //return Promise.resolve(store.get("wxToken"));
            //return store.get("wxToken");
            return new Promise(function (resolve, reject) {
                resolve(store.get("wxToken"));
            });
        },
        saveAccessToken: function (data) {
            //return writeFileAsync(wechat_file,data);
            let wxToken = {
                access_token: data.access_token,
                expires_in: data.expires_in
            }
            // return Promise.resolve(function(){
            // 	store.set("wxToken",wxToken);
            // })
            return new Promise(function (resolve, reject) {
                store.set("wxToken", wxToken);
                resolve();
            });

        },
    }
};

export default config;
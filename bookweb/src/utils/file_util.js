'use strict'

import fs from 'fs-extra';
import Promise from 'bluebird';

export function readFileAsync(fpath, encoding) {
    return new Promise(function (resolve, reject) {
        fs.readFile(fpath, encoding, function (err, content) {
            if (err) reject(err);
            else resolve(content);
        });
    });
}

export function writeFileAsync(fpath, content) {
    return new Promise(function (resolve, reject) {
        fs.writeFile(fpath, content, function (err) {
            if (err) reject(err);
            else resolve();
        });
    });
}

// exports.readFileAsync = function(fpath,encoding){
// 	return new Promise(function(resolve,reject){
//
// 		fs.readFile(fpath,encoding,function(err,content){
// 			if(err) reject(err);
// 			else resolve(content);
// 		});
// 	});
// }

// exports.writeFileAsync = function(fpath,content){
// 	return new Promise(function(resolve,reject){
// 		fs.writeFile(fpath,content,function(err){
// 			if(err) reject(err);
// 			else resolve();
// 		});
// 	});
// }
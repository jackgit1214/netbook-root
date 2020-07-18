/*
 * 处理access_token以及和微信交互的逻辑
 */
'use strict'

var fs = require('fs-extra');
var Promise = require('bluebird');
var request = Promise.promisify(require('request'));
//var util = require('./util');

var prefix = 'https://api.weixin.qq.com/cgi-bin/';
const wxApi = {
    accessToken: prefix + 'token?grant_type=client_credential',
    uploadTempMaterial: prefix + 'media/upload?',  //access_token=ACCESS_TOKEN&type=TYPE  上传临时素材
    getTempMaterial: prefix + 'media/get?',        //access_token=ACCESS_TOKEN&media_id=MEDIA_ID 获取临时素材，GET请求
    uploadPermNews: prefix + 'material/add_news?',   //access_token=ACCESS_TOKEN  上传永久图文
    updatePermNews: prefix + 'material/update_news?',   //access_token=ACCESS_TOKEN  修改图文消息
    uploadPermPics: prefix + 'media/uploadimg?',   //access_token=ACCESS_TOKEN  上传永久图片
    uploadPermOther: prefix + 'material/add_material?',   //access_token=ACCESS_TOKEN  上传永久其他素材
    getPermMaterial: prefix + 'material/get_material?',   //access_token=ACCESS_TOKEN 获取永久素材，POST请求
    delPermMaterial: prefix + 'material/del_material?',   //access_token=ACCESS_TOKEN 删除永久素材，POST请求
    batchGetMaterial: prefix + 'material/batchget_material?',//access_token=ACCESS_TOKEN 批量获取素材,POST请求
    menu: {
        create: prefix + 'menu/create?',  //access_token=ACCESS_TOKEN  创建菜单
        get: prefix + 'menu/get?',        //access_token=ACCESS_TOKE  获取菜单,GET请求
        delete: prefix + 'menu/delete?',  //access_token=ACCESS_TOKEN	删除菜单,GET请求
        getInfo: prefix + 'get_current_selfmenu_info?'  //access_token=ACCESS_TOKEN  获取自定义菜单配置接口
    },
    groups: {
        create: prefix + 'groups/create?',  //access_token=ACCESS_TOKEN  创建分组，POST请求
        get: prefix + 'groups/get?',        //access_token=ACCESS_TOKE  查询所有分组,GET请求
        getId: prefix + 'groups/getid?',    //access_token=ACCESS_TOKEN  查询用户所在分组,POST请求
        update: prefix + 'groups/update?',  //access_token=ACCESS_TOKEN  修改分组名,POST请求
        membersUpdate: prefix + 'groups/members/update?',  //access_token=ACCESS_TOKEN  移动用户分组,POST请求
        membersBatchupdate: prefix + 'groups/members/batchupdate?', //access_token=ACCESS_TOKEN  批量移动用户分组,POST请求
        delete: prefix + 'groups/delete?'   //access_token=ACCESS_TOKEN	删除分组,POST请求
    },
    user: {
        updateUserRemark: prefix + 'user/info/updateremark?',  //access_token=ACCESS_TOKEN  修改用户备注名，POST请求
        getUserInfo: prefix + 'user/info?', //access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN  获取用户基本信息，GET请求
        batchGetUserInfo: prefix + 'user/info/batchget?',  //access_token=ACCESS_TOKEN，POST请求
        getUserOpenIds: prefix + 'user/get?',  //access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID，GET请求
    },
    mass: {
        sendall: prefix + 'message/mass/sendall?',  //access_token=ACCESS_TOKEN 群发消息
    }
}

function Wechat(opts) {     //构造函数
    var that = this;
    this.appID = opts.appID;
    this.appSecret = opts.appSecret;
    // this.access_token = opts.access_token;
    // this.expires_in = opts.expires_in;
    this.getAccessToken = opts.getAccessToken;
    this.saveAccessToken = opts.saveAccessToken;
    this.fetchAccessToken();

}

Wechat.prototype.fetchAccessToken = function () {
    var that = this;

    // 如果this上已经存在有效的access_token，直接返回this对象
    if (this.access_token && this.expires_in) {
        if (this.isvalidAccessToken(this)) {
            return Promise.resolve(this);
        }
    }

    this.getAccessToken().then(function (data) {
        try {
            data = JSON.parse(data);
        } catch (e) {
            return that.updateAccessToken();
        }
        if (that.isvalidAccessToken(data)) {
            return Promise.resolve(data);
        } else {
            return that.updateAccessToken();
        }
    }).then(function (data) {
        that.access_token = data.access_token;
        that.expires_in = data.expires_in;
        that.saveAccessToken(JSON.stringify(data));
        return Promise.resolve(data);
    });
}
Wechat.prototype.isvalidAccessToken = function (data) {
    if (!data || !data.access_token || !data.expires_in) return false;
    var access_token = data.access_token;
    var expires_in = data.expires_in;
    var now = new Date().getTime();
    return (now < expires_in) ? true : false;
}

Wechat.prototype.updateAccessToken = function () {
    var appID = this.appID;
    var appSecret = this.appSecret;
    var url = wxApi.accessToken + '&appid=' + appID + '&secret=' + appSecret;

    return new Promise(function (resolve, reject) {
        request({url: url, json: true}).then(function (response) {
            var data = response.body;
            var now = new Date().getTime();
            var expires_in = now + (data.expires_in - 20) * 1000;   //考虑到网络延迟、服务器计算时间,故提前20秒发起请求
            data.expires_in = expires_in;
            resolve(data);
        });
    });
}

Wechat.prototype.uploadTempMaterial = function (type, filepath) {
    var that = this;
    var form = {  //构造表单
        media: fs.createReadStream(filepath)
    }
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.uploadTempMaterial + 'access_token=' + data.access_token + '&type=' + type;
            request({url: url, method: 'POST', formData: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data) {
                    resolve(_data)
                } else {
                    throw new Error('upload temporary material failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.uploadPermMaterialTest = function (type, file) {
    let that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            let url = wxApi.uploadPermOther + 'access_token=' + data.access_token + "&type=" + type;
            let formData = new FormData();
            formData.append("media", file);
            let opts = {
                method: 'POST',
                body: formData
            }
            fetch(url, opts).then(response => response.json())
                .then((data) => {
                    resolve(data);
                })
                .catch(function (err) {
                    reject(err);
                });
            ;

        });
    });
}


/**
 * 修改永久图文消息
 * {
  "media_id":MEDIA_ID,
  "index":INDEX,
  "articles": {
       "title": TITLE,
       "thumb_media_id": THUMB_MEDIA_ID,
       "author": AUTHOR,
       "digest": DIGEST,
       "show_cover_pic": SHOW_COVER_PIC(0 / 1),
       "content": CONTENT,
       "content_source_url": CONTENT_SOURCE_URL
    }
}
 */
Wechat.prototype.updatePermMaterial = function (mediaId, articles, index) {
    let that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            let url = wxApi.updatePermNews + 'access_token=' + data.access_token;
            let opts = {
                method: 'POST',
                url: url,
                json: true,
                body: {
                    "media_id": mediaId,
                    "index": index,
                    "articles": articles
                }
            }

            request(opts).then(function (response) {
                let _data = response.body;
                if (_data) {
                    resolve(_data);
                } else {
                    throw new Error('upload permanent material failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}


Wechat.prototype.uploadPermMaterial = function (type, material) {
    var that = this;
    var form = {}
    var uploadUrl = '';
    if (type === 'pic') uploadUrl = wxApi.uploadPermPics;
    if (type === 'other') uploadUrl = wxApi.uploadPermOther;
    if (type === 'news') {
        uploadUrl = wxApi.uploadPermNews;
        form = material
    } else {
        form.media = fs.createReadStream(material);
    }
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = uploadUrl + 'access_token=' + data.access_token;
            var opts = {
                method: 'POST',
                url: url,
                json: true
            }
            if (type === 'news') {
                opts.body = form;
            } else {
                opts.formData = form;
            }
            request(opts).then(function (response) {
                var _data = response.body;
                if (_data) {
                    resolve(_data);
                } else {
                    throw new Error('upload permanent material failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.getBatchMaterial = function (type) {
    let that = this;
    let getUrl = wxApi.batchGetMaterial;

    let form = {  //构造表单
        "type": type,
        "offset": 0,
        "count": 20
    }
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            let url = getUrl + 'access_token=' + data.access_token;
            let myHeaders = new Headers();
            myHeaders.append('Content-Type', 'application/json');
            request({url: url, json: form, method: "post"}).then(function (response) {
                //console.log(response);
                let _data = response.body;
                resolve(_data);
                // if(_data.menu){
                // 	resolve(_data.menu);
                // }else{
                // 	throw new Error('get menu failed!');
                // }
            }).catch(function (err) {
                reject(err);
            });

            //resolve(url)
        });
    });
}

Wechat.prototype.getMaterial = function (mediaId, permanent) {
    var that = this;
    var getUrl = permanent ? wxApi.getPermMaterial : wxApi.getTempMaterial;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = getUrl + 'access_token=' + data.access_token;
            if (!permanent) url += '&media_id=' + mediaId;
            resolve(url)
        });
    });
}

Wechat.prototype.delMaterial = function (mediaId) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.delPermMaterial + 'access_token=' + data.access_token;
            var form = {media_id: mediaId}
            request({url: url, method: 'POST', formData: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve();
                } else {
                    throw new Error('delete permanent material failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.replay = function () {
    var content = this.body;
    var message = this.weixin;

    //util tpl有错
    //var xml = util.tpl(content,message);

    this.status = 200;
    this.type = 'application/xml';
    //this.body = xml
    this.body = "";
}

//创建菜单
Wechat.prototype.createMenu = function (menu) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.menu.create + 'access_token=' + data.access_token;
            request({url: url, method: 'POST', body: menu, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve(_data.errmsg);
                } else {
                    throw new Error('create menu failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}


//获取菜单
Wechat.prototype.getMenu = function () {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.menu.get + 'access_token=' + data.access_token;
            request({url: url, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.menu) {
                    resolve(_data.menu);
                } else {
                    throw new Error('get menu failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

//删除菜单
Wechat.prototype.deleteMenu = function () {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.menu.delete + 'access_token=' + data.access_token;
            request({url: url, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve(_data.errmsg);
                } else {
                    throw new Error('delete menu failed!');
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}


/**************************用户分组Groups******************************/

Wechat.prototype.createGroup = function (name) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.groups.create + 'access_token=' + data.access_token;
            var form = {
                group: {
                    name: name
                }
            };
            request({method: 'POST', url: url, body: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.group) {
                    resolve(_data.group);
                } else {
                    throw new Error('create group failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.getGroups = function (name) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.groups.get + 'access_token=' + data.access_token;
            request({url: url, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.groups) {
                    resolve(_data.groups);
                } else {
                    throw new Error('get groups failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

var _deleteGroup = function (access_token, id) {
    var url = wxApi.groups.delete + 'access_token=' + access_token;
    var form = {
        group: {
            id: id
        }
    };
    return new Promise(function (resolve, reject) {
        request({method: 'POST', url: url, body: form, json: true}).then(function (response) {
            var _data = response.body;
            if (_data.errcode === 0) {
                resolve('ok');
            } else {
                throw new Error('delete group:' + id + ' failed: ' + _data.errmsg);
            }
        }).catch(function (err) {
            reject(err);
        });
    });
}

Wechat.prototype.deleteGroups = function (ids) {     //一个或多个id组成的数组
    var that = this;
    that.fetchAccessToken().then(function (data) {
        var queue = [];
        for (var i = 0; i < ids.length; i++) {
            queue.push(_deleteGroup(data.access_token, ids[i]));
        }
        Promise.all(queue).then(function (data) {
            console.log('data:' + data);
        }).catch(function (err) {
            console.log(err)
        })
    });
}


//通过用户的OpenID查询其所在的GroupID
Wechat.prototype.getGroupidByOpenid = function (openid) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.groups.getid + 'access_token=' + data.access_token;
            var form = {
                openid: openid
            };
            request({method: 'POST', url: url, body: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.groupid) {
                    resolve(_data.groupid);
                } else {
                    throw new Error('get get GroupId by OpenId failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.updateGroup = function (groupid, name) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.groups.update + 'access_token=' + data.access_token;
            var form = {
                group: {
                    id: groupid,
                    name: name
                }
            };
            request({method: 'POST', url: url, body: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve(_data.errmsg);
                } else {
                    throw new Error('update group failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.moveUsersToGroup = function (openid, to_groupid) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = '';
            var form = {}
            if (openid && !Array.isArray(openid)) {   //单个用户分组
                url = wxApi.groups.membersUpdate + 'access_token=' + data.access_token;
                form = {
                    openid: openid,
                    to_groupid: to_groupid
                };
            } else if (Array.isArray(openid)) {
                url = wxApi.groups.membersBatchupdate + 'access_token=' + data.access_token;
                form = {
                    openid_list: openid,
                    to_groupid: to_groupid
                };
            }
            request({method: 'POST', url: url, body: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve(_data.errmsg);
                } else {
                    throw new Error('update group failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.updateUserRemark = function (openid, remark) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.user.updateUserRemark + 'access_token=' + data.access_token;
            var form = {
                openid: openid,
                remark: remark
            };
            request({method: 'POST', url: url, body: form, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve(remark);
                } else {
                    throw new Error('update user remark failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

//获取单个或一批用户信息
Wechat.prototype.fetchUserInfo = function (open_id, lang) {
    var that = this;
    var lang = lang || 'zh_CN';
    var url = '';
    var opts = {}
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {

            if (open_id && !Array.isArray(open_id)) {   //单个获取
                url = wxApi.user.getUserInfo + 'access_token=' + data.access_token + '&openid=' + open_id + '&lang=' + lang;
                opts = {
                    url: url,
                    json: true
                }
            } else if (open_id && Array.isArray(open_id)) {
                url = wxApi.user.batchGetUserInfo + 'access_token=' + data.access_token;
                var user_list = [];
                for (var i = 0; i < open_id.length; i++) {
                    user_list.push({
                        openid: open_id[i],
                        lang: lang
                    });
                }
                opts = {
                    method: 'POST',
                    url: url,
                    body: {
                        user_list: user_list
                    },
                    json: true
                }
            }
            request(opts).then(function (response) {
                var _data = response.body;
                if (!_data.errcode) {
                    resolve(_data);
                } else {
                    throw new Error('fetch user info failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.getUserOpenIds = function (next_openid) {
    var that = this;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.user.getUserOpenIds + 'access_token=' + data.access_token;
            if (next_openid) url += '&next_openid=' + next_openid;
            request({url: url, json: true}).then(function (response) {
                var _data = response.body;
                if (!_data.errcode) {
                    resolve(_data);
                } else {
                    throw new Error('get user openIds failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}

Wechat.prototype.massSendMsg = function (type, message, groupid) {
    var that = this;
    var msg = {
        filter: {},
        msgtype: type
    }
    if (!groupid) {
        msg.filter.is_to_all = true
    } else {
        msg.filter.is_to_all = false;
        msg.filter.group_id = groupid;
    }
    msg[type] = message;
    return new Promise(function (resolve, reject) {
        that.fetchAccessToken().then(function (data) {
            var url = wxApi.mass.sendall + 'access_token=' + data.access_token;
            request({method: 'POST', url: url, body: msg, json: true}).then(function (response) {
                var _data = response.body;
                if (_data.errcode === 0) {
                    resolve(_data);
                } else {
                    throw new Error('send mass message failed: ' + _data.errmsg);
                }
            }).catch(function (err) {
                reject(err);
            });
        });
    });
}
module.exports = Wechat;
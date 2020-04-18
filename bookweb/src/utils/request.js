import axios from 'axios'
const baseUrl = 'http://localhost:8080/';

const getUrl = (url) => {
    if (url==undefined||url==null||url=="") {
        return baseUrl;
    }
    return baseUrl.concat(url);
};

const requestPost = (url,params) =>{
    let isOk;

    return new Promise((resolve, reject) => {

        let myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/json');
        let myInit = {
            body: JSON.stringify(params),
            method: 'POST',
            headers: myHeaders,
            mode: 'cors' };
        fetch(url, myInit)
            .then((response) => {
                if (response.ok) {
                    isOk = true;
                } else {
                    isOk = false;
                }
                return response.json();
            })
            .then((responseData) => {
                resolve(responseData);
            })
            .catch((error) => {
                reject(error);
            });
    });
}

const requestGet = (url,params) =>{
    let isOk;

     return new Promise((resolve, reject) => {

        let myHeaders = new Headers();
        myHeaders.append('Content-Type', 'application/json');
        let myInit = { method: 'GET',
            headers: myHeaders,
            mode: 'cors' };

        fetch(url, myInit)
            .then((response) => {
               // console.log(response);
                if (response.ok) {
                    isOk = true;
                } else {
                    isOk = false;
                }
                return response.json();
            })
            .then((responseData) => {
               //console.log(responseData);
                resolve(responseData);

            })
            .catch((error) => {
                reject(error);
            });
    });
}

const request = (url, method, params) => {
    let isOk;
    return new Promise((resolve, reject) => {

        var formdata = new FormData();
        //formdata.append("queryContent","123123");
        //params = {"name":"john","phone":"12345"};
        Object.keys(params).forEach((key) => {
            console.log(key);
            formdata.append(key, params[key]);
        });

        // console.log(params);
        console.log(formdata);

        fetch(getUrl(url), {
            method,
            headers: {
               // 'Content-Type':'application/x-www-form-urlencoded'
            },
            body:formdata
        })
            .then((response) => {
                if (response.ok) {
                    isOk = true;
                } else {
                    isOk = false;
                }
                return response.json();
            })
            .then((responseData) => {
                if (isOk) {
                    resolve(responseData.resultData);
                } else {
                    reject(responseData);
                }
            })
            .catch((error) => {
                reject(error);
            });
    });
};


const jsonRequest = (url, method, params) => {
    let isOk;
    //console.log("==============="+getUrl(url)+"========================")
    return new Promise((resolve, reject) => {
        fetch(getUrl(url), {
            method,
            headers: {
                'Content-Type': "application/json"
            },
            body:JSON.stringify(params)
        })
            .then((response) => {
                if (response.ok) {
                    isOk = true;
                } else {
                    isOk = false;
                }
                //console.log(response.json());
                return response.json();
            })
            .then((responseData) => {
               // if (isOk) {
                    resolve(responseData);
               // } else {
                //    reject(responseData);
               // }
            })
            .catch((error) => {
                reject(error);
            });
    });
};

export {
    request,
    jsonRequest,
    requestGet,
    requestPost
};

// 这份文件就是用来做拦截的
import axios from 'axios'
import router from '@/router'

// 创建一个单例（实例）
const instance = axios.create({
    baseURL: "http://localhost:8880",
    timeout: 4000
})

// 拦截器 - 请求拦截
instance.interceptors.request.use(config => {
    // 部分接口需要拿到token
    let token = localStorage.getItem('token');
    if (token) {
        config.headers = {
            'Authorization': 'Bearer ' + token
        }
    }
    return config;
}, err => {
    return Promise.reject(err)
});


// 拦截器 - 响应拦截
instance.interceptors.response.use(res => {
    if(res.data.code!=200){
        if(res.data.code==401){
            router.push("/")
        }
    }
    return res;
}, err => {
    return Promise.reject(err)
});

// 整体导出
export default instance;

import request from "@/request"

export const loginApi = (UserInfo) => request.post('/user/login', UserInfo);

export const listStudentApi = () => request.get('/user/liststu');

export const adduserApi = (UserInfo) => request.post('/user/adduser', UserInfo);
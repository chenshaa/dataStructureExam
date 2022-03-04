import request from "@/request"

export const listQuesApi = (examId) => request.get('/ques/listques/'+examId);

export const listAllQuesApi = () => request.get('/ques/listallques');

export const getQuesByIdApi = (quesId) => request.get('/ques/getquesbyid/'+quesId);

export const linkQuestionApi = (params) => request.post('/ques/linkques', params);

export const removeQuestionApi = (params) => request.post('/ques/removeques', params);

export const addQuestionApi = (params) => request.post('/ques/addques', params);
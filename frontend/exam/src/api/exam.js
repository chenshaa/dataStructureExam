import request from "@/request"

export const listExamApi = () => request.get('/exam/listexam');

export const addExamApi = (ExamInfo) => request.post('/exam/addexam', ExamInfo);

export const setExamStartApi = (examId) => request.get('/exam/setexamstart/'+examId);

export const setExamEndApi = (examId) => request.get('/exam/setexamend/'+examId);
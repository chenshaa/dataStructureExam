import request from "@/request"

export const listStudentInExamApi = (examId) => request.get('/paper/liststu/'+examId);

export const addPaperApi = (params) => request.post('/paper/addpaper', params);
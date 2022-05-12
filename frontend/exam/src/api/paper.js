import request from "@/request"

export const listStudentInExamApi = (examId) => request.get('/paper/liststu/' + examId);

export const addPaperApi = (params) => request.post('/paper/addpaper', params);

export const correctProgressApi = (examId) => request.get('/paper/correctProgress/' + examId);

export const quesCorrectProgressApi = (examId, quesId) => request.get('/paper/quesCorrectProgress/' + examId + '/' +
    quesId);

export const getAnswerListApi = (examId, quesId) => request.get('/paper/getAnswerList/' + examId + '/' +
    quesId);

export const getAnswerByIdApi = (answerId) => request.get('/paper/getAnswerById/' + answerId);

export const updateScoreApi = (params) => request.post('/paper/updatescore', params);

export const autoCorrectApi = (examId) => request.get('/paper/autocorrect/' + examId);

export const calculateScoreApi = (examId) => request.get('/paper/calculateScore/' + examId);

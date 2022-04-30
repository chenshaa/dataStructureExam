<template>
    <div>
        <!--空页面-->
        <div v-if='emptyPageVisible'>
            <el-empty description="请选择题目">
                <el-button type="primary" @click='startButtFn'>按钮</el-button>
            </el-empty>
        </div>

        <!--批改页面-->
        <div v-if='!emptyPageVisible'>
            <el-page-header @back="goBack" content="批改试卷"></el-page-header>

            <el-divider></el-divider>

            <el-progress :text-inside="true" :stroke-width="26" :percentage="70"></el-progress>

            <el-row :gutter="20" style="margin-top: 20px;">
                <el-col :span="12">
                    <div class="">
                        <!--左半屏-->
                        <el-descriptions title="当前题目" border>
                            <el-descriptions-item label="学生姓名">{{currAnswer.userId}}</el-descriptions-item>
                            <el-descriptions-item label="分数">
                                <el-input v-model="newScore" placeholder="未评分">
                                    <el-button slot="append" icon="el-icon-caret-top" class='KeyBordShowBtn'>
                                    </el-button>
                                </el-input>
                            </el-descriptions-item>
                        </el-descriptions>

                        <div>
                            <FloatBox :value="newScore" @keyChange="keyChange" @submit='numSubmit'
                                v-if="showKeybord">
                            </FloatBox>
                        </div>

                        <!--控制按钮-->
                        <el-card>
                            <div>
                                <el-button-group style="float: left;">
                                    <el-button type="primary" icon="el-icon-arrow-left" @click="previousPage">上一页
                                    </el-button>
                                    <el-button type="primary" @click='nextPage'>下一页<i
                                            class="el-icon-arrow-right el-icon--right"></i>
                                    </el-button>
                                </el-button-group>
                                <el-button type="primary" style="float: right;" @click='updateScore'>上传<i
                                        class="el-icon-upload el-icon--right"></i></el-button>
                                <div style="clear: both;"></div>
                            </div>
                        </el-card>

                        <!--显示批改题目-->
                        <el-card>
                            <el-empty description="Oops,好像没有提交哦!" v-if='isImpty'></el-empty>
                            <div v-html="compileMarkDown(currAnswer.answer)" v-show='!isImpty'></div>
                        </el-card>


                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="">
                        <!--右半屏-->
                        <el-collapse v-model="activeNames">
                            <el-collapse-item title="批改进度" name="1">
                                <AnswerSheet :data="answerList" @keyChange="SelectChange" :set='newPage'></AnswerSheet>
                            </el-collapse-item>
                            <el-collapse-item title="题目信息" name="2">
                                <QuesShowBox :quesInfo='quesInfo'></QuesShowBox>
                            </el-collapse-item>
                        </el-collapse>

                    </div>
                </el-col>

            </el-row>
        </div>

        <el-dialog title="选择题目" :visible.sync="dialogFormVisible">
            <el-form size="small">
                <el-form-item label="考试场次:">

                    <el-select value-key="examId" v-model="examSelectValue" @change="examSelectChangeFn" filterable
                        placeholder="请选择">
                        <el-option v-for="item in examSelectItems" :key="item.examId" :label="item.examName"
                            :value="item.examId">
                        </el-option>
                    </el-select>

                </el-form-item>

                <el-form-item label="选择题目">
                    <el-select v-model="quesSelectValue" @change="quesSelectChangeFn" filterable placeholder="请选择"
                        style="max-width: 100%;">
                        <el-option v-for="item in quesSelectItems" :key="item.questionId" :label="item.questionText"
                            :value="item.questionId">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-descriptions title="批改进度" border></el-descriptions>
                <el-progress :text-inside="true" :stroke-width="26" :percentage="quesCorrectProgress"></el-progress>

                <QuesShowBox :quesInfo='quesInfo'></QuesShowBox>

            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false;">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import QuesShowBox from '@/components/QuesShowBox.vue'
    import FloatBox from '@/components/FloatBox.vue'
    import AnswerSheet from '@/components/AnswerSheet.vue'

    import {
        listExamApi
    } from '@/api/exam.js'

    import {
        listQuesApi,
        getQuesByIdApi,
    } from '@/api/ques.js'

    import {
        quesCorrectProgressApi,
        getAnswerListApi,
        updateScoreApi,
    } from '@/api/paper.js'

    import showdown from "showdown";
    var converter = new showdown.Converter();
    converter.setOption("tables", true);

    export default {
        data() {
            return {
                dialogFormVisible: false,
                examSelectItems: [],
                examSelectValue: '',
                emptyPageVisible: true,
                quesSelectValue: '',
                quesSelectItems: [],
                quesInfo: {},
                quesCorrectProgress: 70,
                answerList: [],
                editAnswer: {},
                activeNames: ['1', '2'],
                showKeybord: false,
                currAnswer: {},
                newPage: 0,
                isImpty: false,
                newScore:'',
                
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {

            },
            examSelectChangeFn(val) {
                listQuesApi(val).then(res => {
                    this.quesSelectItems = res.data.data;
                });
            },
            startButtFn() {
                this.dialogFormVisible = true;
                listExamApi().then(res => {
                    var table = res.data.data;
                    this.examSelectItems = table;
                });
            },
            quesSelectChangeFn(val) {
                //题目信息
                getQuesByIdApi(val).then(res => {
                    this.quesInfo = res.data.data;
                    console.log(res);
                });
                //批改进度
                quesCorrectProgressApi(this.examSelectValue, val).then(res => {
                    this.quesCorrectProgress = res.data.data * 100;
                });
            },
            compileMarkDown(val) {
                if (val == null) {
                    this.isImpty = true;

                } else {
                    this.isImpty = false;

                }
                return converter.makeHtml(val);
            },
            save() {

                getAnswerListApi(this.examSelectValue, this.quesSelectValue).then(res => {
                    console.log(this.dialogFormVisible);
                    this.answerList = res.data.data;
                    this.emptyPageVisible = false;
                    this.dialogFormVisible = false;

                }).catch(err => {
                    console.log(err);
                });

            },
            goBack() {

            },
            keyChange(val) {
                this.newScore = val;
            },
            numSubmit(val) {

            },
            SelectChange(val) {
                this.newScore=this.currAnswer.score;
                this.currAnswer = this.answerList[val];
                this.newScore=this.currAnswer.score;
                this.newPage = val;
            },
            previousPage() {
                this.newPage = this.newPage == 0 ? 0 : this.newPage - 1;
            },
            nextPage() {
                this.newPage = this.newPage == this.answerList.length - 1 ? this.newPage : this.newPage + 1;
            },
            updateScore() {
                updateScoreApi({
                    'quesId': this.quesSelectValue,
                    'examId': this.examSelectValue,
                    'userId': this.currAnswer.userId,
                    'collectId': this.currAnswer.collectId,
                    'newScore': this.newScore
                }).then(res => {
                    this.$message({
                        message: '恭喜你，这是一条成功消息',
                        type: 'success'
                    });
                });
            }

        },
        components: {
            QuesShowBox,
            FloatBox,
            AnswerSheet,
        }
    }
</script>

<style>

</style>

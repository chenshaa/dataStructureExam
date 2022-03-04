<template>
    <div class="managePaper">

        <div style="margin: 10px 0">
            <el-form :inline="true">
                <el-form-item label="当前选择的考试场次:">

                    <el-select value-key="examId" v-model="selectValue" @change="selectChangeFn" filterable
                        placeholder="请选择">
                        <el-option v-for="item in selectItems" :key="item.examId" :label="item.examName"
                            :value="item.examId">
                        </el-option>
                    </el-select>

                </el-form-item>
            </el-form>

            <el-divider></el-divider>

            <el-input class="searchInput ml-5" placeholder="搜索题目" v-model="input" clearable></el-input>
            <el-button class="ml-5" icon="el-icon-search" circle></el-button>

            <el-divider direction="vertical"></el-divider>

            <el-button type="primary" @click="userAdd">添加题目<i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" slot="reference">批量删除题目<i class="el-icon-remove-outline"></i></el-button>
        </div>

        <!--   table    -->

        <div style="margin: 10px 0">

            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                @selection-change="handleSelectionChange">

                <!--   展开表格    -->
                <el-table-column type="expand">
                    <template slot-scope="props">

                        <div class="expandTable">
                            <el-descriptions title="题目信息" border :column="4">
                                <el-descriptions-item label="题目ID">{{props.row.questionId}}</el-descriptions-item>
                                <el-descriptions-item label="关联考试">{{props.row.questionLink}}</el-descriptions-item>

                                <el-descriptions-item label="分值" style='width: 200px;'>{{props.row.questionScore}}
                                </el-descriptions-item>
                                <el-descriptions-item label="题型">{{props.row.questionType}}</el-descriptions-item>
                            </el-descriptions>

                            <el-descriptions title="选项信息" border :column="3">
                                <el-descriptions-item label="选项1">
                                    {{props.row.questionOpinion1}}
                                </el-descriptions-item>
                                <el-descriptions-item label="选项2">
                                    {{props.row.questionOpinion2}}
                                </el-descriptions-item>
                                <el-descriptions-item label="选项3">
                                    {{props.row.questionOpinion3}}
                                </el-descriptions-item>
                                <el-descriptions-item label="选项4">
                                    {{props.row.questionOpinion4}}
                                </el-descriptions-item>
                                <el-descriptions-item label="选项5">
                                    {{props.row.questionOpinion5}}
                                </el-descriptions-item>
                                <el-descriptions-item label="     ">

                                </el-descriptions-item>
                                <el-descriptions-item label="答案">
                                    <el-tag size="small">{{props.row.questionRightChoice}}</el-tag>
                                </el-descriptions-item>

                            </el-descriptions>

                            <el-descriptions title="题目" border :column="3">
                            </el-descriptions>

                            <div v-html="compileMarkDown(props.row.questionText)"></div>


                        </div>
                    </template>
                </el-table-column>

                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="questionId" label="ID" width="200"></el-table-column>
                <el-table-column prop="questionText" label="内容" width="160"></el-table-column>
                <el-table-column prop="questionScore" label="分值" width="160"></el-table-column>
                <el-table-column prop="questionType" label="题型" width="160"></el-table-column>
                <el-table-column label="操作" align="left">
                    <template slot-scope="scope">

                        <el-popconfirm class="ml-5" confirm-button-text='确定' cancel-button-text='我再想想'
                            icon="el-icon-info" icon-color="red" title="您确定删除吗？" @confirm="del(scope.row.questionId)">
                            <el-button type="danger" slot="reference">移除 <i class="el-icon-remove-outline"></i>
                            </el-button>

                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>

        </div>

        <!--   dialog    -->

        <div>
            <el-dialog title="添加新题目" :visible.sync="dialogFormVisible" width="60%" :close-on-click-modal=false
                :close-on-press-escape=false>
                <el-form label-width=auto size="small">

                    <el-form-item label="选择题目">
                        <el-select v-model="quesSelectValue" @change="quesSelectChangeFn" filterable placeholder="请选择"
                            style="max-width: 100%;">
                            <el-option v-for="item in quesSelectItems" :key="item.questionId" :label="item.questionText"
                                :value="item.questionId">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-descriptions title="题目信息" border :column="4">
                        <el-descriptions-item label="题目ID">{{quesInfo.questionId}}</el-descriptions-item>
                        <el-descriptions-item label="关联考试">{{quesInfo.questionLink}}</el-descriptions-item>

                        <el-descriptions-item label="分值" style='width: 200px;'>{{quesInfo.questionScore}}
                        </el-descriptions-item>
                        <el-descriptions-item label="题型">{{quesInfo.questionType}}</el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions title="选项信息" border :column="3">
                        <el-descriptions-item label="选项1">
                            {{quesInfo.questionOpinion1}}
                        </el-descriptions-item>
                        <el-descriptions-item label="选项2">
                            {{quesInfo.questionOpinion2}}
                        </el-descriptions-item>
                        <el-descriptions-item label="选项3">
                            {{quesInfo.questionOpinion3}}
                        </el-descriptions-item>
                        <el-descriptions-item label="选项4">
                            {{quesInfo.questionOpinion4}}
                        </el-descriptions-item>
                        <el-descriptions-item label="选项5">
                            {{quesInfo.questionOpinion5}}
                        </el-descriptions-item>
                        <el-descriptions-item label="     ">

                        </el-descriptions-item>
                        <el-descriptions-item label="答案">
                            <el-tag size="small">{{quesInfo.questionRightChoice}}</el-tag>
                        </el-descriptions-item>

                    </el-descriptions>

                    <el-descriptions title="题目" border :column="3">
                    </el-descriptions>

                    <div v-html="compileMarkDown(quesInfo.questionText)"></div>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false;editMode=false">取 消</el-button>
                    <el-button type="primary" @click="save">确 定</el-button>
                </div>

            </el-dialog>
        </div>
    </div>
</template>

<script>
    import {
        listExamApi
    } from '@/api/exam.js'

    import {
        listQuesApi,
        listAllQuesApi,
        getQuesByIdApi,
        linkQuestionApi,
        removeQuestionApi,

    } from '@/api/ques.js'

    import showdown from "showdown";
    var converter = new showdown.Converter();
    converter.setOption("tables", true);

    export default {
        data() {
            return {
                input: '',
                tableData: [],
                dialogFormVisible: false,
                editMode: false,
                multipleSelection: [],
                form: {

                },
                options: [],
                selectItems: [],
                selectValue: '',
                quesSelectItems: [],
                quesSelectValue: '',
                quesInfo: {},

            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                listExamApi().then(res => {
                    var table = res.data.data;
                    this.selectItems = table;
                });
            },
            save() {
                linkQuestionApi({
                    param1: this.quesSelectValue,
                    param2: this.selectValue
                }).then(res => {
                    if (res.data.code == 200) {
                        //添加成功
                        this.$message({
                            message: '添加成功',
                            type: 'success',
                            showClose: true
                        });
                        this.dialogFormVisible = false;
                        this.selectChangeFn(this.selectValue);
                    } else {
                        //添加失败
                        this.$message({
                            message: '添加失败：' + res.data.msg + '  code' + res.data.code,
                            type: 'error',
                            showClose: true
                        });
                    }
                });
            },
            userAdd() {
                this.dialogFormVisible = true;
                listAllQuesApi().then(res => {
                    this.quesSelectItems = res.data.data;

                });
            },
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            del(id) {
                removeQuestionApi({
                    param1: id,
                    param2: this.selectValue
                }).then(res => {
                    if (res.data.code == 200) {
                        //添加成功
                        this.$message({
                            message: '移除成功',
                            type: 'success',
                            showClose: true
                        });
                        this.selectChangeFn(this.selectValue);
                    } else {
                        //添加失败
                        this.$message({
                            message: '移除失败：' + res.data.msg + '  code' + res.data.code,
                            type: 'error',
                            showClose: true
                        });
                    }
                });

            },
            selectChangeFn(selVal) {
                listQuesApi(selVal).then(res => {
                    var table = res.data.data;
                    this.tableData = table;
                });
            },
            quesSelectChangeFn(selVal) {
                getQuesByIdApi(selVal).then(res => {
                    this.quesInfo = res.data.data;
                    console.log(res);
                });
            },
            compileMarkDown(val) {
                return converter.makeHtml(val);
            },

        }
    }
</script>

<style>
    .managePaper {
        margin: 10px 0;
    }

    .searchInput {
        width: 200px;
    }

    .el-select-dropdown {
        max-width: 50%;
    }

    .expandTable {
        margin: 40px;
    }
</style>

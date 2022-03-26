<template>
    <div class="managePaper">

        <div style="margin: 10px 0">


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

                            <QuesShowBox :quesInfo='props.row'></QuesShowBox>

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

                    <el-descriptions title="题目信息" border :column="3">

                        <el-descriptions-item label="分值">
                            <el-input v-model="form.questionScore"></el-input>
                        </el-descriptions-item>

                        <el-descriptions-item label="题型">
                            <el-select value-key="examId" v-model="form.questionType" filterable placeholder="可以为空">
                                <el-option v-for="item in quesTypeItems" :key="item.value" :label="item.name"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-descriptions-item>

                        <el-descriptions-item label="关联考试">
                            <el-select value-key="examId" v-model="form.questionLink" filterable placeholder="可以为空">
                                <el-option v-for="item in selectItems" :key="item.examId" :label="item.examName"
                                    :value="item.examId">
                                </el-option>
                            </el-select>
                        </el-descriptions-item>

                    </el-descriptions>

                    <el-descriptions title="选项信息" border :column="3">
                        <el-descriptions-item label="选项1">
                            <el-input v-model="form.questionOpinion1"></el-input>
                        </el-descriptions-item>
                        <el-descriptions-item label="选项2">
                            <el-input v-model="form.questionOpinion2"></el-input>
                        </el-descriptions-item>
                        <el-descriptions-item label="选项3">
                            <el-input v-model="form.questionOpinion3"></el-input>
                        </el-descriptions-item>
                        <el-descriptions-item label="选项4">
                            <el-input v-model="form.questionOpinion4"></el-input>
                        </el-descriptions-item>
                        <el-descriptions-item label="选项5">
                            <el-input v-model="form.questionOpinion5"></el-input>
                        </el-descriptions-item>
                        <el-descriptions-item label="     ">

                        </el-descriptions-item>
                        <el-descriptions-item label="答案">
                            <el-input v-model="form.questionRightChoice"></el-input>
                        </el-descriptions-item>

                    </el-descriptions>

                    <el-descriptions title="题目" border :column="3">
                    </el-descriptions>

                    <mavon-editor v-model="form.questionText" />

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
    import QuesShowBox from '@/components/QuesShowBox.vue'

    import {
        listExamApi
    } from '@/api/exam.js'

    import {
        listQuesApi,
        listAllQuesApi,
        getQuesByIdApi,
        linkQuestionApi,
        removeQuestionApi,
        addQuestionApi,
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
                quesTypeItems: [{
                        name: '单选题',
                        value: '0'
                    },
                    {
                        name: '多选题',
                        value: '1'
                    },
                    {
                        name: '不定项选择题',
                        value: '2'
                    },
                    {
                        name: '填空题',
                        value: '3'
                    },
                    {
                        name: '判断题',
                        value: '4'
                    },
                    {
                        name: '简答题',
                        value: '5'
                    },
                    {
                        name: '综合题',
                        value: '6'
                    }
                ],
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {

                listAllQuesApi().then(res => {
                    var table = res.data.data;
                    this.tableData = table;
                });
            },
            save() {

                addQuestionApi(this.form).then(res => {
                    if (res.data.code == 200) {
                        //添加成功
                        this.$message({
                            message: '添加成功',
                            type: 'success',
                            showClose: true
                        });
                        this.dialogFormVisible = false;
                        this.load();
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


        },
        components: {
            QuesShowBox,
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

    .expandTable {
        margin: 40px;
    }
</style>

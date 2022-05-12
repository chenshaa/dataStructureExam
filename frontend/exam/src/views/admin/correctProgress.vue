<template>
    <div class="manageStu">

        <div style="margin: 10px 0">

            <el-form :inline="true">
                <el-form-item label="当前选择的考试场次:">

                    <el-select value-key="examId" v-model="examSelectValue" @change="selectChangeFn" filterable
                        placeholder="请选择">
                        <el-option v-for="item in examSelectItem" :key="item.examId" :label="item.examName"
                            :value="item.examId">
                        </el-option>
                    </el-select>

                </el-form-item>
            </el-form>

            <el-divider></el-divider>

            <el-input class="searchInput ml-5" placeholder="请输入内容" v-model="searchInput" clearable></el-input>
            <el-button class="ml-5" icon="el-icon-search" circle></el-button>

            <el-button type="primary" @click="autoCorrect">自动批改主观题 <i class="el-icon-s-claim"></i></el-button>
            <el-button type="primary" @click="setScore">计算得分 <i class="el-icon-s-check"></i></el-button>
        </div>

        <div style="margin: 10px 0">

            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="userId" label="ID" width="200"></el-table-column>
                <el-table-column prop="userNickname" label="昵称" width="160"></el-table-column>
                <el-table-column prop="answerRatio" label="答题比例" width="80"></el-table-column>
                <el-table-column prop="correctionProgress" label="批改比例" width="80"></el-table-column>
                <el-table-column prop="fullScore" label="满分" width="80"></el-table-column>
                <el-table-column prop="score" label="得分" width="80"></el-table-column>
                <el-table-column label="操作" align="left">
                    <template slot-scope="scope">
                        <el-button type="primary" @click="handleCorrect(scope.row)">批改 <i
                                class="el-icon-edit-outline"></i>
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

        </div>

        <!--结果显示dialog-->
        <div>
            <el-dialog title="结果" :visible.sync="dialogFormVisible" width="30%">
                <div style="overflow-x:auto ;overflow-y: auto;">
                    <p>{{result}}</p>
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
        correctProgressApi,
        autoCorrectApi,
        calculateScoreApi,
    } from '@/api/paper.js'

    export default {
        data() {
            return {
                searchInput: '',
                tableData: [],
                dialogFormVisible: false,
                multipleSelection: [],
                examSelectItem: [],
                examSelectValue: '',
                result: '',
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                listExamApi().then(res => {
                    var table = res.data.data;
                    this.examSelectItem = table;
                });
            },
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            selectChangeFn(examId) {
                correctProgressApi(examId).then(res => {
                    var table = res.data.data;
                    this.tableData = table;
                });
            },
            autoCorrect() {
                autoCorrectApi(this.examSelectValue).then(res => {
                    this.result = res.data.data;
                    this.dialogFormVisible = true;
                });
            },
            setScore() {
                calculateScoreApi(this.examSelectValue).then(res => {
                    this.result = res.data.data;
                    this.dialogFormVisible = true;
                });
            },
            handleCorrect(row) {
                this.$message('todo:选择用户进行批改');
            }


        }
    }
</script>

<style>
    .manageStu {
        margin: 10px 0;
    }

    .searchInput {
        width: 200px;
    }
</style>

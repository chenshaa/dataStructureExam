<template>
    <div class="manageStu">

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

            <el-input class="searchInput ml-5" placeholder="请输入内容" v-model="input" clearable></el-input>
            <el-button class="ml-5" icon="el-icon-search" circle></el-button>

            <el-button type="primary" @click="autoCorrect">自动批改主观题 <i class="el-icon-s-claim"></i></el-button>
            <el-button type="primary" @click="setScore">计算得分 <i class="el-icon-s-check"></i></el-button>
            <el-button type="danger" slot="reference">批量移除 <i class="el-icon-remove-outline"></i></el-button>
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

                        <el-popconfirm class="ml-5" confirm-button-text='确定' cancel-button-text='我再想想'
                            icon="el-icon-info" icon-color="red" title="您确定移除吗？" @confirm="del(scope.row.id)">
                            <el-button type="danger" slot="reference">移除 <i class="el-icon-remove-outline"></i>
                            </el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>

        </div>

        <!--自动批改结果-->
        <div>
            <el-dialog title="自动批改" :visible.sync="dialogFormVisible" width="30%">
                
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import {
        listExamApi
    } from '@/api/exam.js'

    import {
        listStudentApi,
        adduserApi
    } from '@/api/user.js'

    import {
        listStudentInExamApi,
        addPaperApi,
        correctProgressApi,
    } from '@/api/paper.js'

    export default {
        data() {
            return {
                input: '',
                tableData: [],
                dialogFormVisible: false,
                multipleSelection: [],
                selectItems: [],
                selectValue: '',
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
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            selectChangeFn(selVal) { 
                correctProgressApi(selVal).then(res => {
                    var table = res.data.data;
                    this.tableData = table;
                });
            },
            autoCorrect(){
                
            },
            setScore(){
                
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

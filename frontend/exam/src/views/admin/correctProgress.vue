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

            <el-button type="primary" @click="userAdd">添加 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" slot="reference">批量移除 <i class="el-icon-remove-outline"></i></el-button>
        </div>

        <div style="margin: 10px 0">

            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="userId" label="ID" width="200"></el-table-column>
                <el-table-column prop="userAccount" label="账号" width="160"></el-table-column>
                <el-table-column prop="userNickname" label="昵称" width="160"></el-table-column>
                <el-table-column prop="userGroup" label="用户组" width="80"></el-table-column>
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

        <div>
            <el-dialog title="添加学生" :visible.sync="dialogFormVisible" width="30%">
                <el-form label-width="80px" size="small">

                    <el-form-item label="学生">

                        <el-select value-key="userId" v-model="stuSelectValue"  filterable
                            placeholder="请选择">
                            <el-option v-for="item in stuSelectItems" :key="item.userId" :label="item.userNickname"
                                :value="item.userId">
                            </el-option>
                        </el-select>

                    </el-form-item>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
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
        listStudentApi,
        adduserApi
    } from '@/api/user.js'

    import {
        listStudentInExamApi,
        addPaperApi,
    } from '@/api/paper.js'

    export default {
        data() {
            return {
                input: '',
                tableData: [],
                dialogFormVisible: false,
                multipleSelection: [],
                form: {
                    userGroup: '2'
                },
                roles: [{
                    value: '2',
                    label: '学生'
                }, {
                    value: '1',
                    label: '教师',
                    disabled: true
                }, {
                    value: '0',
                    label: '管理员',
                    disabled: true
                }],
                selectItems: [],
                selectValue: '',
                stuSelectItems: [],
                stuSelectValue: '',
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
                addPaperApi({
                    paperUser:this.stuSelectValue,
                    paperLink:this.selectValue
                }).then(res => {
                    if (res.data.code == 200) {
                        //添加成功
                        this.$message({
                            message: '添加成功',
                            type: 'success',
                            showClose: true
                        });
                        this.dialogFormVisible=false;
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
                listStudentApi().then(res => {
                    this.stuSelectItems = res.data.data.data;
                    console.log(res.data.data);
                });
            },
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            selectChangeFn(selVal) {
                listStudentInExamApi(selVal).then(res => {
                    var table = res.data.data;
                    this.tableData = table;
                });
            },

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

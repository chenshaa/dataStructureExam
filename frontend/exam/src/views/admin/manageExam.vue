<template>
    <div class="manageStu">

        <div style="margin: 10px 0">
            <el-input class="searchInput ml-5" placeholder="请输入内容" v-model="input" clearable></el-input>
            <el-button class="ml-5" icon="el-icon-search" circle></el-button>

            <el-divider direction="vertical"></el-divider>

            <el-button type="primary" @click="userAdd">增加场次<i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" slot="reference">批量删除场次<i class="el-icon-remove-outline"></i></el-button>
        </div>

        <div style="margin: 10px 0">

            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="examId" label="ID" width="200"></el-table-column>
                <el-table-column prop="examName" label="考试名称"></el-table-column>
                <el-table-column prop="examDescription" label="描述" width="160"></el-table-column>
                <el-table-column prop="examStartTime" label="开始时间" width="160"></el-table-column>
                <el-table-column prop="examEndTime" label="结束时间" width="160"></el-table-column>
                <el-table-column label="操作" align="left" width="460">
                    <template slot-scope="scope">

                        <el-button type="primary" @click="handleStart(scope.row)">现在开始 <i
                                class="el-icon-video-play"></i>
                        </el-button>

                        <el-button type="primary" @click="handleEnd(scope.row)">现在结束 <i class="el-icon-video-pause
"></i>
                        </el-button>

                        <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i>
                        </el-button>
                        <el-popconfirm class="ml-5" confirm-button-text='确定' cancel-button-text='我再想想'
                            icon="el-icon-info" icon-color="red" title="您确定删除吗？" @confirm="del(scope.row.id)">
                            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i>
                            </el-button>

                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>

        </div>

        <div>
            <el-dialog title="考试信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal=false
                :close-on-press-escape=false :before-close="handleClose">
                <el-form label-width=auto size="small">

                    <el-form-item label="考试名称">
                        <el-input v-model="form.examName" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="考试描述">
                        <el-input v-model="form.examDescription" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="考试开始时间">
                        <el-date-picker v-model="form.examStartTime" type="datetime" placeholder="选择开始时间"
                            value-format="timestamp">
                        </el-date-picker>
                    </el-form-item>

                    <el-form-item label="考试结束时间">
                        <el-date-picker v-model="form.examEndTime" type="datetime" placeholder="选择结束时间" align="right"
                            :picker-options="pickerOptions" value-format="timestamp">
                        </el-date-picker>
                    </el-form-item>


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
        listExamApi,
        addExamApi,
        setExamStartApi,
        setExamEndApi,
    } from '@/api/exam.js'

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
                pickerOptions: {
                    shortcuts: [{
                        text: '今天',
                        onClick(picker) {
                            picker.$emit('pick', new Date());
                        }
                    }, {
                        text: '昨天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() - 3600 * 1000 * 24);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '一周前',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', date);
                        }
                    }]
                },

            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                listExamApi().then(res => {
                    var table = res.data.data;
                    for (var j = 0; j < table.length; j++) {
                        if (table[j].examStartTime != null) {
                            table[j].examStartTime = new Date(parseInt(table[j].examStartTime))
                                .toLocaleString();
                        } else {
                            table[j].examStartTime = "未指定"
                        }

                        if (table[j].examEndTime != null) {
                            table[j].examEndTime = new Date(parseInt(table[j].examEndTime)).toLocaleString();
                        } else {
                            table[j].examEndTime = "未指定"
                        }
                    }
                    this.tableData = table;
                });
            },
            save() {
                addExamApi(this.form).then(res => {
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
            },
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            handleEdit(row) {
                this.editMode = true;
                var editData = JSON.parse(JSON.stringify(row));

                if (editData.examStartTime == "未指定") {
                    editData.examStartTime = null;
                } else {
                    editData.examStartTime = new Date(editData.examStartTime).getTime()
                }

                if (editData.examEndTime == "未指定") {
                    editData.examEndTime = null;
                } else {
                    editData.examEndTime = new Date(editData.examEndTime).getTime()
                }

                this.form = editData
                this.dialogFormVisible = true
            },
            handleClose(done) {
                this.editMode = false;
                done();
            },
            handleStart(row) {
                setExamStartApi(row.examId).then(res => {
                    if (res.data.code == 200) {
                        //成功
                        this.$message({
                            message: '考试已经开始',
                            type: 'success',
                            showClose: true
                        });
                    } else {
                        //失败
                        this.$message({
                            message: '开启失败：' + res.data.msg + '  code' + res.data.code,
                            type: 'error',
                            showClose: true
                        });
                    }
                });
            },
            handleEnd(row) {
                setExamEndApi(row.examId).then(res => {
                    if (res.data.code == 200) {
                        //成功
                        this.$message({
                            message: '考试已经结束',
                            type: 'success',
                            howClose: true
                        });
                    } else {
                        //失败
                        this.$message({
                            message: '结束失败：' + res.data.msg + '  code' + res.data.code,
                            type: 'error',
                            showClose: true
                        });
                    }
                });
            },
            del(id) {

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

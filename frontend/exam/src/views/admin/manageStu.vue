<template>
    <div class="manageStu">

        <div style="margin: 10px 0">
            <el-input class="searchInput ml-5" placeholder="请输入内容" v-model="searchInput" clearable></el-input>
            <el-button class="ml-5" icon="el-icon-search" circle></el-button>

            <el-button type="primary" @click="userAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        </div>

        <div style="margin: 10px 0">

            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="userId" label="ID" width="200"></el-table-column>
                <el-table-column prop="userAccount" label="账号" width="160"></el-table-column>
                <el-table-column prop="userNickname" label="昵称"></el-table-column>

                <el-table-column prop="userGroup" label="用户组" width="80">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.userGroup === '学生' ? 'primary' : 'success'" disable-transitions>
                            {{scope.row.userGroup}}
                        </el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="操作" align="left">
                    <template slot-scope="scope">
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

        <!--添加用户dialog-->
        <div>
            <el-dialog title="用户信息" :visible.sync="userInfoFormVisible" width="30%">
                <el-form label-width="80px" size="small">

                    <el-form-item label="姓名">
                        <el-input v-model="addUserForm.userNickname" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="账号">
                        <el-input v-model="addUserForm.account" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="密码">
                        <el-input v-model="addUserForm.password" autocomplete="off" show-password></el-input>
                    </el-form-item>

                    <el-form-item label="确认密码">
                        <el-input v-model="addUserForm.password2" autocomplete="off" show-password></el-input>
                    </el-form-item>

                    <el-form-item label="角色">
                        <el-select v-model="addUserForm.userGroup" placeholder="请选择角色" style="width: 100%">
                            <el-option v-for="item in roles" :key="item.value" :label="item.label" :value="item.value"
                                :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="userInfoFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="addUser">确 定</el-button>
                </div>
            </el-dialog>
        </div>

        <!--编辑用户dialog-->
        <div>
            <el-dialog title="编辑用户信息" :visible.sync="userInfoEditFormVisible" width="30%">
                <el-form label-width="80px" size="small">

                    <el-form-item label="姓名">
                        <el-input v-model="editUserForm.userNickname" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="账号">
                        <el-input v-model="editUserForm.account" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="密码">
                        <el-input v-model="editUserForm.password" autocomplete="off" show-password></el-input>
                    </el-form-item>

                    <el-form-item label="确认密码">
                        <el-input v-model="editUserForm.password2" autocomplete="off" show-password></el-input>
                    </el-form-item>

                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="userInfoEditFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="editUser">确 定</el-button>
                </div>
            </el-dialog>
        </div>

    </div>
</template>

<script>
    import {
        listStudentApi,
        adduserApi
    } from '@/api/user.js'

    export default {
        data() {
            return {
                searchInput: '',
                tableData: [],
                userInfoFormVisible: false,
                userInfoEditFormVisible: false,
                multipleSelection: [],
                addUserForm: {
                    userGroup: '2'
                },
                editUserForm: {
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
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                listStudentApi().then(res => {
                    var table = res.data.data.data;
                    for (var j = 0; j < table.length; j++) {
                        for (var i = 0; i < 3; i++) {
                            if (this.roles[i].value == table[j].userGroup) {
                                table[j].userGroup = this.roles[i].label;
                            }
                        }
                    }
                    this.tableData = table;
                });
            },
            addUser() {
                adduserApi(this.addUserForm).then(res => {
                    if (res.data.code == 200) {
                        //添加成功
                        this.$message({
                            message: '添加成功',
                            type: 'success',
                            showClose: true
                        });
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
                this.userInfoFormVisible = true;
            },
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            handleEdit(val) {
                this.userInfoEditFormVisible = true;
            },
            editUser() {
                this.$message('todo:用户信息编辑');
                this.userInfoEditFormVisible = false;
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

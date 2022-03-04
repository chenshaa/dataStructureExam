<template>
    <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden" background-color="rgb(48, 65, 86)"
        text-color="#fff" active-text-color="#ffd04b" :collapse-transition="false" :collapse="isCollapse" router>
        <div style="height: 60px; line-height: 60px; text-align: center">
            <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px;">
            <b style="color: white; margin-left: 5px" v-show="logoTextShow">后台管理系统</b>
        </div>
        <div v-for="item in menus" :key="item.id">
            <div v-if="item.path">
                <el-menu-item :index="item.path">
                    <i :class="item.icon"></i>
                    <span slot="title">{{ item.name }}</span>
                </el-menu-item>
            </div>
            <div v-else>
                <el-submenu :index="item.id + ''">
                    <template slot="title">
                        <i :class="item.icon"></i>
                        <span slot="title">{{ item.name }}</span>
                    </template>
                    <div v-for="subItem in item.children" :key="subItem.id">
                        <el-menu-item :index="subItem.path">
                            <i :class="subItem.icon"></i>
                            <span slot="title">{{ subItem.name }}</span>
                        </el-menu-item>
                    </div>
                </el-submenu>
            </div>
        </div>
    </el-menu>
</template>

<script>
    export default {
        name: "Aside",
        props: {
            isCollapse: Boolean,
            logoTextShow: Boolean
        },
        data() {
            return {
                menus: [{
                        "id": 10,
                        "name": "主页",
                        "path": "/admin",
                        "icon": "el-icon-house",
                        "description": null,
                        "children": [],
                        "pid": null,
                        "pagePath": "Home",
                        "sortNum": "0"
                    },
                    {
                        "id": 9,
                        "name": "学生管理",
                        "path": "/admin/manageStu",
                        "icon": "el-icon-user",
                        "description": null,
                        "children": [],
                        "pid": null,
                        "pagePath": "Donate",
                        "sortNum": "100"
                    },
                    {
                        "id": 11,
                        "name": "题库管理",
                        "path": "/admin/manageQues",
                        "icon": "el-icon-tickets",
                        "description": null,
                        "children": [],
                        "pid": null,
                        "pagePath": "Donate",
                        "sortNum": "200"
                    },
                    {
                        "id": 4,
                        "name": "考试管理",
                        "path": null,
                        "icon": "el-icon-edit-outline",
                        "description": null,
                        "children": [{
                                "id": 3,
                                "name": "考试场次管理",
                                "path": "/admin/manageExam",
                                "icon": "el-icon-date",
                                "description": null,
                                "children": null,
                                "pid": 4,
                                "pagePath": "User",
                                "sortNum": "301"
                            },
                            {
                                "id": 2,
                                "name": "参与考试人员",
                                "path": "/admin/manageExamStu",
                                "icon": "el-icon-s-custom",
                                "description": null,
                                "children": null,
                                "pid": 4,
                                "pagePath": "Role",
                                "sortNum": "302"
                            },
                            {
                                "id": 1,
                                "name": "出题",
                                "path": "/admin/manageExamQues",
                                "icon": "el-icon-circle-plus-outline",
                                "description": null,
                                "children": null,
                                "pid": 4,
                                "pagePath": "Menu",
                                "sortNum": "303"
                            }
                        ],
                        "pid": null,
                        "pagePath": null,
                        "sortNum": "300"
                    },
                    {
                        "id": 8,
                        "name": "试卷管理",
                        "path": null,
                        "icon": "el-icon-folder",
                        "description": null,
                        "children": [{
                                "id": 7,
                                "name": "试卷管理",
                                "path": "/admin/correctProgress",
                                "icon": "el-icon-document",
                                "description": null,
                                "children": null,
                                "pid": 8,
                                "pagePath": "User",
                                "sortNum": "301"
                            },
                            {
                                "id": 6,
                                "name": "批改试卷",
                                "path": "/admin/correctPapers",
                                "icon": "el-icon-finished",
                                "description": null,
                                "children": null,
                                "pid": 8,
                                "pagePath": "Role",
                                "sortNum": "302"
                            },
                            {
                                "id": 5,
                                "name": "统计",
                                "path": "/admin/paperStatistic",
                                "icon": "el-icon-data-analysis",
                                "description": null,
                                "children": null,
                                "pid": 8,
                                "pagePath": "Menu",
                                "sortNum": "303"
                            }
                        ],
                        "pid": null,
                        "pagePath": null,
                        "sortNum": "300"
                    },
                    
                ],
                opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id +
                    '') : [],
            }
        },
    }
</script>

<style>
    .el-menu-item.is-active {
        background-color: rgb(38, 52, 69) !important;
    }

    .el-menu-item:hover {
        background-color: rgb(38, 52, 69) !important;
    }

    .el-submenu__title:hover {
        background-color: rgb(38, 52, 69) !important;
    }

    /*解决收缩菜单文字不消失问题*/
    .el-menu--collapse span {
        visibility: hidden;
    }
</style>

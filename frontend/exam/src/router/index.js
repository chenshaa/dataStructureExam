import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '@/views/login'

import Admin from '@/views/admin/index.vue'
import AdminWelcomePage from '@/views/admin/welcomePage.vue'
import AdminManageStu from '@/views/admin/manageStu.vue'
import AdminManageExam from '@/views/admin/manageExam.vue'
import AdminManagePaper from '@/views/admin/managePaper.vue'

Vue.use(VueRouter)

const routes = [{
        path: '/',
        name: 'login',
        component: Login
    },
    {
        path: '/admin',
        name: 'AdminPage',
        component: Admin,
        children: [{
                path: '',
                name: '首页',
                component: AdminWelcomePage,
            },
            {
                path: 'manageStu',
                name: '学生管理',
                component: AdminManageStu,
            },
            {
                path: 'manageExam',
                name: '考试管理',
                component: AdminManageExam,
            },
            {
                path: 'managePaper',
                name: '试卷管理',
                component: AdminManagePaper,
            },
        ]
    }
]

const router = new VueRouter({
    routes,
    mode: 'history'
})

const originalPush = VueRouter.prototype.push
   VueRouter.prototype.push = function push(location) {
   return originalPush.call(this, location).catch(err => err)
}

export default router

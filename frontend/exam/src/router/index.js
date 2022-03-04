import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [{
        path: '/',
        name: 'login',
        component: () => import('@/views/login')
    },
    {
        path: '/admin',
        name: 'AdminPage',
        component:() => import('@/views/admin/index.vue'),
        children: [{
                path: '',
                name: '首页',
                component: () => import('@/views/admin/welcomePage.vue'),
            },
            {
                path: 'manageStu',
                name: '学生管理',
                component: () => import('@/views/admin/manageStu.vue'),
            },
            {
                path: 'manageExam',
                name: '考试管理',
                component: () => import('@/views/admin/manageExam.vue'),
            },
            {
                path: 'managePaper',
                name: '试卷管理',
                component: () => import('@/views/admin/managePaper.vue'),
            },
            {
                path: 'manageQues',
                name: '题库管理',
                component: () => import('@/views/admin/manageQues.vue'),
            },
            {
                path: 'manageExamQues',
                name: '出题管理',
                component: () => import('@/views/admin/manageExamQues.vue'),
            },
            {
                path: 'manageExamStu',
                name: '考试学生管理',
                component: () => import('@/views/admin/manageExamStu.vue'),
            },
            ,
            {
                path: 'correctPapers',
                name: '批改试卷',
                component: () => import('@/views/admin/correctPapers.vue'),
            },
            ,
            {
                path: 'correctProgress',
                name: '考试进度',
                component: () => import('@/views/admin/correctProgress.vue'),
            },
            ,
            {
                path: 'paperStatistic',
                name: '试卷统计',
                component: () => import('@/views/admin/paperStatistic.vue'),
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

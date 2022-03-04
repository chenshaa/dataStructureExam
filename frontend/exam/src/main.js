import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import store from './store'
import App from './App.vue'
import router from './router'

import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
Vue.component("mavon-editor", mavonEditor);

Vue.config.productionTip = false

Vue.use(ElementUI)

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app')

import 'iview/dist/styles/iview.css'
import Vue from 'vue'
import iView from 'iview'
import VueRouter from 'vue-router'
import Login from './login.vue'

Vue.config.productionTip = false
Vue.use(VueRouter)
Vue.use(iView)

const routes = [
  { path: '/login', component: Login },
  { path: '/', redirect: '/login' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

new Vue({
  router,
  render: h => h(Login)
}).$mount('#app')

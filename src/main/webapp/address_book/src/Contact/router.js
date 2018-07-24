import Vue from 'vue';
import Router from 'vue-router';
import List from './list.vue';
import Add from './add.vue';
import Edit from './edit.vue';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'list',
      component: List,
    },
    {
      path: '/add',
      name: 'add',
      component: Add,
    },
    {
      path: '/edit/:id',
      name: 'edit',
      component: Edit,
    },
    {
      path: '/',
      component: List,
    },
  ],
});
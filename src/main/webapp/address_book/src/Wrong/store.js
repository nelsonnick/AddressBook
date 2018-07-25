import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    keyword: '',
    pageCurrent: '1'
  },
  mutations: {
    save (state, page) {
      state.keyword = page.keyword
      state.pageCurrent = page.pageCurrent
    }
  },
  actions: {

  },
});

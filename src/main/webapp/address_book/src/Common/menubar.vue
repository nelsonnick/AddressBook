<template>
  <Menu mode="horizontal" theme="dark" :active-name="active" @on-select="MenuClick">
    <div class="layout-nav">
      <MenuItem name="county">市区</MenuItem>
      <MenuItem name="bureau">部门</MenuItem>
      <MenuItem name="department">科室</MenuItem>
      <MenuItem name="contact">联络</MenuItem>
    </div>
  </Menu>
</template>
<script>
  import * as BASE from './Base.js'
  export default {
    name: 'menubar',
    props: ['active'],
    methods: {
      MenuClick (name) {
        if (name.toString() === 'county') {
          window.location.href = BASE.base + 'county'
        } else if (name.toString() === 'bureau') {
          window.location.href = BASE.base + 'bureau'
        } else if (name.toString() === 'department') {
          window.location.href = BASE.base + 'department'
        } else if (name.toString() === 'contact') {
          window.location.href = BASE.base + 'contact'
        } else {
          window.location.href = BASE.base
        }
      }
    }
  }
</script>
<style scoped>
  .layout-nav {
    width: 1200px;
    margin: 0 auto;
    margin-right: 20px;
  }
</style>

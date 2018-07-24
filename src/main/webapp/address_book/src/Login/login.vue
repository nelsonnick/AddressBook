<template>
  <Form ref="formInline" :model="formInline" inline>
    <FormItem prop="user">
      <Input type="text" v-model="formInline.user" placeholder="用户名">
      <Icon type="ios-person-outline" slot="prepend"></Icon>
      </Input>
    </FormItem>
    <FormItem prop="password">
      <Input type="password" v-model="formInline.password" placeholder="密码">
      <Icon type="ios-locked-outline" slot="prepend"></Icon>
      </Input>
    </FormItem>
    <FormItem>
      <Button type="primary" @click="handleSubmit">登录</Button>
    </FormItem>
  </Form>
</template>
<script>
  import axios from 'axios'
  export default {
    data () {
      return {
        formInline: {
          user: '',
          password: ''
        }
      }
    },
    methods: {
      handleSubmit() {
        axios.get('/login', {
          params: {
            user: this.formInline.user,
            password: this.formInline.password
          }
        }).then(res => {
          if (res.data === 'OK') {
            window.location.href = '/d'
          } else {
            this.$Message.error('用户名或密码错误!')
          }
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
    }
  }
</script>
<style lang="less">
</style>

<template>
  <div>
    <Layout class="layout">
      <Header>
        <MenuBar :active="active"></MenuBar>
      </Header>
      <Row>
        <Col>
          <div>
            <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
              <BreadcrumbItem>人社通讯簿</BreadcrumbItem>
              <BreadcrumbItem>部门</BreadcrumbItem>
              <BreadcrumbItem>新增</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100" ref="Form">
          <Form-item size="large" label="市区" required>
            <Select  size="large" v-model="countyId" style="width: 600px">
              <Option v-for="item in county" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item label="名称" required>
            <Input size="large" v-model="name"  style="width: 600px"></Input>
          </Form-item>
          <Form-item label="网址" required>
            <Input size="large" v-model="url" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="职责" required>
            <Input size="large" v-model="duty" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="备注" required>
            <Input size="large" v-model="remark" style="width: 600px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave" :disabled="dis">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset" :disabled="dis">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack" :disabled="dis">返回</Button>
          </Form-item>
        </Form>
        </Col>
        <Col span="6">&nbsp;</Col>
      </Row>
    </Layout>
  </div>
</template>
<script>
  import * as API from './API.js'
  import MenuBar from '../Common/menubar.vue'
  import axios from 'axios'

  export default {
    name: 'add',
    components: {MenuBar},
    data () {
      return {
        active: 'bureau',
        dis: false,
        countyId: '1',
        name: '',
        url: '',
        duty: '',
        remark: '',
        county: []
      }
    },created: function () {
      this.getCounty()
    },

    methods: {
      goReset () {
        this.name = ''
        this.url = ''
        this.duty = ''
        this.remark = ''
        this.countyId = '1'
      },
      goSave() {
        this.dis = true
        this.$Loading.start()
        axios.get(API.Add, {
          params: {
            countyId: this.countyId,
            name: this.name,
            url: this.url,
            duty: this.duty,
            remark: this.remark
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('新增成功!')
            setTimeout(() => {
              this.$router.push({path: '/'})
            }, 1000)
          } else {
            this.dis = false
            this.$Loading.error()
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.dis = false
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/' })
      },
      getCounty() {
        axios.get(API.GetCounty).then(res => {
          this.county = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法获取市区列表!'
          })
        })
      }
    }
  }
</script>
<style scoped>
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
</style>

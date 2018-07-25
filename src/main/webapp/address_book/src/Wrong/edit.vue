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
              <BreadcrumbItem>报错</BreadcrumbItem>
              <BreadcrumbItem>修改</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
        <Form :label-width="100">
          <Form-item size="large" label="市区" required>
            <Select  size="large" v-model="countyId" style="width: 600px" disabled>
              <Option v-for="item in county" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item size="large" label="部门" required>
            <Select  size="large" v-model="bureauId" style="width: 600px" disabled>
              <Option v-for="item in bureau" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item size="large" label="科室" required>
            <Select  size="large" v-model="departmentId" style="width: 600px" disabled>
              <Option v-for="item in department" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item label="类型" required>
            <Input size="large" v-model="type" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item label="内容" required>
            <Input size="large" v-model="content" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item label="时间" required>
            <Input size="large" v-model="time" style="width: 600px" disabled></Input>
          </Form-item>
          <Form-item>
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
    name: 'edit',
    components: {MenuBar},
    data () {
      return {
        active: 'wrong',
        dis: false,
        departmentId: '',
        type: '',
        content: '',
        time: ''
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
    },
    watch: {
      // 如果路由有变化，会再次执行该方法
      '$route': 'fetchData'
    },
    methods: {
      goReset () {
        this.fetchData(this.$route.params.id)
      },
      goBack () {
        this.$router.push({ path: '/' })
      },
      fetchData (id) {
        axios.get(API.Get,
           { params: { id: id } }
        ).then(res => {
          this.departmentId = res.data.did.toString()
          this.type = res.data.type
          this.content = res.data.content
          this.time = res.data.time.toString()
          this.getBureauId(this.departmentId)
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误，无法获取数据!'
          })
        })
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
      },
      getCountyId(bureauId) {
        axios.get(API.GetCountyId,{
          params: {
            id: bureauId
          }
        }).then(res => {
          this.countyId = res.data.cid.toString()
          this.getBureau(this.countyId)
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法获取市区!'
          })
        })
      },
      getBureau(countyId) {
        axios.get(API.GetBureau,{
          params: {
            countyId: countyId
          }
        }).then(res => {
          this.bureau = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法获取部门列表!'
          })
        })
      },
      getBureauId(departmentId) {
        axios.get(API.GetBureauId,{
          params: {
            id: departmentId
          }
        }).then(res => {
          this.bureauId = res.data.bid.toString()
          this.getCountyId(this.bureauId)
          this.getDepartment(this.bureauId)
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法获取部门!'
          })
        })
      },
      getDepartment(bureauId) {
        axios.get(API.GetDepartment,{
          params: {
            bureauId: bureauId
          }
        }).then(res => {
          this.department = eval('(' + res.data + ')')
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法获取科室列表!'
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

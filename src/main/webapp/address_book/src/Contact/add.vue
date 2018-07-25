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
              <BreadcrumbItem>联络</BreadcrumbItem>
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
            <Select  size="large" v-model="countyId" style="width: 600px" @on-change="changeBureau">
              <Option v-for="item in county" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item size="large" label="部门" required>
            <Select  size="large" v-model="bureauId" style="width: 600px" @on-change="changeDepartment">
              <Option v-for="item in bureau" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item size="large" label="科室" required>
            <Select  size="large" v-model="departmentId" style="width: 600px">
              <Option v-for="item in department" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select >
          </Form-item>
          <Form-item label="名称" required>
            <Input size="large" v-model="name"  style="width: 600px"></Input>
          </Form-item>
          <Form-item label="电话" required>
            <Input size="large" v-model="phone" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="地址" required>
            <Input size="large" v-model="address" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="办公室号" required>
            <Input size="large" v-model="office" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="经度" required>
            <Input size="large" v-model="latitude" style="width: 600px"></Input>
          </Form-item>
          <Form-item label="纬度" required>
            <Input size="large" v-model="longitude" style="width: 600px"></Input>
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
        active: 'contact',
        dis: false,
        countyId: '1',
        bureauId: '1',
        departmentId: '1',
        name: '',
        address: '',
        phone: '',
        office: '',
        latitude: '',
        longitude: '',
        duty: '',
        remark: '',
        county: [],
        bureau: [],
        department: []
      }
    },
    created: function () {
      this.getCounty()
      this.getBureau('1')
      this.getDepartment('1')
    },
    methods: {
      goReset () {
        this.name = ''
        this.address = ''
        this.phone = ''
        this.office = ''
        this.latitude = ''
        this.longitude = ''
        this.duty = ''
        this.remark = ''
        this.countyId = '1'
        this.bureauId = '1'
        this.departmentId = '1'
      },
      goSave() {
        this.dis = true
        this.$Loading.start()
        axios.get(API.Add, {
          params: {
            departmentId: this.departmentId,
            name: this.name,
            address: this.address,
            phone: this.phone,
            office: this.office,
            latitude: this.latitude,
            longitude: this.longitude,
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
      },
      changeBureau(countyId) {
        this.getBureau(countyId)
        this.bureauId = ''
      },
      changeDepartment(bureauId) {
        this.getDepartment(bureauId)
        this.departmentId = ''
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

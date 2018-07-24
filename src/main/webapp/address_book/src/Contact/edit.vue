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
              <BreadcrumbItem>修改</BreadcrumbItem>
            </Breadcrumb>
          </div>
        </Col>
      </Row>
      <Row>
        <Col span="6">&nbsp;</Col>
        <Col span="12">
          <Form :label-width="100" ref="Form">
            <Form-item size="large" label="市区" required>
              <Select  size="large" v-model="countyId" style="width: 600px"  @on-change="changeBureau">
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
            <Form-item label="地址" required>
              <Input size="large" v-model="address" style="width: 600px"></Input>
            </Form-item>
            <Form-item label="电话" required>
              <Input size="large" v-model="phone" style="width: 600px"></Input>
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
    name: 'edit',
    components: {MenuBar},
    data () {
      return {
        active: 'contact',
        dis: false,
        countyId: '',
        bureauId: '',
        departmentId: '',
        name: '',
        address: '',
        phone: '',
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
      goSave() {
        this.dis = true
        this.$Loading.start()
        axios.get(API.Edit, {
          params: {
            id: this.$route.params.id,
            departmentId: this.departmentId,
            name: this.name,
            address: this.address,
            phone: this.phone,
            latitude: this.latitude,
            longitude: this.longitude,
            duty: this.duty,
            remark: this.remark
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('修改成功!')
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
      fetchData (id) {
        axios.get(API.Get,
          { params: { id: id } }
        ).then(res => {
          this.departmentId = res.data.did.toString()
          this.name = res.data.name
          this.address = res.data.address
          this.phone = res.data.phone
          this.latitude = res.data.latitude
          this.longitude = res.data.longitude
          this.duty = res.data.duty
          this.remark = res.data.remark
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

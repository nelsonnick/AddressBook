<template>
  <div>
    <Layout class="layout">
      <Form :label-width="120" ref="Form">
        <Row>
          <Col span="12">
            <Form-item label="市区" required>
              <Input size="large" v-model="countyName" style="width: 400px" disabled></Input>
            </Form-item>
            <Form-item label="部门" required>
              <Input size="large" v-model="bureauName" style="width: 400px" disabled></Input>
            </Form-item>
            <Form-item label="科室" required>
              <Input size="large" v-model="departmentName" style="width: 400px" disabled></Input>
            </Form-item>
          </Col>
          <Col span="12">
            <Form-item label="名称" required>
              <Input size="large" v-model="contactName" style="width: 400px"></Input>
            </Form-item>
            <Form-item label="房间" required>
              <Input size="large" v-model="contactOffice" style="width: 400px"></Input>
            </Form-item>
            <Form-item label="电话" required>
              <Input size="large" v-model="contactPhone" style="width: 400px"></Input>
            </Form-item>
          </Col>
        </Row>
        <Row>
          <Col span="8">&nbsp;</Col>
          <Col span="8">
            <Form-item>
              <Button size="large" type="success" @click="goSave">保存</Button>
              <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
              <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack">返回</Button>
            </Form-item>
          </Col>
          <Col span="8">&nbsp;</Col>
        </Row>
      </Form>
    </Layout>
  </div>
</template>
<script>
  import * as API from './API.js'
  import axios from 'axios'

  export default {
    name: 'editContact',
    data () {
      return {
        countyName: '',
        bureauName: '',
        departmentName: '',
        departmentAddress: '',
        departmentImg: '',
        departmentLatitude: '',
        departmentLongitude: '',
        departmentDuty: '',
        departmentRemark: '',
        contactName: '',
        contactPhone: '',
        contactOffice: ''
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
      goSave() {
        this.$Loading.start()
        axios.get(API.EditContact, {
          params: {
            id: this.$route.params.id,
            contactName: this.contactName,
            contactPhone: this.contactPhone,
            contactOffice: this.contactOffice
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('修改成功!')
            setTimeout(() => {
              this.$router.push({path: '/list'})
            }, 1000)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误，无法修改!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
      },
      fetchData (id) {
        axios.get(API.GetContact,
          { params: { id: id } }
        ).then(res => {
          this.countyName = res.data.countyName
          this.bureauName = res.data.bureauName
          this.departmentName = res.data.departmentName
          this.departmentAddress = res.data.departmentAddress
          this.departmentImg = res.data.departmentImg
          this.departmentLatitude = res.data.departmentLatitude
          this.departmentLongitude = res.data.departmentLongitude
          this.departmentDuty = res.data.departmentDuty
          this.departmentRemark = res.data.departmentRemark
          this.contactName = res.data.contactName
          this.contactPhone = res.data.contactPhone
          this.contactOffice = res.data.contactOffice
        }).catch(res => {
          this.$Notice.error({
            title: '服务器内部错误!'
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

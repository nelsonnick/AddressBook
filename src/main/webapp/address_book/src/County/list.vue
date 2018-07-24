<template>
  <div>
    <Layout class="layout">
      <Header>
        <MenuBar :active="active"></MenuBar>
      </Header>
        <Row>
          <Col>
            <div class="left">
              <Breadcrumb :style="{margin: '20px 15px 0px 15px'}">
                <BreadcrumbItem>人社通讯簿</BreadcrumbItem>
                <BreadcrumbItem>市区</BreadcrumbItem>
                <BreadcrumbItem>列表</BreadcrumbItem>
              </Breadcrumb>
            </div>
            <div class="right">
              <Button type="info" @click="goAdd">新增</Button>
              <Search @goQuery="getQuery"></Search>
            </div>
          </Col>
        </Row>
        <Row>
          <Col>
          <div style="min-height: 200px;" class="layout-content">
            <Table
              highlight-row
              :height="height"
              :context="self"
              :border="border"
              :stripe="stripe"
              :size="size"
              :columns="columns"
              :data="pageList">
            </Table>
          </div>
          </Col>
        </Row>
      <Row>
        <Col>
        <div>
          <div class="left">
            <Options
              @showBorder="getBorder"
              @showStripe="getStripe"
              @tableSize="getSize"
            >
            </Options>
          </div>
          <div class="right">
            <Page
              ref="pages"
              @goList="getList"
              @savePageCurrent="saveCurrent"
              @savePageCurrentAndKeyword="CurrentAndKeyword"
              :queryURL="query"
              :totalURL="total"
              :keyword="keyword"
            >
            </Page>
          </div>
        </div>
        </Col>
      </Row>
    </Layout>
  </div>
</template>
<script>
  import Search from '../Common/search.vue'
  import Page from '../Common/page.vue'
  import Options from '../Common/options.vue'
  import * as API from './API.js'
  import MenuBar from '../Common/menubar.vue'
  import axios from 'axios'

  export default {
    name: 'list',
    components: { Search, Page, Options, MenuBar },
    data () {
      return {
        active: 'county',
        query: API.Query,
        total: API.Total,
        keyword: '',
        pageList: [],
        pageTotal: '',
        index: '',
        border: false,
        stripe: false,
        size: 'small',
        height: 450,
        self: this,
        columns: [
          {
            title: '序号',
            key: 'id',
            width: 80,
            sortable: true,
            render: (h, params) => {
              return h('div', params.index + 1)
            }
          },
          {
            title: '名称',
            key: 'name',
            sortable: true
          },
          {
            title: '官网',
            key: 'url',
            sortable: true
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 300,
            render: (h, params) => {
              const operate = []
              operate.push(
                 h('Button', {
                   props: {
                     type: 'info',
                     size: 'small'
                   },
                   on: {
                     click: () => {
                       this.goEdit(params.index)
                     }
                   }
                 }, '修改')
              )
              operate.push(
                 h('Button', {
                   props: {
                     type: 'error',
                     size: 'small'
                   },
                   on: {
                     click: () => {
                       this.goDel(params.index)
                     }
                   }
                 }, '删除')
              )
              return h('div', operate)
            }
          }
        ]
      }
    },
    methods: {
      getQuery (keyword) {
        this.keyword = keyword
        this.$refs.pages.query(keyword)
      },
      getQueryNoChange (keyword) {
        this.keyword = keyword
        this.$refs.pages.queryNoChange(keyword)
      },
      getBorder (border) {
        this.border = border
      },
      getStripe (stripe) {
        this.stripe = stripe
      },
      getSize (tableSize) {
        if (tableSize.toString() === 'true') {
          this.height = 665
          this.size = 'large'
        } else {
          this.height = 450
          this.size = 'small'
        }
      },
      getList (pageList) {
        this.pageList = pageList
      },
      saveCurrent (pageCurrent) {
        this.$store.commit('save', {
          pageCurrent: pageCurrent
        })
      },
      CurrentAndKeyword (keyword, pageCurrent) {
        this.$store.commit('save', {
          keyword: keyword,
          pageCurrent: pageCurrent
        })
      },
      goAdd () {
        this.$router.push({path: '/add'})
      },
      goEdit (index) {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      },
      goDel(index) {
        this.$Loading.start()
        axios.get(API.Del, {
          params: {
            id: this.pageList[index].id
          }
        }).then(res => {
          if (res.data === 'OK') {
            this.$Loading.finish()
            this.$Message.success('删除成功!')
            this.getQuery(this.keyword)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: res.data
            })
          }
        }).catch(res => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      }
    }
  }
</script>
<style scoped>
  .layout-content{
    margin:0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .left{
    margin: 15px;
    border-radius: 4px;
  }
  .right{
    margin: 15px;
    border-radius: 4px;
    float: right;
  }
</style>
<style scoped>
  .layout-content {
    margin: 0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .left {
    margin: 15px;
    border-radius: 4px;
  }
  .right {
    margin: 15px;
    border-radius: 4px;
    float: right;
  }
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
</style>

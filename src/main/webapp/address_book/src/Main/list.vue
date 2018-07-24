<template>
  <div>
    <Layout class="layout">
      <Row>
        <Col>
          <div class="right">
            <Button type="info" @click="goAdd"><Icon type="plus"></Icon>新增</Button>
            <Button type="ghost" @click="goDown"><Icon type="ios-cloud-download"></Icon>下载</Button>
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
  import * as BASE from '../Common/Base.js'
  import axios from 'axios'

  export default {
    name: 'list',
    components: {Search, Page, Options},
    data () {
      return {
        name: '',
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
            sortable: true,
            width: 80,
            render: (h, params) => {
              return h('div', params.index + 1)
            }
          },
          {
            title: '市区--部门--科室',
            key: 'countyName',
            sortable: true,
            width: 80,
              render: (h, params) => {
                return h('div', params.row.countyName + '--' + params.row.bureauName + '--' + params.row.departmentName)
              }
          },
          {
            title: '办公地址',
            key: 'departmentAddress',
            sortable: true
          },
          {
            title: '经纬度',
            key: 'where',
            sortable: true,
            render: (h, params) => {
              return h('div', params.row.departmentLatitude + ',' + params.row.departmentLongitude)
            }
          },
          {
            title: '业务名称',
            key: 'contactName',
            sortable: true,
            width: 80
          },
          {
            title: '联系电话',
            key: 'contactPhone',
            sortable: true
          },
          {
            title: '房间号',
            key: 'contactOffice',
            sortable: true,
            width: 80
          },
          {
            title: '操作',
            key: 'operate',
            align: 'center',
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
                       this.goEditContact(params.index)
                     }
                   }
                 }, '修改电话')
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
      goEditContact (index) {
        this.$router.push({path: '/editContact/' + this.pageList[index].contactId})
      },
      goDel (index) {
        this.$router.push({path: '/del/' + this.pageList[index].contactId})
      },
      goDown () {
        window.location.href = BASE.base + 'd/export?keyword=' + this.keyword
      }
    }
  }
</script>
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

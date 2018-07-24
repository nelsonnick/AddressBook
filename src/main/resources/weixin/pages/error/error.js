var dataset = require('../datasets.js');
Page({
  data: {
    cid: '',
    bid: '',
    did: '',
    cname: '',
    bname: '',
    dname: '',
    checkboxValue: '',
    inputValue: "",
    errors:[
      { name: 'error_address', value: '办公地点' },
      { name: 'error_phone', value: '联系电话' },
      { name: 'error_location', value: '地图定位' },
      { name: 'error_office', value: '办公室号' },
      { name: 'error_img', value: '参考图片' },
      { name: 'error_duty', value: '部门职责' },
    ]
  },
  checkboxChange: function (e) {
    this.setData({ checkboxValue: e.detail.value })
  },
  bindKeyInput: function (e) {
    this.setData({
      inputValue: e.detail.value
    })
  },
  goSend: function () {
    wx.showToast({
      title: '成功',
      icon: 'success',
      duration: 2000
    })
    // wx.request({
    //   url: 'test.php', 
    //   data: {
    //     checkboxValue: this.data.checkboxValue,
    //     inputValue: this.data.inputValue
    //   },
    //   header: {
    //     'content-type': 'application/json'
    //   },
    //   success: function (res) {
    //     console.log(res.data)
    //   }
    // })
  },
  goBack: function () {
    wx.navigateBack({
      delta: 1
    })
  },
  onLoad: function (options) {
    const county = dataset.county[options.cid]
    const bureau = county.bureau[options.bid]
    const department = bureau.department[options.did]
    this.setData({
      cid: options.cid,
      bid: options.bid,
      did: options.did,
      cname: county.name,
      bname: bureau.name,
      dname: department.name,
    })
  },

})
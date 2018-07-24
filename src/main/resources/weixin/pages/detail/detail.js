var dataset = require('../datasets.js');
Page({
  data: {
    cid: '',
    bid: '',
    did: '',
    cname: '',
    bname: '',
    dname: '',
    contact: [],
    address: '',
    img: '',
    latitude: 0,
    longitude: 0,
    duty: '',
    remark: ''
  },
  callPhone: function (event) {
    wx.makePhoneCall({
      phoneNumber: event.currentTarget.dataset.phone
    })
  },
  goError: function () {
    wx.navigateTo({
      url: '../error/error?cid=' + this.data.cid + '&bid=' + this.data.bid + '&did=' + this.data.did
    })
  },
  goBack: function () {
    wx.navigateBack({
      delta: 1
    })
  },
  goMap: function () {
    wx.openLocation({
      latitude: this.data.latitude,
      longitude: this.data.longitude,
      scale: 28,
      name: this.data.bname + '\n' + this.data.dname,
      address: this.data.address
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
      contact: department.contact,
      address: department.address,
      img: department.img,
      latitude: department.latitude,
      longitude: department.longitude,
      duty: department.duty,
      remark: department.remark
    })
  }
})
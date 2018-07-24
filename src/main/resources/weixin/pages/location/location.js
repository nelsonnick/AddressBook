var dataset = require('../datasets.js');
var c = 0
var b = 4
var d = 20
Page({
  data: {
    flag: true,
    cid: c,
    bid: b,
    did: d,
    pic: false,
    county: dataset.county,
    bureau: dataset.county[c].bureau,
    department: dataset.county[c].bureau[b].department,
    latitude: dataset.county[c].bureau[b].department[d].latitude,
    longitude: dataset.county[c].bureau[b].department[d].longitude,
    img: dataset.county[c].bureau[b].department[d].img,
    markers: [{
      iconPath: '/image/location.png',
      scale: 28,
      latitude: dataset.county[c].bureau[b].department[d].latitude,
      longitude: dataset.county[c].bureau[b].department[d].longitude,
      width: 50,
      height: 50,
      title: dataset.county[c].bureau[b].department[d].name,
      callout: {
        content: dataset.county[c].bureau[b].department[d].name
      }
    }]
  },
  onLoad: function(options) {
    if (options.length) {
      var op = {
        scene: "0,4,20"
      }
    } else {
      var op = options
    }
    var scene = decodeURIComponent(op.scene)
    const county = dataset.county[scene.split(",")[0]]
    const bureau = county.bureau[scene.split(",")[1]]
    const department = bureau.department[scene.split(",")[2]]
    if (department.img == '') {
      var p = true
    }else{
      var p = false
    }
    this.setData({
      cid: scene.split(",")[0],
      bid: scene.split(",")[1],
      did: scene.split(",")[2],
      pic: p,
      bureau: dataset.county[scene.split(",")[0]].bureau,
      department: dataset.county[scene.split(",")[0]].bureau[scene.split(",")[1]].department,
      latitude: department.latitude,
      longitude: department.longitude,
      img: department.img,
      markers: [{
        iconPath: '/image/location.png',
        scale: 28,
        latitude: department.latitude,
        longitude: department.longitude,
        width: 50,
        height: 50,
        title: department.name,
        callout: {
          content: department.name
        }
      }]
    })

  },
  goShow: function () {
    this.setData({ flag: false })
  },
  goHide: function () {
    this.setData({ flag: true })
  },
  countyChange: function(e) {
    const val = e.detail.value
    if (dataset.county[val].bureau[0].department[0].img == '') {
      var p = true
    } else {
      var p = false
    }
    this.setData({
      bureau: dataset.county[val].bureau,
      department: dataset.county[val].bureau[0].department,
      cid: val,
      bid: 0,
      did: 0,
      pic: p,
      latitude: dataset.county[val].bureau[0].department[0].latitude,
      longitude: dataset.county[val].bureau[0].department[0].longitude,
      img: dataset.county[val].bureau[0].department[0].img,
      markers: [{
        iconPath: '/image/location.png',
        scale: 28,
        latitude: dataset.county[val].bureau[0].department[0].latitude,
        longitude: dataset.county[val].bureau[0].department[0].longitude,
        width: 50,
        height: 50,
        title: dataset.county[val].bureau[0].department[0].name,
        callout: {
          content: dataset.county[val].bureau[0].department[0].name
        }
      }]
    })
  },
  bureauChange: function(e) {
    const val = e.detail.value
    const bureau = this.data.bureau
    const cc = this.data.cid
    if (dataset.county[cc].bureau[val].department[0].img == '') {
      var p = true
    } else {
      var p = false
    }
    this.setData({
      department: bureau[val].department,
      bid: val,
      did: 0,
      pic: p,
      latitude: dataset.county[cc].bureau[val].department[0].latitude,
      longitude: dataset.county[cc].bureau[val].department[0].longitude,
      img: dataset.county[cc].bureau[val].department[0].img,
      markers: [{
        iconPath: '/image/location.png',
        scale: 28,
        latitude: dataset.county[cc].bureau[val].department[0].latitude,
        longitude: dataset.county[cc].bureau[val].department[0].longitude,
        width: 50,
        height: 50,
        title: dataset.county[cc].bureau[val].department[0].name,
        callout: {
          content: dataset.county[cc].bureau[val].department[0].name
        }
      }]
    })
  },
  departmentChange: function(e) {
    const val = e.detail.value
    const cc = this.data.cid
    const bb = this.data.bid
    if (dataset.county[cc].bureau[bb].department[val].img == ''){
      var p = true
    } else {
      var p = false
    }
    this.setData({
      did: val,
      latitude: dataset.county[cc].bureau[bb].department[val].latitude,
      longitude: dataset.county[cc].bureau[bb].department[val].longitude,
      img: dataset.county[cc].bureau[bb].department[val].img,
      pic: p,
      markers: [{
        iconPath: '/image/location.png',
        scale: 28,
        latitude: dataset.county[cc].bureau[bb].department[val].latitude,
        longitude: dataset.county[cc].bureau[bb].department[val].longitude,
        width: 50,
        height: 50,
        title: dataset.county[cc].bureau[bb].department[val].name,
        callout: {
          content: dataset.county[cc].bureau[bb].department[val].name
        }
      }]
    })
  },
  goDetail: function() {
    wx.navigateTo({
      url: '../detail/detail?cid=' + this.data.cid + '&bid=' + this.data.bid + '&did=' + this.data.did
    })
  },
  goError: function() {
    wx.navigateTo({
      url: '../error/error?cid=' + this.data.cid + '&bid=' + this.data.bid + '&did=' + this.data.did
    })
  },
  goMap: function() {
    wx.openLocation({
      latitude: this.data.latitude,
      longitude: this.data.longitude,
      scale: 28,
      name: this.data.bureau[this.data.bid].name + '\n' + this.data.department[this.data.did].name,
      address: this.data.department[this.data.did].address
    })
  }
})
#!/user/bin/env python
# coding=utf-8
import requests
import json
import pymysql


def getStr(id, name):
    http = ''
    jpg = ''
    str1 = ''
    str2 = ''
    str3 = ''
    db = pymysql.connect("localhost", "root", "root", "address_book")
    cursor2 = db.cursor()
    cursor2.execute("SELECT id, name FROM bureau WHERE cid=" + id)
    bureaus = cursor2.fetchall()
    for bureau in bureaus:
        bureau_id = bureau[0]
        bureau_name = bureau[1]
        cursor3 = db.cursor()
        cursor3.execute(
            "SELECT id,name,address,img,latitude,longitude,duty,remark FROM department WHERE bid=" + str(bureau_id))
        departments = cursor3.fetchall()
        for department in departments:
            department_id = department[0]
            department_name = department[1]
            department_address = department[2]
            department_img = department[3]
            department_latitude = department[4]
            department_longitude = department[5]
            department_duty = department[6]
            department_remark = department[7]
            cursor4 = db.cursor()
            cursor4.execute(
                "SELECT id,name,address,latitude,longitude,phone,office FROM contact WHERE did=" + str(department_id))
            contacts = cursor4.fetchall()
            for contact in contacts:
                contact_id = contact[0]
                contact_name = contact[1]
                contact_address = contact[2]
                contact_latitude = contact[3]
                contact_longitude = contact[4]
                contact_phone = contact[5]
                contact_office = contact[6]
                str1 = str1 + '{name:"' + contact_name + '",phone:"' + contact_phone + '"},'
            str2 = str2 + '{name:"' + department_name + '",contact:[' + str1[:-1] + '],address:"' + department_address + '",img:"' + http + department_img + jpg + '",latitude:' + str(
                department_latitude) + ',longitude:' + str(
                department_longitude) + ',duty:"' + department_duty + '",remark:"' + department_remark + '"},'
            str1 = ''
        str3 = str3 + '{name:"' + bureau_name + '",department:[' + str2[:-1] + ']},'
        str2 = ''
    return 'export var ' + name + ' = [' + str3[:-1] + ']'


def getfile():
    filename = 'e:/weixin/pages/datasets.js'
    with open(filename, 'w', encoding='utf8') as f:
        f.write('var url = "/image/"\n')
        f.write(getStr('1', 'shizhi')+'\n')
        f.write(getStr('2', 'lixia')+'\n')
        f.write(getStr('3', 'shizhong')+'\n')
        f.write(getStr('4', 'huaiyin')+'\n')
        f.write(getStr('5', 'tianqiao')+'\n')
        f.write(getStr('6', 'licheng')+'\n')
        f.write(getStr('7', 'changqing')+'\n')
        f.write(getStr('8', 'zhangqiu')+'\n')
        f.write(getStr('9', 'pingyin')+'\n')
        f.write(getStr('10', 'jiyang')+'\n')
        f.write(getStr('11', 'shanghe')+'\n')
        f.write(getStr('12', 'gaoxin')+'\n')
        f.write(getStr('13', 'nanshan')+'\n')
        f.write(u"\n")
        f.write(u'export var county = [\n')
        f.write(u'{ name: "市直属", bureau: shizhi },\n')
        f.write(u'{ name: "历下区", bureau: lixia },\n')
        f.write(u'{ name: "市中区", bureau: shizhong },\n')
        f.write(u'{ name: "槐荫区", bureau: huaiyin },\n')
        f.write(u'{ name: "天桥区", bureau: tianqiao },\n')
        f.write(u'{ name: "历城区", bureau: licheng },\n')
        f.write(u'{ name: "长清区", bureau: changqing },\n')
        f.write(u'{ name: "章丘区", bureau: zhangqiu },\n')
        f.write(u'{ name: "平阴县", bureau: pingyin },\n')
        f.write(u'{ name: "济阳县", bureau: jiyang },\n')
        f.write(u'{ name: "商河县", bureau: shanghe },\n')
        f.write(u'{ name: "高新区", bureau: gaoxin },\n')
        f.write(u'{ name: "南山区", bureau: nanshan }\n')
        f.write(u']\n')


def geturl():
    url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx70be117b9b7b904a&secret=941ab224ca10231ddf4bd1afeeb130a7"
    r = requests.get(url)
    ACCESS_TOKEN = r.text
    return 'https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=' + ACCESS_TOKEN


def downPic(urls, scene, name):
    data = {
        'scene': scene,
        'page': 'pages/location/location',
        'width': 430,
        'auto_color': False,
        'line_color': {'r': '0', 'g': '0', 'b': '0'},
        'is_hyaline': False
    }
    res = requests.post(urls, data=json.JSONEncoder().encode(data))
    picture = open('c:/小程序码' + name + '.jpg', 'wb')
    picture.write(res.content)
    picture.close()


def getPic():
    db = pymysql.connect("localhost", "root", "root", "address_book")
    cursor1 = db.cursor()
    cursor1.execute("SELECT id, name FROM county")
    countis = cursor1.fetchall()
    c = 0
    for county in countis:
        county_id = county[0]
        county_name = county[1]
        cursor2 = db.cursor()
        cursor2.execute("SELECT id, name FROM bureau WHERE cid="+str(county_id))
        bureaus = cursor2.fetchall()
        b = 0
        for bureau in bureaus:
            bureau_id = bureau[0]
            bureau_name = bureau[1]
            cursor3 = db.cursor()
            cursor3.execute("SELECT id,name FROM department WHERE bid=" + str(bureau_id))
            departments = cursor3.fetchall()
            d = 0
            for department in departments:
                department_name = department[1]
                u = geturl()
                downPic(u, str(c) + ',' + str(b) + ',' + str(d), county_name + '-' + bureau_name + '-' + department_name)
                print('[' + str(c) + ',' + str(b) + ',' + str(d) + ']' + county_name + '-' + bureau_name + '-' + department_name + '--->OK')
                d = d + 1
            b = b + 1
        c = c + 1


getfile()



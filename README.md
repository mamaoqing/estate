[物业管理](#3)

[菜单接口](####菜单接口)

[添加菜单](#####添加菜单)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)

[菜单接口](####菜单接口)


### 物业管理

#### 菜单接口

##### 添加菜单

请求方式：post

url：/sdzy/sMenu/insertMenu?token=??????

请求参数：

|    参数名    | 参数类型 | 是否必传 | 说明               |
| :----------: | -------- | -------- | ------------------ |
|     name     | String   | true     | 菜单名             |
|   parentId   | Long     | true     | 父菜单id           |
| parentIdList | String   | true     | 所有的父级菜单列表 |
|     url      | String   | true     | 请求路径           |
|     type     | String   | true     | 类型               |
|    state     | String   | true     | 状态               |
|    remark    | String   | true     | 备注               |
|    token     | String   | true     | 登录唯一凭证       |

返回结果：



##### 修改菜单

请求方式：put

url：/sdzy/sMenu/updateMenu?token=??????

请求参数：

|    参数名    | 参数类型 | 是否必传 | 说明         |
| :----------: | -------- | -------- | ------------ |
|      id      | Long     | true     | 菜单di       |
|     name     | String   | true     | 菜单名称     |
|   parentId   | Long     | true     | 父菜单id     |
| parentIdList | String   | true     | 父菜单集合   |
|     url      | String   | true     | 请求连接     |
|     type     | String   | true     | 类型         |
|    state     | String   | true     | 状态         |
|    remark    | String   | true     | 备注         |
|    token     | String   | true     | 登录唯一凭证 |

返回结果：



##### 删除菜单

请求方式：delete

url：/sdzy/sMenu/{id}?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| :----: | :------: | :------: | ---------------- |
|   id   |   Long   |   true   | 需要删除的菜单id |
| token  |  String  |   true   | 登录唯一凭证     |

返回结果：



##### 菜单列表

请求方式： get

url：/sdzy/sMenu/get?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| :----: | -------- | -------- | ------------ |
| token  | String   | true     | 登录唯一凭证 |

返回结果：

{
    "code": 0,  
    "msg": "成功",
    "data": [
        {
            "id": 1,
            "name": "楼栋管理",
            "parentId": null,
            "parentIdList": null,
            "url": null,
            "type": null,
            "state": null,
            "remark": null,
            "createdAt": null,
            "createdBy": null,
            "createdName": null,
            "modifiedAt": null,
            "modifiedBy": null,
            "modifiedName": null,
            "isDelete": 0,
            "chirldMenuList": [ 
                {
                    "id": 4,
                    "name": "添加楼栋",
                    "parentId": 1,
                    "parentIdList": "1,",
                    "url": "www.baidu.com",
                    "type": null,
                    "state": null,
                    "remark": null,
                    "createdAt": null,
                    "createdBy": null,
                    "createdName": null,
                    "modifiedAt": null,
                    "modifiedBy": null,
                    "modifiedName": null,
                    "isDelete": 0,
                    "chirldMenuList": []
                }
            ]
        }
}

返回说明：

| 返回名称       | 类型    | 说明                           |
| -------------- | ------- | ------------------------------ |
| id             | Long    | 主键唯一id                     |
| name           | String  | 菜单名称                       |
| parentId       | Long    | 父菜单id                       |
| parentIdList   | String  | 父菜单的集合，包括所有的父菜单 |
| url            | String  | 访问连接 路径                  |
| type           | String  | 菜单类型                       |
| state          | String  | 菜单状态                       |
| remark         | String  | 备注                           |
| isDelete       | Integer | 是否删除                       |
| chirldMenuList | Array   | 子菜单                         |



##### 菜单单个

请求方式： get

url：/sdzy/sMenu/{id}?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| :----: | -------- | -------- | ------------ |
| token  | String   | true     | 登录唯一凭证 |
|   id   | Long     | true     | 菜单id       |

返回结果：



##### 设置角色菜单

请求方式： post

url：/sdzy/sRoleMenu/setRoleMenu?roleId=?&menuIds=?&token=??????

请求参数：

| 参数名  | 参数类型 | 是否必传 | 说明                        |
| :-----: | -------- | -------- | --------------------------- |
|  token  | String   | true     | 登录唯一凭证                |
| roleId  | Long     | true     | 角色id                      |
| menuIds | String   | true     | 菜单集合的字符串，用“,”隔开 |

返回结果：



#### 角色接口

##### 获取角色列表

请求方式： get

url：/sdzy/sRole/get?pageNo=?&size=?&token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传     | 说明         |
| :----: | -------- | ------------ | ------------ |
| pageNo | Integer  | true         | 当前页码     |
|  size  | Integer  | false 默认10 | 页面大小     |
| token  | String   | true         | 登录唯一凭证 |

返回结果：



##### 获取角色

请求方式： get

url：/sdzy/sRole/{id}?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| :----: | -------- | -------- | ---------------- |
|   id   | double   | true     | 查询角色的主键id |
| token  | String   | true     | 登录唯一凭证     |

返回结果：



##### 更新角色信息

请求方式： put

url：/sdzy/sRole/updateRole?token=??????&....=1

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| :----: | -------- | -------- | ------------ |
|   id   | Long     | true     | 主键id       |
|  name  | String   | true     | 菜单名称     |
|  type  | String   | true     | 类型         |
| compId | Long     | true     | 公司id       |
| state  | String   | true     | 状态         |
| remark | String   | false    | 备注         |
| token  | String   | true     | 登录唯一凭证 |

返回结果：



##### 添加角色信息

请求方式： post

url：/sdzy/sRole/insertRole?token=??????&....=1

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| :----: | -------- | -------- | ---------------- |
|  name  | String   | true     | 查询角色的主键id |
|  type  | String   | true     | 类型             |
| compId | Long     | true     | 公司id           |
| state  | String   | true     | 状态             |
| remark | String   | false    | 备注             |
| token  | String   | true     | 登录唯一凭证     |

返回结果：



##### 删除角色

请求方式： delete

url：/sdzy/sRole/{id}?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| :----: | -------- | -------- | ---------------- |
|   id   | double   | true     | 删除角色的主键id |
| token  | String   | true     | 登录唯一凭证     |

返回结果：

#### 用户接口

##### 获取用户

请求方式： get

url：/sdzy/sUser/{id}?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| :----: | -------- | -------- | ---------------- |
|   id   | double   | true     | 用户信息的主键id |
| token  | String   | true     | 登录唯一凭证     |

返回结果：



##### 获取用户列表

请求方式： get

url：/sdzy/sUser/listUser?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传    | 说明         |
| :----: | -------- | ----------- | ------------ |
| token  | String   | true        | 登录唯一凭证 |
| pageNo | Integer  | true        | 页码         |
|  size  | Integer  | false默认10 | 页面大小     |

返回结果：



##### 添加用户信息

请求方式： post

url：/sdzy/sUser/insertUser?token=??????&....=1

请求参数：

|  参数名  | 参数类型 | 是否必传 | 说明     |
| :------: | -------- | -------- | -------- |
|   name   | String   | true     | 用户姓名 |
|  compId  | String   | true     | 公司id   |
|  orgId   | Long     | true     | 组织id   |
| userName | String   | true     | 登录名   |
| password | String   | true     | 面密码   |
|   tel    | String   | true     | 电话     |
|   type   | String   | true     | 用户类型 |
|  state   | String   | true     | 用户状态 |
|  remark  | String   | false    | 备注     |

返回结果：



##### 更新用户信息

请求方式： put

url：/sdzy/sUser/updateUser?token=??????&....=1

请求参数：

|  参数名  | 参数类型 | 是否必传 | 说明     |
| :------: | -------- | -------- | -------- |
|    id    | Integer  | true     | 用户id   |
|   name   | String   | true     | 用户姓名 |
|  compId  | String   | true     | 公司id   |
|  orgId   | Long     | true     | 组织id   |
| userName | String   | true     | 登录名   |
| password | String   | true     | 面密码   |
|   tel    | String   | true     | 电话     |
|   type   | String   | true     | 用户类型 |
|  state   | String   | true     | 用户状态 |
|  remark  | String   | false    | 备注     |

返回结果：



##### 删除用户

请求方式： delete

url：/sdzy/sUser/{id}?token=??????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| :----: | -------- | -------- | ---------------- |
|   id   | double   | true     | 用户信息的主键id |
| token  | String   | true     | 登录唯一凭证     |

返回结果：



##### 设置用户角色

设置用户角色，根据用户id，将用户拥有的角色的id用“,”隔开

请求方式： post

url：/sdzy/sUser/setUserRole?token=??????

请求参数：

| 参数名  | 参数类型 | 是否必传 | 说明                  |
| :-----: | -------- | -------- | --------------------- |
| userId  | Long     | true     | 用户信息的主键id      |
| roleIds | String   | true     | 权限的集合，用“,”隔开 |
|  token  | String   | true     | 登录唯一凭证          |

返回结果：





#### 公司接口

##### 添加公司信息

请求方式： post

url：/sdzy/sCompany/insertCompany?token=??????&....=1

请求参数：

|          参数名          | 参数类型 | 是否必传 | 说明             |
| :----------------------: | -------- | -------- | ---------------- |
|           name           | String   | true     | 公司名称         |
|       abbreviation       | String   | true     | 简称             |
|    establishmentDate     | Date     | true     | 成立日期         |
|         compAddr         | String   | true     | 公司地址         |
|      registeredAddr      | String   | true     | 注册地址         |
|    registeredCapital     | String   | true     | 注册资本         |
| unifiedSocialCreditCode  | String   | true     | 统一社会信用代码 |
| taxpayerIdentificationNo | String   | true     | 纳税人识别号     |
|       registeredNo       | String   | false    | 工商注册号       |
|        ccompType         | String   | true     | 公司类型         |
|    businessTermBegin     | Date     | true     | 营业期限开始     |
|     businessTermEnd      | Date     | true     | 营业期限结束     |
|          state           | String   | true     | 状态             |
|           tel            | String   | true     | 电话             |
|          eMail           | String   | true     | 邮箱             |
|          remark          | String   | true     | 备注             |
|          token           | String   | true     | 登录唯一凭证     |

返回结果：



##### 更新公司信息

请求方式： post

url：/sdzy/sCompany/updateCompany?token=??????&....=1

请求参数：

|          参数名          | 参数类型 | 是否必传 | 说明             |
| :----------------------: | -------- | -------- | ---------------- |
|            id            | Long     | true     | 公司id           |
|           name           | String   | true     | 公司名称         |
|       abbreviation       | String   | true     | 简称             |
|    establishmentDate     | Date     | true     | 成立日期         |
|         compAddr         | String   | true     | 公司地址         |
|      registeredAddr      | String   | true     | 注册地址         |
|    registeredCapital     | String   | true     | 注册资本         |
| unifiedSocialCreditCode  | String   | true     | 统一社会信用代码 |
| taxpayerIdentificationNo | String   | true     | 纳税人识别号     |
|       registeredNo       | String   | false    | 工商注册号       |
|        ccompType         | String   | true     | 公司类型         |
|    businessTermBegin     | Date     | true     | 营业期限开始     |
|     businessTermEnd      | Date     | true     | 营业期限结束     |
|          state           | String   | true     | 状态             |
|           tel            | String   | true     | 电话             |
|          eMail           | String   | true     | 邮箱             |
|          remark          | String   | true     | 备注             |
|          token           | String   | true     | 登录唯一凭证     |

返回结果：



##### 公司列表

请求方式： get

url：/sdzy/sCompany/listCompany？pageNo=?&size=?

请求参数：

| 参数名 | 参数类型 | 是否必传     | 说明         |
| :----: | -------- | ------------ | ------------ |
| pageNo | Integer  | true         | 页码         |
|  size  | Integer  | false 默认10 | 页面大小     |
| token  | String   | true         | 登录唯一凭证 |

返回结果：



##### 单个公司信息

请求方式： get

url：/sdzy/sCompany/{id}?token=?????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| :----: | -------- | -------- | ------------ |
|   id   | Long     | true     | 公司信息的id |
| token  | String   | true     | 登录唯一凭证 |

返回结果：

##### 删除公司信息

请求方式： delete

url：/sdzy/sCompany/{id}?token=?????

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| :----: | -------- | -------- | ------------ |
|   id   | Long     | true     | 公司信息的id |
| token  | String   | true     | 登录唯一凭证 |

返回结果：



#### 社区接口

##### 添加社区信息

请求方式： post

url：sdzy/rCommunity/insertCommunity

请求参数：

|     参数名      | 参数类型 | 是否必传 | 说明          |
| :-------------: | -------- | -------- | ------------- |
|      name       | String   | true     | 小区名称      |
|     compId      | long     | true     | 物业公司id    |
|   provinceId    | long     |          | 省id          |
|     cityId      | long     |          | 市id          |
|   districtId    | long     |          | 县区id        |
|    province     | String   |          | 省            |
|   cityString    | String   |          | 市            |
|    district     | String   |          | 县区          |
| detailedAddress | String   | true     | 详细地址      |
|   buildedDate   | Date     |          | 建造日期      |
|   deliverDate   | Date     |          | 交付日期      |
|   serviceType   | String   |          | 服务类型      |
|   usableType    | String   |          | 用途类型      |
|      state      | String   | true     | 状态          |
|       tel       | String   |          | 电话          |
|      eMail      | String   |          | 邮箱          |
|  introduction   | String   |          | 社区简介/介绍 |
|     remark      | String   |          | 备注          |
|      token      | String   | true     | 登录唯一凭证  |

返回结果：



##### 修改社区信息

请求方式： put

url：sdzy/rCommunity/updateCommunity

请求参数：

|     参数名      | 参数类型 | 是否必传 | 说明          |
| :-------------: | -------- | -------- | ------------- |
|       id        | Long     | true     | 主键id        |
|      name       | String   | true     | 小区名称      |
|     compId      | Long     | true     | 物业公司id    |
|   provinceId    | long     |          | 省id          |
|     cityId      | Long     |          | 市id          |
|   districtId    | long     |          | 县区id        |
|    province     | String   |          | 省            |
|      city       | String   |          | 市            |
|    district     | String   |          | 县区          |
| detailedAddress | String   | true     | 详细地址      |
|   buildedDate   | date     |          | 建造日期      |
|   deliverDate   | date     |          | 交付日期      |
|   serviceType   | String   |          | 服务类型      |
|   usableType    | String   |          | 用途类型      |
|      state      | String   | true     | 状态          |
|       tel       | String   |          | 电话          |
|      eMail      | String   |          | 邮箱          |
|  introduction   | String   |          | 社区简介/介绍 |
|     remark      | String   |          | 备注          |
|      token      | String   | true     | 登录唯一凭证  |

返回结果：



##### 社区信息

请求方式： get

url：sdzy/rCommunity/{id}

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| ------ | -------- | -------- | ------------ |
| id     | long     | true     | 社区id       |
| token  | String   | true     | 登录唯一凭证 |

返回结果：





##### 社区列表信息

请求方式： get

url：sdzy/rCommunity/listCommunity

请求参数：

| 参数名 | 参数类型 | 是否必传     | 说明         |
| ------ | -------- | ------------ | ------------ |
| pageNo | Integer  | true         | 页码         |
| size   | Integer  | false 默认10 | 页面大小     |
| token  | String   | true         | 登录唯一凭证 |

返回结果：

##### 删除社区

请求方式： delete

url：sdzy/rCommunity/{id}

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明                       |
| ------ | -------- | -------- | -------------------------- |
| id     | Long     | true     | 需要删除的社区信息的主键id |
| token  | String   | true     | 登录唯一凭证               |

返回结果：

##### 社区权限

简述：在用户登录时，查看用户拥有的数据权限。

社区->社区分区->楼栋->单元->房间

请求方式： get

url：sdzy/rCommunity/userCommunity

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明         |
| ------ | -------- | -------- | ------------ |
| token  | String   | true     | 登录唯一凭证 |

返回结果：



##### 社区房间

简述：根据用户点击不同的范围，查询当前范围下的所有的房间，如果左侧菜单选中社区一级，就查询该社区下的所有房间，如果选中的事建筑一级，就查询该建筑下的所有房间	

请求方式： get

url：sdzy/rCommunity/getCommunityById

请求参数：

| 参数名     | 参数类型 | 是否必传     | 说明         |
| ---------- | -------- | ------------ | ------------ |
| token      | String   | true         | 登录唯一凭证 |
| pageNo     | Integer  | true         | 页码         |
| size       | Integer  | false 默认10 | 页面大小     |
| commId     | Long     | false        | 社区id       |
| commAreaId | Long     | false        | 分区id       |
| buildingId | Long     | false        | 建筑id       |
| unitId     | Long     | false        | 单元id       |
| roomNo     | String   | false        | 房间号码     |

返回结果：

### 地区接口

#### 获取全国的地区

简述：获取全国的地区

请求方式： get

url：/sdzy/rProvince/get

返回结果：

{

  "code": 0,

  "msg": "成功",

  "data": [

​    {

​      "id": 1,

​      "provinceCode": "370000",

​      "provinceName": "山东省",

​      "cityList": [

​        {

​          "id": 1,

​          "cityCode": "370100",

​          "cityName": "济南市",

​          "provinceId": 1,

​          "districtList": [

​            {

​              "id": null,

​              "districtCode": "370101",

​              "districtName": "历下区",

​              "provinceId": 1,

​              "cityId": 1

​            }

​          ]

​        }

​      ]

​    }

  ]

}




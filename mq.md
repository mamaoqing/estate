### 工作记录
#### 阿里云服务器安装mariadb数据库过程
> 1.现在的centos7一般都会自带mariadb数据库，所以我们在安装之前首先要查看一下自己的linux是否已经安装过，如果安装过，就要先删除再重新安装。
```shell
[root@VM_0_11_centos mysql]# rpm -qa | grep mariadb
[root@VM_0_11_centos mysql]# rpm -e --nodeps mariadb-libs-5.5.64-1.el7.x86_64
```

在我们删除之后，就可以进行安装了。
>2.我们可以直接通过yum命令来安装，简单快捷。
```shell
[root@VM_0_11_centos mysql]# yum install mariadb-server
```
因为mariadb-server 默认依赖安装mariadb，所以我们直接安装这个mariadb-server就行了
>3.安装完成之后，我们需要启动mariadb服务，并且将服务设置为开机自动启动。
```shell
[root@VM_0_11_centos mysql]# systemctl start mariadb  # 开启服务
[root@VM_0_11_centos mysql]# systemctl enable mariadb  # 设置为开机自启动服务
```
>4.对数据库进行配置
```shell
[root@VM_0_11_centos mysql]# mysql_secure_installation
```
```
Enter current password for root (enter for none):  # 输入数据库超级管理员root的密码(注意不是系统root的密码)，第一次进入还没有设置密码则直接回车
Set root password? [Y/n]  # 设置密码，y
New password:  # 新密码
Re-enter new password:  # 再次输入密码
Remove anonymous users? [Y/n]  # 移除匿名用户， y
Disallow root login remotely? [Y/n]  # 拒绝root远程登录，n，不管y/n，都会拒绝root远程登录
Remove test database and access to it? [Y/n]  # 删除test数据库，y：删除。n：不删除，数据库中会有一个test数据库，一般不需要
Reload privilege tables now? [Y/n]  # 重新加载权限表，y。或者重启服务也许
```
>5.配置完成后，测试一下是否能够成功登陆
```shell
[root@VM_0_11_centos mysql]# mysql -u root -p
Enter password:
```
这里输入配置时候设置的密码，看到下面内容说明已经成功了。
```
Welcome to the MariaDB monitor.  Commands end with ; 
Your MariaDB connection id is 8
Server version: 5.5.65-MariaDB MariaDB Server

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]>
```
>6.设置MariaDB字符集为utf-8

    6.1 修改/etc/my.cnf 文件，在  [mysqld]  标签下添加下面内容
    
    init_connect='SET collation_connection = utf8_unicode_ci'
    init_connect='SET NAMES utf8'
    character-set-server=utf8
    collation-server=utf8_unicode_ci
    skip-character-set-client-handshake
    
    6.2 修改/etc/my.cnf.d/client.cnf 文件，在  [client]  标签下添加下面内容
    default-character-set=utf8
    
    6.3 修改/etc/my.cnf.d/mysql-clients.cnf  文件，在  [mysql]  标签下添加
    default-character-set=utf8
    
    6.4 重启服务
```shell
[root@VM_0_11_centos mysql]# systemctl restart mariadb
```
>7.配置远程连接

mariadb数据库默认拒绝远程连接，如果在自己电脑上面需要通过navicat 等工具连接数据库的话，需要进行下面的配置。
    
    7.1 关闭防火墙
    [root@VM_0_11_centos mysql]# systemctl stop firewalld
    或者在不关闭防火墙的情况下，将3306端口开启
    [root@VM_0_11_centos mysql]# firewall-cmd --zone=public --add-port=3306/tcp --permanent
    然后重启防火墙服务
    [root@VM_0_11_centos mysql]# firewall-cmd --reload
    
    7.2 然后进入数据库修改mysql数据库下的user表
    [root@VM_0_11_centos mysql]# mysql -uroot -p 
    
    MariaDB [(none)]> use mysql; # 选中mysql数据库
    Reading table information for completion of table and column names
    You can turn off this feature to get a quicker startup with -A
    
    Database changed
    MariaDB [mysql]> select host,user from user;
    +------+------+
    | host | user |
    +------+------+
    | localhost    | root |
    | ::1  | root |
    | 127.0.0.1  | root |
    | VM_0_11_centos  | root |
    +------+------+
    将自己的用户名修改为%
    update user set host='%' where host='VM_0_11_centos';
    flush privileges;
    重启服务
    systemctl restart mariadb

>8.就可以进行远程连接了

`如果时云服务器的话，不能远程连接应该要去控制台做一个操作，很简单的，就不再这里写了。`

#### 阿里云服务器java环境安装

>1.首先去[官网](https://note.youdao.com/)下载相关的tar包。下载完成之后并且上传到服务器

>2.通过putty等工具连接到服务器然后进行解压。tar -xvf 你的jdk

>3.配置相关的环境变量
```shell
vim /etc/profile
# 在最下面添加下面的内容
export JAVA_HOME=/ 你的jdk解压目录，能看到bin的那层
export CLASSPATH=.:${JAVA_HOME}/jre/lib/rt.jar:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar
export PATH=$PATH:${JAVA_HOME}/bin
```
保存退出，然后执行source /etc/profile，使配置文件生效，到此完成！

#### Git



#### IDEA

##### 安装

1.[官网](https://www.jetbrains.com/idea/download/#section=windows)下载最新版的idea工具。

2.[下载激活工具](https://www.qjwsg.com/mq/Windows%E8%BD%AF%E4%BB%B6/idea%E7%A0%B4%E8%A7%A3/jetbrains-agent-2020.1.2.zip)

3.打开idea，选择试用，进入软件界面之后然后将激活工具/lib/jetbrains-agent.jar直接拖入软件界面，重启，安装需要的文件。ok！

##### 排除文件

file->settings->Editor->file types 

![image-20200724100656861](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200724100656861.png)

在最下面的lgnore files and folders：将不想展示的文件名添加，通过分号隔开。

##### 设置注释模板

1.新建一个Group

![image-20200724101246627](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200724101246627.png)

2.编写需要的模板样式

![image-20200724105430094](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200724105430094.png)

3.点击Edit variables设置默认值

![image-20200724105541287](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200724105541287.png)

4.选中java，点击ok

![image-20200724105620767](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200724105620767.png)

### centos7.6安装vue环境

1.下载nodejs的包，我这里下载的是xz结尾的文件node-v14.5.0-linux-x64.tar.xz

2.将文件上传到服务器上，然后依次执行

```shell
[root@home nodejs]# zx -d node-v14.5.0-linux-x64.tar.xz
[root@home nodejs]# tar -xf node-v14.5.0-linux-x64.tar
[root@home nodejs]# cd node-v14.5.0-linux-x64
[root@home nodejs]# pwd
[root@home nodejs]# /opt/nodejs
[root@home nodejs]# ln -s /opt/nodejs/node-v14.5.0-linux-x64/bin/node /usr/bin/node
[root@home nodejs]# ln -s /opt/nodejs/node-v14.5.0-linux-x64/bin/npm /usr/bin/npm
[root@home nodejs]# ln -s /opt/nodejs/node-v14.5.0-linux-x64/bin/npm /usr/bin/npx
[root@home nodejs]# node -v
[root@home nodejs]# npm
[root@home nodejs]# npx
# 设置淘宝源
[root@home nodejs]# npm install cnpm -g --registry=https://registry.npm.taobao.org
[root@home nodejs]# ln -s  /opt/nodejs/node-v14.5.0-linux-x64/lib/node_modules/cnpm/bin/cnpm  /usr/local/bin/cnpm
[root@home nodejs]# cnpm
```

### vue安装

```shell
[root@home nodejs]# cnpm install -g @vue/cli@3.0
```

### 开放防火墙端口

```shell
# 启动防火墙命令
[root@home nodejs]# systemctl start firewalld.service
# 关闭防火墙命令
[root@home nodejs]# systemctl stop firewalld.service
# 重启防火墙命令
[root@home nodejs]# systemctl restart firewalld.service
# 查看防火墙命令
[root@home nodejs]# systemctl status firewalld.service
# 开放端口，开放之后需要重启生效。
[root@home nodejs]# firewall-cmd --zone=public --add-port=8080/tcp --permanent
```

### centos7.6 jdk

1.先解压文件。

2.配置文件。

3.重启。完成

```shell
[root@home nodejs]# tar -xvf jdk-8u261-linux-x64.tar.gz
[root@home nodejs]# cd jdk1.8.0_261
[root@home nodejs]# pwd
[root@home nodejs]# /opt/java/jdk1.8.0_261
[root@home nodejs]# vim /etc/profile
# 在最后面添加
export JAVA_HOME= #/opt/java/jdk-8u261-linux-x64 ##### 注意这里的是jdk解压目录的路径。
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH
[root@home nodejs]# source /etc/profile
[root@home nodejs]# java
[root@home nodejs]# javac
[root@home nodejs]# java -version
```

### nginx安装

1.解压安装包，之后进入目录

```shell
[root@home nginx]# tar -xvf nginx-1.18.0.tar.gz
[root@home nginx]# cd nginx-1.18.0
```

2.nginx配置，推荐使用默认配置

```shell
[root@home nginx-1.18.0]# ./configure
```

![image-20200811153326481](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200811153326481.png)

发现遇到错误，没有pcre环境,需要安装环境

```shell
[root@home nginx-1.18.0]# yum install -y pcre pcre-devel
```

![image-20200811153602646](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200811153602646.png)

没有zlib环境

```shell
[root@home nginx-1.18.0]# yum install -y zlib zlib-devel
```

![image-20200811153755669](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200811153755669.png)

安装完成。

3.编译安装

```shell
[root@home nginx-1.18.0]# make install
```

4.查看安装位置，进入目录启动nginx

```shell
[root@home nginx-1.18.0]# whereis nginx
nginx: /usr/local/nginx

[root@home nginx-1.18.0]# cd /usr/local/nginx/sbin/
[root@home sbin]# ./nginx
```

5.访问ip地址，出现下图表示成功

![image-20200811154254845](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200811154254845.png)

> ./nginx 开启 
>
> ./nginx -s stop 停止
>
>  ./nginx -s quit 
>
> ./nginx -s reload

6.修改配置文件

```shell
[root@home sbin]# cd /usr/local/nginx/conf
[root@home sbin]# vi nginx.conf
# 修改完记得重启
[root@home sbin]# ./nginx -s reload
# 设置开机启动nginx
[root@home sbin]#  vi /etc/rc.local
# 在文件中加入 /usr/local/nginx/sbin/nginx
```



### springboot拦截器

拦截器需要实现HandlerInterceptor接口。

```java
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SUserService userService;

    // 登录路径
    @Value("${loginPath}")
    private String loginPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("验证用户是否登录");
        try {

            String token = String.valueOf(request.getParameter("token"));
            SUser user = (SUser) redisTemplate.opsForValue().get(token);
//            SUser user = (SUser) request.getSession().getAttribute(token);
            if (user != null) {
                return true;
            }
//            response.sendRedirect(loginPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```

将拦截器配置到项目中

```java
@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor getSessionInterceptor() {
        return new LoginInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(getSessionInterceptor());
        // 拦截路径 /** 表示所有的路径都拦截
        registration.addPathPatterns("/**");
        // 不拦截路径 多个用 "," 隔开
//        registration.excludePathPatterns("","","","");
    }
}
```



### springboot从配置文件取值

```java
@Value("${qr_code.port}")

@Component
@ConfigurationProperties(prefix = "qr_code")
public class Student {
	private String charset;
}
```



### 自定义异常类

自定义异常类需要自己编写一个异常类，然后继承自RuntimeException

```java
public class BillException extends RuntimeException{

    private Integer code;

    public BillException(Integer code,String msg){

        super(msg);
        this.code = code;
    }

    public Integer getCode(){
        return  code;
    }
}
```

捕获异常

```java
@Slf4j
@ControllerAdvice
public class HandlerExceptionUtil {

    @ResponseBody // 返回一个json
    @ExceptionHandler(BillException.class)
    public Result handlerException(BillException billException){

        log.error(billException.getMessage());

        return ResultUtil.error(billException.getMessage(),billException.getCode());
    }
}

```

### 日志记录

```properties
logging.level.com.estate=info
# 指定文件
logging.file.path=springLog
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n
```

logback配置文件

```xml
<?xml version="1.0" ?>
<configuration debug="false">
    <!--  控制台日志  -->
    <appender name="consoleLog" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ---> %p - %c[%L] %msg%n</pattern>
        </encoder>
    </appender>

    <!--  配置info日志文件  -->
    <appender name="fileInfoLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- info 日志，只记录INFO WARN 信息，不记录error信息 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>DENY</onMatch>
            <onMismatch>ACCEPT</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>logs/info.%d.log</FileNamePattern>
        </rollingPolicy>
        <encoder>
            <pattern>%d -->%p %c[%L] --> %msg%n</pattern>
            <!--            <pattern>%d{yyyy-MM-dd HH:mm:ss}&ndash;&gt; [%thread] %-5level %logger{50} &ndash;&gt; %msg%n</pattern>-->
        </encoder>
    </appender>

    <!--  配置error日志文件  -->
    <appender name="fileErrorLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤只记录error日志 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <!-- d:时间 c:类名 L:行号 msg:日志信息 n:换行 p:日志级别 -->
            <pattern>%d -->%p %c[%L] --> %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>logs/error.%d.log</FileNamePattern>
        </rollingPolicy>
    </appender>
    <!-- 全局日志级别 -->
    <root level="info">
        <appender-ref ref="consoleLog"/>
        <appender-ref ref="fileInfoLog"/>
        <appender-ref ref="fileErrorLog"/>
    </root>

</configuration>
```

### 获取header中的值

```java
public Result deleteMenu(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sMenuService.deleteMenuById(id, token));
}
```

### java时间格式转换

```java
/**
 * 成立日期
 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date establishmentDate;

@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date establishmentDates;
```

<<@JsonFormat>>注解是将数据库时间格式转换为字符串格式，一般用于后台给前台传值，JsonFormat注解一般会少8小时，需要在注解中的timezone="GMT+8"

<!@DateTimeFormat>注解是将字符串格式转换为时间格式，一般用于前台给后台传值。

### mybatis-plus使用

在数据库中没有字段，但是在实体类中需要用到的属性，需要加@TableField(exist = false) 注解

多表查询的时候，可以参考

```java
@Repository
public interface SUserMapper extends BaseMapper<SUser> {
    Page<SUser> listUser(Page page,@Param("ew") QueryWrapper queryWrapper);
}
@Service
public Page<SUser> listUser(String token, Map<String, String> map) {
        Page<SUser> sUserPage = null;
        Page<SUser> page = new Page<>(pageNo, size);
        QueryWrapper<SUser> userServiceQueryWrapper = new QueryWrapper<>();
        // 拼接条件，多个表中字段名相同的时候，在前面加表前缀
        userServiceQueryWrapper.eq(!StringUtils.isEmpty(map.get("compId")), "aa.comp_id", map.get("compId"));
        userServiceQueryWrapper.eq(!StringUtils.isEmpty(map.get("orgId")), "aa.org_id", map.get("orgId"));
        userServiceQueryWrapper.eq(!StringUtils.isEmpty(map.get("userName")), "aa.user_name", map.get("userName"));
        userServiceQueryWrapper.eq(!StringUtils.isEmpty(map.get("name")), "aa.comp_id", map.get("name"));
        userServiceQueryWrapper.eq("aa.comp_id", user.getCompId());
    	userServiceQueryWrapper.and(qw->qw.eq().or().eq())
        sUserPage = userMapper.listUser(page,userServiceQueryWrapper);
    mapper中的条件
    // ${ew.customSqlSegment}
        
        return sUserPage;
    }
```

### vue自定义组件，字典选择组件

```vue
<child @child1="checkIn" :distId="useTypeDistId" :distName="useType" :change="useTypeChange"></child>
<!--checkIn 回调函数 distId 字典表即s_dist 中的id。distName对应实体类中的字段名，如果页面只有一个地方引用可以不用。change默认选中的值 -->

父组件中添加
<script>
	import child from "./child"
    export default {
        components: {
            child
        },
        methods:{
             checkIn(value,name){
                if(name === 'usableType'){
                    this.form.usableType = value;
                }
                if(name === 'commState'){
                    this.form.state = value;
                }
            },
        }
    }
    
</script>
```

### vue中的数据类型转换

/*整数*/parseInt(string)

/*分数*/parseFloat(string)

/*Number*/Number(val)

### java导出excel文件

```java
package com.estate.sdzy.common.excel;

import com.alibaba.druid.util.StringUtils;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.sdzy.system.mapper.SDictMapper;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出excel
 */
@Slf4j
@Component
public class ExportExcel extends ExcelUtil {

    private static SDictMapper dictMapper;

    private static RParkingSpaceMapper parkingSpaceMapper;
    /**
     *
     * @param response 返回
     * @param fileName 文件名称
     * @param className 类名，完全限定名
     * @param dataList 数据集合
     * @param auth 导出人
     * @throws ClassNotFoundException 1
     * @throws NoSuchMethodException 1
     * @throws IllegalAccessException 1
     * @throws InvocationTargetException 1
     */
    public static void writeOut(HttpServletResponse response, String fileName, String className,List<?> dataList,String auth) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> list = ExcelUtil.getClassFirld(className);
        Class<?> c = Class.forName(className);
        Map<String, String> map = getDistfield(className);
        HSSFWorkbook workbook = createHSSFWorkbook(fileName,list,auth,dataList,c,map);
        //将excel的数据写入文件
        ByteArrayOutputStream fos = null;
        byte[] retArr = null;
        fos = new ByteArrayOutputStream();
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            workbook.write(fos);
            retArr = fos.toByteArray();
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            //解决中文名称乱码问题,要保存的文件名
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "iso-8859-1") + ".xls");
            os.write(retArr);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件导出异常。");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *
     * @param fileName 文件名
     * @param list 表头集合
     * @param auth 导出人
     * @param dataList 数据集合
     * @param c 类
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static HSSFWorkbook createHSSFWorkbook(String fileName, List<String> list, String auth, List<?> dataList,Class c,Map<String, String> map) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // 创建表格
        HSSFWorkbook wb = new HSSFWorkbook();
        //默认宽，默认高
        HSSFSheet sheet = setSheetBaseInfoExcel(fileName, 15, 40, wb);
        // 设置样式
        HSSFCellStyle style = getColumnTopStyle(wb);
        // 冻结三行,即excel表的前三行不随鼠标滚动而滚动
        sheet.createFreezePane(0, 3, 0, 3);

        // 创建第一行
        HSSFRow rowm = sheet.createRow(0);
        // 第一行的内容,开始设置第一行的第一个单元格
        HSSFCell cell = rowm.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(fileName);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, list.size() - 1));
        // 创建第二行
        HSSFRow rowm2 = sheet.createRow(1);
        // 第二行的内容,开始设置第二行的第一个单元格
        HSSFCell cell2 = rowm2.createCell(0);
        cell2.setCellStyle(style);
        cell2.setCellValue(auth);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, list.size() - 1));
        // 循环创建第三行内容,表头
        Integer rows = 999;
        if(!CollectionUtils.isEmpty(dataList)){
            rows = dataList.size();
        }
        HSSFRow rowm3 = sheet.createRow(2);
        for (int i = 0, count = list.size(); i < count; i++) {
            String value = list.get(i);
            String dist = map.get(value);
            HSSFCell cellTitle = rowm3.createCell(i);
            cellTitle.setCellStyle(style);
            cellTitle.setCellValue(value);
            // 如果dist不是空的，表示该字段是下拉列表
            if(!StringUtils.isEmpty(dist)){
                // 创建新的下拉框从第三行开始如果数据集合长度为空,就到999行.否则就到集合大小的长度
                CellRangeAddressList regions = new CellRangeAddressList(3,3+rows,i,i);
                List<String> dictNames = dictMapper.listDictName(dist);
                String [] arr = new String[dictNames.size()];
                for (int j = 0; j < dictNames.size(); j++) {
                    arr[j] = dictNames.get(j);
                }
                DVConstraint constraint = DVConstraint.createExplicitListConstraint(arr);
                HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);
                sheet.addValidationData(dataValidation);
            }
        }
        if (dataList != null) {
            createData(dataList,sheet,style,c,list);
        }
        return wb;
    }
    /**
     *  创建数据内容
     * @param dataList 数据集合
     * @param sheet
     * @param style 样式
     * @param c 类
     * @param list 表头集合
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void createData(List<?> dataList, HSSFSheet sheet, HSSFCellStyle style, Class c,List<String> list) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 数据集是从表格的第四行开始的
        Field[] fields = c.getDeclaredFields();
        int row = 3;
        for (int i = 0, count = dataList.size(); i < count; i++) {
            HSSFRow rowm = sheet.createRow(row + i);
            for (Field field : fields) {
                // 如果字段存在ExcelAnnotation注解，表示是需要导出的内容，就获取里面的值
                if(field.isAnnotationPresent(ExcelAnnotation.class)){
                    // 获取字段属性
                    String type = field.getGenericType().toString();
                    // 字段名
                    String name = field.getName();
                    Method m = c.getMethod("get" +upperCase1th(name));
                    Object value = m.invoke(dataList.get(i));               
                    // 获取注解属性
                    String aname =  field.getAnnotation(ExcelAnnotation.class).value();
                    for (int j=0;j<list.size();j++){
                        if(aname.equals(list.get(j))){
                            HSSFCell cell = rowm.createCell(j);
                            cell.setCellStyle(style);
                            setCellValue(cell,value,type,null);
                            break;
                        }
                    }
                }
            }
        }
    }
    /**
     * 初始化表格
     *
     * @param excelName 表名称
     * @param columWith 列宽
     * @param rowHight  行高
     * @param wb        表格对象.
     * @return wb
     */
    private static HSSFSheet setSheetBaseInfoExcel(String excelName, int columWith, int rowHight, HSSFWorkbook wb) {
        // 创建文件名称为excelName的excel文件
        HSSFSheet sheet = wb.createSheet(excelName);
        // 设置默认宽高
        sheet.setDefaultColumnWidth(columWith);
        sheet.setDefaultRowHeightInPoints(rowHight);
        return sheet;
    }
    /**
     * 设置样式
     *
     * @param workbook
     * @return
     */
    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 设置字体名字
        font.setFontName("宋体");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(true);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    public static String upperCase1th(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    @Autowired
    public void setDictMapper(SDictMapper dictMapper) {
        ExportExcel.dictMapper = dictMapper;
    }
    @Autowired
    public void setParkingSpaceMapper(RParkingSpaceMapper parkingSpaceMapper) {
        ExportExcel.parkingSpaceMapper = parkingSpaceMapper;
    }
}

```

```java
package com.estate.sdzy.common.excel;


import com.estate.exception.BillException;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public abstract class ExcelUtil {

    static final String STRING = "class java.lang.String";
    static final String DOUBLE = "class java.lang.Double";
    static final String INTEGER = "class java.lang.Integer";
    static final String BOOLEAN = "class java.lang.Boolean";
    static final String DATE = "class java.util.Date";

    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            log.error("文件不存在");
            throw new BillException(BillExceptionEnum.FILE_NOTFOUND_ERROR);
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            log.error("文件错误");
            throw new BillException(BillExceptionEnum.FILE_TYPE_ERROR);
        }
    }

    /**
     * 获取实体类中的所有需要导出的字段名称
     *
     * @param className 完全限定名
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> getClassFirld(String className) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        Field[] fields = aClass.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                String value = annotation.value();
                boolean export = annotation.export();
                boolean master = annotation.master();
                String dist = annotation.dist();
                System.out.println(field.getName());
                System.out.println(value);
                System.out.println(dist);
                System.out.println(master);
                System.out.println(export);
                System.out.println("----------");
                list.add(value);
            }
        }
        return list;
    }

    /**
     * 根据不同类型设置表格内的值
     * @param cell 表格对象
     * @param value 需要填写的值
     * @param type 实体中的属性类型.
     * @param fmt 如果存在时间格式，就按照时间格式转，否则就是 yyyy-MM-dd的格式
     */
    public static void setCellValue(HSSFCell cell, Object value, String type, String fmt){
        if(null == value){
            cell.setCellValue("");
        }else{
            if(INTEGER.equals(type)){
                cell.setCellValue((Integer)value);
            }
            if(DOUBLE.equals(type)){
                cell.setCellValue((Double)value);
            }
            if(BOOLEAN.equals(type)){
                cell.setCellValue((Boolean)value);
            }
            if(STRING.equals(type)){
                cell.setCellValue(String.valueOf(value));
            }
            if(DATE.equals(type)){
                String date = new SimpleDateFormat(fmt != null ? fmt:"yyyy-MM-dd").format((Date)value);
                cell.setCellValue(date.toString());
            }
        }
    }

}

```



### java导入excel文件

```java
package com.estate.sdzy.common.excel;

import com.alibaba.fastjson.JSON;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导入excel
 */
@Slf4j
@Component
public class ImportExcel extends ExcelUtil {

    private static RParkingSpaceMapper parkingSpaceMapper;

    /**
     * 解析表格中的数据
     *
     * @param file
     * @param className
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Object> getFileData(MultipartFile file, String className) throws IOException, ClassNotFoundException {

        Class<?> c = Class.forName(className);
        Field[] fields1 = c.getDeclaredFields();
        Annotation[] annotations = c.getAnnotations();
        List<String> fields = new ArrayList<>();
        List<String> masterField = new ArrayList<>();
        for (Field field : fields1) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)){
                fields.add(field.getName());
                if(field.getAnnotation(ExcelAnnotation.class).master()){
                    masterField.add(field.getName());
                }
            }
        }
        Integer compId = 0;
        Integer commId = 0;
        Integer areaId = 0;
        if (fields.contains("compName")) {
            compId = fields.indexOf("compName");
        }
        if (fields.contains("commName")) {
            commId = fields.indexOf("commName");
        }
        if (fields.contains("areaName")) {
            areaId = fields.indexOf("areaName");
        }

        List<Object> list = new ArrayList<>();
        // excel文件,
        /*
         * 1.先循环获取到每一行的信息
         * 2.然后将每一行的信息在循环，获取每列的信息即每个单元格的内容
         * */
        Workbook workBook = getWorkBook(file);
        if (null != workBook) {
            for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                Sheet sheetAt = workBook.getSheetAt(i);
                if (null == sheetAt) {
                    continue;
                }
                int firstRowNum = sheetAt.getFirstRowNum();
                int lastRowNum = sheetAt.getLastRowNum();
                for (int rowNum = firstRowNum + 3; rowNum < lastRowNum + 1; rowNum++) {
                    // 获取到具体的行
                    Row row = sheetAt.getRow(rowNum);
                    if (null == row) {
                        continue;
                    }
                    short firstCellNum = row.getFirstCellNum();
                    short lastCellNum = row.getLastCellNum();
                    if (lastCellNum > 0) {
                        Map<String, Object> map = new HashMap<>(16);
                        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            String s = fields.get(cellNum);
                            if(masterField.contains(s) && StringUtils.isEmpty(getCellValue(cell))){
                                throw new BillException(BillExceptionEnum.FILE_MASTER_FIELDNO_ERROR);
                            }
                            map.put(s, getCellValue(cell));
                            if(cellNum == compId){
                                log.info("公司名称是:{}",getCellValue(cell));
                                Long compIdByName = parkingSpaceMapper.getCompIdByName(getCellValue(cell));
                                map.put("compId",compIdByName);
                            }
                            if(cellNum == commId){
                                log.info("社区称是:{}",getCellValue(cell));
                                Long commIdByName = parkingSpaceMapper.getCommIdByName(getCellValue(cell));
                                map.put("commId",commIdByName);
                            }
                            if(cellNum == areaId){
                                log.info("分区名称是:{}",getCellValue(cell));
                                Long commIdByName = parkingSpaceMapper.getAreaIdByName(getCellValue(cell));
                                map.put("commAreaId",commIdByName);
                            }
                        }
                        Object aClass = JSON.parseObject(JSON.toJSONString(map), c);
                        list.add(aClass);
                    }

                }

            }
        }

        return list;
    }


    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        CellType cellTypeEnum = cell.getCellTypeEnum();
        switch (cellTypeEnum) {
            case STRING:
                cellValue = String.valueOf(cell);
                break;
            case NUMERIC:
                cellValue = cell.toString();
                break;
            case BLANK:
                cellValue = "";
                break;
        }
        return cellValue;

    }

    @Autowired
    public void setParkingSpaceMapper(RParkingSpaceMapper parkingSpaceMapper) {
        ImportExcel.parkingSpaceMapper = parkingSpaceMapper;
    }
}

```

需要用到的jar

```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
</dependency>
```



### springboot项目遇到的问题

#### 1.项目可以正常跑，但是打包就提示找不到符号问题

​	可能的原因：项目中只生成了可以运行的jar，没有生成依赖的jar。需要在springboot的配置下面添加<classifier>exec</classifier>，再次打包会生成两个jar，一个用于依赖一个用于运行项目。

![image-20200904112452704](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200904112452704.png)

```xml
<plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <includeSystemScope>true</includeSystemScope>
                    <mainClass>com.estate.timedtask.costrule.CrontabCostRule</mainClass>
                    <fork>true</fork>
                    <classifier>exec</classifier>
                </configuration>
            </plugin>
```





### springcould 配置

```xml
<!--项目中引入jar-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>xxxxxx</version>
</dependency>
```

```xml
# 在applicatoin.properties中添加下面的属性
spring.application.name=你的微服务名
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848 # nacos服务地址，不需要加后面的/nacos
```

```java
// 在springboot的启动类上面加上@EnableDiscoveryClient注解
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProviderApplication {

    // 微服务之间的通讯通过这个RestTemplate来完成，需要放入spring容器中， @LoadBalanced注解找到相应的服务名
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
	public static void main(String[] args) {
		SpringApplication.run(NacosProviderApplication.class, args);
	}
}
```

### springboot+sharding多数据源

```xml
<!-- for spring namespace -->
<dependency>
    <groupId>io.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-namespace</artifactId>
    <version>3.1.0</version>
</dependency>
<!-- for spring boot -->
<dependency>
    <groupId>io.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>3.1.0</version>
</dependency>

# 所有的数据源，“,”隔开
sharding.jdbc.datasource.names=db0,db1
# 第一个数据源，主数据库，用于增删改
sharding.jdbc.datasource.db0.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.db0.driverClassName=org.mariadb.jdbc.Driver
sharding.jdbc.datasource.db0.url=jdbc:mysql://192.168.0.114:3306/wygl?useUnicode=true&characterEncoding=utf8mb4&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT
sharding.jdbc.datasource.db0.username=root
sharding.jdbc.datasource.db0.password=sdzy@123
sharding.jdbc.datasource.db0.maxActive=100
sharding.jdbc.datasource.db0.maxWait=10000
#sharding.jdbc.datasource.db0.maxIdle=15
sharding.jdbc.datasource.db0.initialSize=1
sharding.jdbc.datasource.db0.timeBetweenEvictionRunsMillis=60000
sharding.jdbc.datasource.db0.minEvictableIdleTimeMillis=300000
sharding.jdbc.datasource.db0.validationQuery=select 1

# 第二个数据源 从数据库，用于查询
sharding.jdbc.datasource.db1.type=com.alibaba.druid.pool.DruidDataSource
sharding.jdbc.datasource.db1.driverClassName=org.mariadb.jdbc.Driver
sharding.jdbc.datasource.db1.url=jdbc:mysql://192.168.0.114:3306/wygl?useUnicode=true&characterEncoding=utf8mb4&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT
sharding.jdbc.datasource.db1.username=root
sharding.jdbc.datasource.db1.password=sdzy@123
sharding.jdbc.datasource.db1.maxActive=100
sharding.jdbc.datasource.db1.maxWait=10000
#sharding.jdbc.datasource.db1.maxIdle=15
sharding.jdbc.datasource.db1.initialSize=1
sharding.jdbc.datasource.db1.timeBetweenEvictionRunsMillis=60000
sharding.jdbc.datasource.db1.minEvictableIdleTimeMillis=300000
sharding.jdbc.datasource.db1.validationQuery=select 1
```

### springboot test

如果项目中的test类中没有添加@Test注解，就会出现java.lang.Exception: No runnable methods这个问题。

### 数据库表数据id从0开始

```
truncate table 你的表名 
```

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

url：/sdzy/sMenu/get

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

#### 获取公司

请求方式：get

url：/sdzy/sCompany/getComp

简述：根据用户类型，区分管理员和超级管理员，超级管理员可以查看全部的公司，管理员只能查看自己的公司



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



### 公司联系人

#### 联系人列表

请求方式： get

url：/sdzy/sCompLink/{id}

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明   |
| ------ | -------- | -------- | ------ |
| id     | Long     | true     | 公司id |

返回结果：

{

​        "id": 1, 主键

​        "compId": 1, 公司id

​        "name": "zhangsan", 联系人姓名

​        "job": "java", 工作

​        "birthday": "1995-10-01", 生日

​        "likes": "ç¯®ç", 爱好

​        "addr": "å±±ä¸çæµåå¸", 地址

​        "state": "1", 状态

​        "tel": "18866881121", 电话

​        "remark": "å¤æ³¨", 备注

​        "email": "107@qq.com" 邮箱

​      }



#### 添加联系人

请求方式： post

url：/sdzy/sCompLink/insertCompLink

请求参数：

| 参数名   | 参数类型 | 是否必传 | 说明       |
| -------- | -------- | -------- | ---------- |
| compId   | Long     | true     | 公司id     |
| name     | String   | true     | 联系人姓名 |
| job      | String   | false    | 职务       |
| birthday | String   | false    | 生日       |
| likes    | String   | false    | 爱好       |
| addr     | String   | true     | 地址       |
| tel      | String   | true     | 电话       |
| eMail    | String   | true     | 邮箱       |
| remark   | String   | false    | 备注       |

返回结果：



#### 修改联系人

请求方式： put

url：/sdzy/sCompLink/updateCompLink

请求参数：

| 参数名   | 参数类型 | 是否必传 | 说明             |
| -------- | -------- | -------- | ---------------- |
| id       | Long     | true     | 需要修改的主键id |
| compId   | Long     | true     | 公司id           |
| name     | String   | true     | 联系人姓名       |
| job      | String   | false    | 职务             |
| birthday | String   | false    | 生日             |
| likes    | String   | false    | 爱好             |
| addr     | String   | true     | 地址             |
| tel      | String   | true     | 电话             |
| eMail    | String   | true     | 邮箱             |
| remark   | String   | false    | 备注             |

返回结果：



#### 删除联系人

请求方式： delete

url：/sdzy/sCompLink/{id}

请求参数：

| 参数名 | 参数类型 | 是否必传 | 说明             |
| ------ | -------- | -------- | ---------------- |
| id     | Long     | true     | 需要删除的主键id |

返回结果：



### 公司组织机构

#### 组织机构列表

请求方式： get

url：/sdzy/sOrg/listOrg

请求参数：

null

返回结果：

{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "id": 1,
            "name": "财务部",
            "compId": 1,
            "parentId": null,
            "parentIdList": null,
            "state": null,
            "remark": "添加公司自动生成机构信息",
            "createdAt": "2020-08-04T11:20:21.000+0000",
            "createdBy": null,
            "createdName": null,
            "modifiedAt": "2020-08-04T11:20:21.000+0000",
            "modifiedBy": null,
            "modifiedName": null,
            "isDelete": 0,
            "childList": [
                {
                    "id": 4,
                    "name": "会计",
                    "compId": 1,
                    "parentId": 1,
                    "parentIdList": null,
                    "state": null,
                    "remark": "添加公司自动生成机构信息",
                    "createdAt": "2020-08-05T02:11:27.000+0000",
                    "createdBy": null,
                    "createdName": null,
                    "modifiedAt": "2020-08-05T02:11:27.000+0000",
                    "modifiedBy": null,
                    "modifiedName": null,
                    "isDelete": 0,
                    "childList": [
                        {
                            "id": 12,
                            "name": "学徒会计",
                            "compId": 1,
                            "parentId": 4,
                            "parentIdList": null,
                            "state": null,
                            "remark": "1",
                            "createdAt": null,
                            "createdBy": null,
                            "createdName": null,
                            "modifiedAt": null,
                            "modifiedBy": null,
                            "modifiedName": null,
                            "isDelete": 0,
                            "childList": []
                        }
                    ]
                },
                {
                    "id": 5,
                    "name": "出纳",
                    "compId": 1,
                    "parentId": 1,
                    "parentIdList": null,
                    "state": null,
                    "remark": "添加公司自动生成机构信息",
                    "createdAt": "2020-08-05T02:19:25.000+0000",
                    "createdBy": null,
                    "createdName": null,
                    "modifiedAt": "2020-08-05T02:19:25.000+0000",
                    "modifiedBy": null,
                    "modifiedName": null,
                    "isDelete": 0,
                    "childList": []
                },
                {
                    "id": 6,
                    "name": "记账",
                    "compId": 1,
                    "parentId": 1,
                    "parentIdList": null,
                    "state": null,
                    "remark": "1",
                    "createdAt": null,
                    "createdBy": null,
                    "createdName": null,
                    "modifiedAt": null,
                    "modifiedBy": null,
                    "modifiedName": null,
                    "isDelete": 0,
                    "childList": []
                }
            ]
        }
    ]
}

S

#### 添加组织机构

请求方式： post

url：/sdzy/sOrg/insertOrg

请求参数：



#### 社区信息

##### 添加社区信息

请求方式： post

url：/sdzy/sOrg/insertOrg

请求参数：





































































### 物业管理费用

#### 数据库表F_COST_RULE关键字段：

|          字段名           |  类型   |      说明      |                             备注                             |
| :-----------------------: | :-----: | :------------: | :----------------------------------------------------------: |
|       cost_type_id        | bigint  |    收费类型    |    关联f_cost_type表的主键id，收费类型。例：物业管理收入     |
|       cost_item_id        | bigint  |  费用规则名称  | 关联f_cost_item表，费用规则名称，具体明细。例：车位物业服务费、商铺、写字楼消防 |
|           name            | varchar |  费用标准名称  |                                                              |
|          comp_id          | bigint  |     公司id     |                 与s_comany表id关联。公司信息                 |
|        begin_date         |  date   |    开始时间    |                       费用开始计费时间                       |
|         end_date          |  date   |    结束时间    |                       费用结束计费时间                       |
|      billing_method       | varchar |    计费方式    |                                                              |
|        price_type         | varchar |    价格类型    |                                                              |
|           price           | decimal |      价格      |                                                              |
|        price_unit         | decimal |    价格单位    |                                                              |
|   is_liquidated_damages   |   int   |  是否有违约金  |                       是否存在违约金，                       |
| liquidated_damages_method | varchar | 违约金计算方式 |                                                              |
|        bill_cycle         | varchar |    账单周期    |            自动生成账单的周期，定时任务，自动执行            |
|         bill_day          | varchar |     出账天     |                                                              |

#### 中间表F_COST_RULE_ROOM

|    字段名     |  类型   |         说明         |                             备注                             |
| :-----------: | :-----: | :------------------: | :----------------------------------------------------------: |
| cost_rule_id  | bigint  |    费用标准的主键    |                      关联具体的费用标准                      |
| property_type | varchar | 物业类型，房产停车位 |      具体的关联的物业类型，有房产，停车位，公寓，厂房等      |
| propertty_id  | bigint  |     具体房产的id     | 如果property_type是停车位，那这个就是停车位的id如果时房产，就是房产的id |
|      id       | bigint  |      id主键自增      |                                                              |

#### 账单表F_BILL

|    字段名    |  类型   |        说明        |                备注                |
| :----------: | :-----: | :----------------: | :--------------------------------: |
|   bill_no    | varchar | 账单id，账单号唯一 |      由时间和公司信息生成唯一      |
|   room_id    | bigint  |       房间id       |                                    |
| 账单生成时间 |         |                    |                                    |
|   是否逾期   |         |                    |                                    |
|   是否付款   |         |                    |                                    |
| 逾期费用规则 |         |                    |                                    |
|    price     | decimal |      账单价格      |                                    |
|    remark    | varchar |        说明        | 具体物业单位在那个时间段的物业费用 |

#### 账单明细表

|   字段名   |  类型   |      说明      |              备注              |
| :--------: | :-----: | :------------: | :----------------------------: |
|  bill_id   | bigint  |   f_bill的id   |                                |
|   项目名   | varchar | 费用项目的名称 |                                |
|   price    | decimal |      价格      |                                |
| 优惠后金额 | decimal |   优惠后的钱   | 类似于淘宝拍下商品后卖家改价格 |

#### 账单物业表

| 字段名         | 类型   | 说明       | 备注                             |
| -------------- | ------ | ---------- | -------------------------------- |
| bill_id        | bigint | f_bill的id |                                  |
| room_id        | bigint | 物业的id   | 可能是停车位，房子，公寓、厂房等 |
| 账单生成时间   | date   |            |                                  |
| 是否逾期       |        |            |                                  |
| 逾期产生的费用 |        |            |                                  |
| 是否付款       |        |            |                                  |



> `F_COST_RULE 表跟r_parking_space、r_room等表格通过F_COST_RULE_ROOM关联，一个费用标准可以同时生效多个物业单位，即F_COST_RULE 与F_COST_RULE_ROOM是一对多的关系。系统根据用户选择的账单周期，自动生成该标准下所有物业单位的账单，包括账单明细，存储在F_BILL表中。根据账单物业表给没一个业主生成该业主的一个账单，通过业主信息中保存的业主的微信open_id定向的给业主推送他的该账单。`

#### 物业标准

##### 查询条件：

公司名称（下拉，超级管理员可以查看到所有公司的列表，其他只能看到自己公司。）

费用标准名称 输入框，后台查询用like。

收费类型 （下拉 f_cost_type表中的name）

费用明细（下拉f_cost_item表中的name）

##### 列表展示：

公司名、标准名称、收费类型、费用明细、开始时间、结束时间、计费方式、价格类型、价格单位、是否有违约金、账单周期、出账天数

##### 添加 、修改：

公司（下拉，不是超级管理员只能查看自己公司）`*`

收费类型（下拉，选中公司的f_cost_type 的列表）`*`

费用明细（下拉，选中公司的f_cost_item的列表）`*`

标准名称（输入框）`*`

开始时间（时间选择器）

结束时间（时间选择器）

计费方式（下拉）

计费方式（输入框） `*`

价格类型

价格 `*` 输入框

价格单位 （输入框）

是否有违约金（下拉） `*`

添加修改的时候，默认当前登录人问添加修改人时间是当前时间。

##### 删除：

用户确认之后，进行逻辑删除，将所有的f_cost_rule_room中的数据也进行逻辑删除







### 费用标准

数据库表：f_cost_rule 、f_cost_rule_room、f_bill_period、f_chargeoff_period	
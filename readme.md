#代码生成器使用说明

## 1.代码生成器使用场景
1.1 本工具主要作用是根据数据库的表设置,快速生成业务代码.适合有一套的开发规范,定制好生成器后快速生成代码.  
1.2 主要解决大量写重复代码的问题,开发原则之一是生成**易于修改**的代码(即生成的代码可直接使用,同时为了满足业务方便添加或者修改逻辑)
1.3 不解决设计即开发的问题,虽然会尽量保证生成的代码可直接调用运行,但是设计即开发还应该开发项目特定的个性化工具,包括诸如权限管理,报表,一对多等其它要考虑的因素
    所以生成器不主要解决该问题
## 2.使用对象
可以项目整体使用,一次生成所有代码.
也可以开发人员单独生成自己需要的表的业务代码,按需生成(推荐这种方式)

## 3.实现说明
生成代码主要有两种形式:
1.抄袭mybatis-generator的底层代码,使用java类编写generator生成代码(由于使用java代码,灵活度较高)
2.使用freemarker模板引擎作为生成器生成代码

## 3.使用方法
1.配置cfg.json(参考4掌握如何使用配置文件)
2.目前没有图形界面,只能main方法直接执行.开始类为:com.stuto.generator.Start
该类有两个方法:
一个是generate(),基础的生成代码的方法
一个是generateTw(),针对TW系统的代码风格做了定制化的修改(其中的service,serviceImpl,api使用的模板引擎生成器)

请查看generate()方法的注释了解使用方法

## 4.配置文件说明
工具为了使用方便,使用全局配置文件可灵活配置生成的内容,形式等
~~~
{
  // 个人信息
  "individual":{
    // 配置姓名,在生成的文件头上添加创建人等信息
    "name":"yongqiang.zhang",
    // 邮箱,同上
    "email":"yongqiang.zhang@elitesland.com",
    // 配置生成的文件是否覆盖,比如第一次生成文件有First.java,当再执行main方法生成First.java的时候是否将原文件覆盖
    // 建议:如果生成的文件位置为实际项目的位置该项设置为false,因为生成的代码你自己做了修改之后不被覆盖将丢失,此时会生成First.java.1这样的文件
    "overwriteEnabled":"true",
    // 生成的文件的编码
    "fileEncoding":"UTF-8",
    // 你的项目在你电脑中的目录,代码生成将以此作为根目录
    "targetProject":"C:\\software\\IdeaWork\\telework-svr",
    // 如果是java文件,该项有意义,就是class类的包名的基础部分
    "rootPackage":"com.el.tw.app",
    // 如果不设置业务表的模块,默认为此模块的.
    "defaultModule":"operation"

  },
  // 所有的generaotr的配置
  "generators":{
  // java类实现的生成器
    "topClassGenerators":{
      // 实体类生成器
      "PoGenerator":{
        // 生成器描述
        "desc":"生成数据库字段对应的po实体类对象",
        // 生成器写文件时针对项目根路径的再添加一层前缀目录,目前用于业务代码和测试代码的分开输出.
        "resourcePath":"src/main/java",
        // 生成器生成的代码放在哪个包下,举例就是 api|service|mapper|domain 等
        "package":"entity",
        // 配置类名前缀
        "prefix":"Tw",
        // 配置类名后缀
        "suffix":"Entity",
        // 配置继承哪个类
        "extends":"com.el.tw.app.core.TwPayload"
      },
      "ViewGenerator":{
        "desc":"生成数据库字段对应的View实体类对象",
        "resourcePath":"src/main/java",
        "package":"domain.view",
        "prefix":"Tw",
        "suffix":"View",
        "extends":""
      },
      "QueryGenerator":{
        "desc":"生成数据库字段对应的Query实体类对象",
        "resourcePath":"src/main/java",
        "package":"domain.query",
        "prefix":"Tw",
        "suffix":"Query",
        "extends":"com.el.core.domain.PagingQuery"
      },
      "MapperGenerator":{
        "desc":"生成数据库表对应的mapper",
        "resourcePath":"src/main/java",
        "package":"mapper",
        "prefix":"Tw",
        "suffix":"Mapper",
        "extends":"",
        "sqlBuilderInsertIgnore":"create_user_id,create_time,modify_user_id,modify_time,del_flag",
        "sqlBuilderUpdateIgnore":"create_user_id,create_time,modify_user_id,modify_time,del_flag"
      },
      "MapperTestGenerator":{
        "desc":"生成数据库表对应的mapper测试类",
        "resourcePath":"src/test/java",
        "package":"mapper",
        "prefix":"Tw",
        "suffix":"MapperTest",
        "extends":"com.el.tw.TwTest"
      }
    },
    // 模板引擎生成器
    "templateGenerators":{
      // service 生成器
      "ServiceGenerator":{
        // 生成器描述
        "desc":"生成service",
        // 要生成的文件类型,考虑可能要生成js或者html等文件
        "fileType":".java",
        // 同java类生成器
        "resourcePath":"src/main/java",
        // 模板路径(根路径再resource下)
        "templatePath":"service.ftl",
        // 同java类生成器
        "package":"service",
        // 同java类生成器
        "prefix":"Tw",
        // 同java类生成器
        "suffix":"Service",
        "extends":""
      },
      "ServiceImplGenerator":{
        "desc":"生成serviceImpl",
        "fileType":".java",
        "resourcePath":"src/main/java",
        "templatePath":"serviceImpl.ftl",
        "package":"service",
        "prefix":"Tw",
        "suffix":"ServiceImpl",
        "extends":""
      },
      "ApiGenerator":{
        "desc":"生成API(即controller层代码)",
        "fileType":".java",
        "resourcePath":"src/main/java",
        "templatePath":"api.ftl",
        "package":"api",
        "prefix":"Tw",
        "suffix":"Api",
        "extends":""
      }
    },
    // 对java类名的特殊处理器.原由:tw 的表以T开头, 例如T_PERSON,实际生成类名时应该去掉T
    "classNameHandler":"com.stuto.generator.generators.tw.TwClassNameHandler"

  },
  // 数据库的相关设置
  "db":{
    // 数据库的类型,由于没安装Oracle,目前只mysql能用,oracle未测试
    "dbType":"mysql",
    // 表的选取
    "tableCatalog":{
      "catalog":"",
      "schema":"",
      // 具体要选则的表名
      "chooseTables":"T_PERSON_COPY",
      // 忽略的表
      "ignoredTables":"",
      // 表名模糊匹配选表
      "nameLike":"%",
      // 忽略的表字段
      "ignoredColumns":"tenant_id"
    },
    // 相关规则
    "rules":{
      // 数据库的逻辑删除列
      "delFlagColumn":"del_flag"
    },
    // mysql相关配置
    "mysql":{
      "connection":{
        // 驱动类
        "driverClass":"com.mysql.jdbc.Driver",
        // 地址
        "url":"jdbc:mysql://localhost:3306/el_telework?useSSL=false&characterEncoding=utf8",
        // 用户名
        "username":"root",
        // 密码
        "password":"admin"
      },
      // mysql数据库字段类型与java实体类的字段类型映射
      "fieldTypeMap":{
        "varchar":"java.lang.String",
        "date":"java.time.LocalDate",
        "time":"java.time.LocalTime",
        "datetime":"java.time.LocalDateTime",
        "timestamp":"java.time.LocalDateTime",
        "bigint":"java.lang.Long",
        "decimal":"java.math.BigDecimal",
        "tinyint":"java.lang.Byte",
        "bit":"java.lang.Byte",
        "smallint":"java.lang.Short",
        "int":"java.lang.Integer",
        "float":"java.lang.Float",
        "double":"java.lang.Double",
        "char":"java.lang.String",
        "text":"java.lang.String",
        "varbinary":"byte[]"
      }

    }
  }

}

~~~

## FAQ
1.

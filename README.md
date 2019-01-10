# sqltool
# 功能
1、可替换现有数据库客户端工具  
2、支持dml、ddl  
3、在同时管理多个类型数据库同时，避免切换各种客户端  
4、支持表名、字段名自动补全  
5、支持使用别名自动补全  
6、支持sql格式化  
7、支持查看ddl语句  
8、支持表格编辑  
9、支持关联pdm中描述，减少查数据边翻看pdm麻烦，特别对新接手项目的开发人员  
10、支持跨数据库迁移数据（单表迁移到多表、多表迁移到单表），可在项目升级数据库时使用  
11、支持迁移时脚本保存  
12、支持生成随机数据  

# 支持数据库类型
1、oracle  
2、db2  
3、mysql  
4、sqlserver  
5、redis  

# 使用
1、配置自己的数据库类型 窗口>首选项>JDBC
![image](https://github.com/448697783/sqltool/blob/master/METADATA/20190110181636.png)
2、填写信息
填写名称（描述）->选择数据库类型->选择驱动->ip、端口、实例名、用户名、密码->pdm（可选填写）->登陆
![image](https://raw.githubusercontent.com/448697783/sqltool/master/METADATA/20190110181846.png)
3、加载表名、列名及pdm中关联信息
点击刷新
![image](https://raw.githubusercontent.com/448697783/sqltool/master/METADATA/20190110182011.png)
4、执行
打开新查询窗口->选择查询那个库->选择需要执行语句->点击执行语句查询
![image](https://github.com/448697783/sqltool/blob/master/METADATA/20190110182116.png?raw=true)
5、直接点击X然后点击√可直接删除数据，没有主键的会让不选择一个列去删除
6、直接编辑表格点击√可直接修改

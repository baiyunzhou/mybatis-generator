# mybatis-generator
## 目的
- 解决多个项目DAO层代码生成风格混乱

## 现状
- 使用自己的生成器生成代码
- 用maven插件的方式生成
- 集成到业务代码生成
- 配置不同、风格不同
- 不具有良好的扩展性，重复生成替换难度大

## 解决方案
- 统一代码生成规则、代码结构、生成方式、使用规范

## 生成规则
- 生成基础的增删改查
- 生成Mapper、MapperExt、xml、ext xml、实体类代码

## 生成方式
- 独立代码生成项目，中心化代码生成

## 代码结构
- 表对应实体，放在model目录下
- Mapper和MapperExt对应数据库操作，放在dao目录下
- xml文件放在resources的mapper目录下

## 使用规范
- 不允许修改Ext之外的生成代码

## 使用方法
- git clone https://github.com/baiyunzhou/mybatis-generator.git
- 导入到IDE
- 修改resources/generator.properties
	- 修改数据库连接、用户名、密码
	- 修改数据库名称
	- 修改生成位置
	- 修改author.name
- 运行Generator的main方法生成代码
- 刷新项目目录
- 复制生成的代码
- 运行Cleaner的main方法清理生成的代码
	

## CustomCommand

### 简介

自定义命令插件,可以执行动作链


### 使用方式

在`CustomCommand/commands.yml`中定义命令,通过`/customcommand reload`命令重载以加载到服务器中

#### 命令配置格式

```yaml
命令名字: #支持中文
  aliases: #命令别名
    - '命令别名1'
    - '命令别名2'
  cooldown: 100 #命令的冷却时间,以tick为单位
  cooldown_message: '&cexample命令正在冷却中' #命令冷却时的提醒消息,如果没有此项将不会发送消息
  permission: '命令的权限'
  actions: #命令执行的动作
    - 'action1'
    - 'action2'
```

#### 命令可用动作

`tell 消息文本`向玩家发送一条消息

`actionbar 消息文本`向玩家发送一条actionbar消息

`title 消息文本`向玩家发送一条title消息

`subtitle 消息文本`向玩家发送一条subtitle消息

`command 命令`以执行此命令的玩家身份执行一条命令

`console 命令`服务器后台执行一条命令

`delay 数量`延迟指定tick后执行

`runIf {condition:"条件",action:"满足条件时执行的动作",condition_type:"条件类型"}`满足某条件时执行指定动作



#### 注意事项

1. 自定义命令执行的动作中若为玩家执行,会解析指定玩家的PlaceholderAPI变量
2. 动作command需要有玩家作为执行载体,若为控制台执行,将会报错
3. runIf条件类型为可选项,默认为解析PlaceholderAPI变量
4. 条件类型目前只支持`placeholderapi`
5. 动作中可以使用`<arg_参数序号>`的形式来引用命令参数,若参数序号超过命令参数长度或小于0,`<arg_参数序号>`将会被替换为空文本
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
  argument_settings: #命令的参数校验设置
    min_argument:
      min: 1
      hint: '&c参数不足'
    max_argument:
      max: 1
      hint: '&c参数过长'
    type_settings:
      0:
        type: number
        min: 0
        max: 10
        hint: '&c%argument%不是一个有效的数字'
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

#### runIf中的条件类型列表

`common`

能够解析placeholderapi变量,对于直接返回true或者false的变量,可以直接使用,也可以使用形如`xx运算符xx`的格式进行简单的逻辑判断

其中,运算符支持`>`,`>=`,`<`,`<=`,`==/=`几种,在两侧值都为数字时,会比较大小,而如果一方不是数字,`>`和`>=`将会判断左侧是否包含右侧,区别在于,`>`要求左侧长度必须大于右侧,而`>=`可以两字符串相等;`<`和`<=`反之

需要注意的是,运算符左右两侧的所有内容都将被用于比较,也就是说假设你的比较内容是`a == a`,他们看起来相同,但实际上比较内容是`a `和` a`,从而导致结果为false

#### 注意事项

1. 自定义命令执行的动作中若为玩家执行,会解析指定玩家的PlaceholderAPI变量
2. 动作command需要有玩家作为执行载体,若为控制台执行,将会报错
3. runIf条件类型为可选项,默认为`common`
4. 动作中可以使用`<arg_参数序号>`的形式来引用命令参数,若参数序号超过命令参数长度或小于0,`<arg_参数序号>`将会被替换为空文本
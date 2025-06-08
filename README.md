简体中文 | [English](README_EN.md)

# PostSpawner

![版本](https://img.shields.io/github/v/release/postyizhan/PostSpawner?color=blue&label=版本)
![Minecraft](https://img.shields.io/badge/Minecraft-1.13+-green)
![语言](https://img.shields.io/badge/语言-简体中文|English-orange)

PostSpawner 是一个轻量级的 Minecraft 刷怪笼管理插件，提供了对刷怪笼的挖掘和放置的管理功能。

## 📚 功能特性

- ✅ **刷怪笼管理**：支持精准采集和普通工具挖掘刷怪笼
- ✅ **多语言支持**：内置中文和英文语言文件，可轻松扩展
- ✅ **丰富的动作系统**：支持命令执行、物品掉落、音效播放等多种动作
- ✅ **钩子系统**：兼容 PlaceholderAPI、ItemsAdder、MythicMobs、NeigeItems、Oraxen 等插件
- ✅ **灵活配置**：完全可自定义的配置和消息系统
- ✅ **更新检查**：提供可关闭的更新检查功能

## 💻 安装

1. 下载最新的 PostSpawner.jar
2. 将插件放入服务器的 plugins 文件夹中
3. 启动/重启服务器
4. 编辑 plugins/PostSpawner/config.yml 文件进行自定义配置
5. 使用 `/ps reload` 命令重载插件配置

## 🔧 命令

| 命令 | 描述 | 权限 |
|------|------|------|
| `/ps reload` | 重载插件配置和语言文件 | `postspawner.command.reload` |
| `/ps version` | 显示版本信息 | `postspawner.command.version` |
| `/ps help` | 显示帮助信息 | `postspawner.command.help` |
| `/ps update` | 检查更新 | `postspawner.command.update` |

## 🔒 权限

| 权限 | 描述 | 默认 |
|------|------|------|
| `postspawner.use.place` | 放置刷怪笼权限 | 所有玩家 |
| `postspawner.use.break` | 破坏刷怪笼权限 | 所有玩家 |
| `postspawner.command.reload` | 重载命令权限 | OP |
| `postspawner.command.version` | 版本命令权限 | 所有玩家 |
| `postspawner.command.help` | 帮助命令权限 | 所有玩家 |
| `postspawner.command.update` | 更新命令权限 | OP |
| `postspawner.admin` | 包含所有权限 | OP |

## 🛠️ 动作

PostSpawner 提供了丰富的动作系统，可以在配置文件中使用以下格式的动作：

格式说明： `[]` 为必填参数，`()` 为选填参数

| 动作语法 | 描述 | 示例 |
|----------|------|------|
| `[command] [命令]` | 让玩家执行命令 | `[command] spawn` |
| `[op] [命令]` | 临时给予玩家OP权限执行命令 | `[op] gamemode creative` |
| `[console] [命令]` | 在控制台执行命令 | `[console] broadcast 有人挖掘了刷怪笼！` |
| `[sound] [音效] (音量) (音调)` | 为玩家播放音效 | `[sound] BLOCK_ANVIL_LAND 1.0 1.5` |
| `[message] [文本]` | 向玩家发送消息 | `[message] &a你成功挖掘了刷怪笼！` |
| `[title] [主标题] (副标题)` | 向玩家展示标题 | `[title] &a挖掘成功 &7获得了一个刷怪笼` |
| `[drop_monster_spawner]` | 掉落被破坏的刷怪笼 | `[drop_monster_spawner]` |
| `[drop] [物品ID] (数量)` | 在指定位置掉落物品 | `[drop] DIAMOND 5` |
| `[give] [物品ID] (数量)` | 给予玩家物品 | `[give] DIAMOND_SWORD 1` |
| `[buff] [药水效果] (持续时间秒) (等级)` | 给予玩家药水效果 | `[buff] SPEED 30 2` |
| `[money] [操作] [数量]` | 控制玩家经济 | `[money] give 100`, `[money] take 200`, `[money] set 50` |
| `[points] [操作] [数量]` | 控制玩家点券 | `[points] give 100`, `[points] take 200`, `[points] set 50` |

特别的，创造模式下破坏刷怪笼不会执行任何动作。

## 📦 物品库支持

在动作中使用 `[drop]` 和 `[give]` 时，支持以下物品库的物品：

| 物品库 | 格式 | 示例 |
|-------|------|------|
| Minecraft | `minecraft:物品ID [数量]` | `minecraft:diamond_sword`, `diamond:5` |
| ItemsAdder | `itemsadder:物品ID [数量]` | `itemsadder:ruby_sword` |
| MMOItems | `mmoitems:类型:ID [数量]` | `mmoitems:SWORD:EXCALIBUR` |
| NeigeItems | `neigeitems:物品ID [数量]` | `neigeitems:test_item` |
| Oraxen | `oraxen:物品ID [数量]` | `oraxen:amethyst_helmet` |
| MythicMobs | `mythicmobs:物品ID [数量]` | `mythicmobs:special_bow` |
| SX-Item | `sxitem:物品ID [参数1:参数2...] [数量]` | `sxitem:magic_wand:arg1:arg2` |
| Zaphkiel | `zaphkiel:物品ID [数量]` | `zaphkiel:legendary_axe` |
| CraftEngine | `craftengine:物品ID [数量]` | `craftengine:magic_staff` |
| AzureFlow | `azureflow:物品ID [数量]` | `azureflow:wizard_robe` |
<!-- | Ratziel(未实现) | `ratziel:物品ID [数量]` | `ratziel:jiao_long` |
| ItemTools(未实现) | `itemtools:物品ID [数量]` | `itemtools:Apple` | -->

简化格式：
- 默认物品源为minecraft，可以省略，例如：`diamond:5` 等价于 `minecraft:diamond:5`
- 如果数量为1，可以省略，例如：`itemsadder:ruby_sword` 等价于 `itemsadder:ruby_sword 1`

### 📊 变量

在动作中可以使用以下内建变量：

- `%player%` - 玩家名称
- `%block_x%` - 方块X坐标
- `%block_y%` - 方块Y坐标
- `%block_z%` - 方块Z坐标
- `%block_world%` - 方块所在世界
- `%entity_type%` - 实体类型

此外，如果安装了 PlaceholderAPI，还可以使用 PlaceholderAPI 变量。

## 📫 联系

- [![](https://img.shields.io/badge/QQ群-611076407-54B4EF?logo=data:image/png;base64,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)](https://8aka.cn/qq) <-点击加入

- [![](https://img.shields.io/discord/1342805340839870514.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.com/invite/jN4Br8uhSS) <-点击加入

- [![](https://img.shields.io/badge/GitHub-postyizhan-181717?style=plastic&logo=github&logoColor=white)](https://github.com/postyizhan) <- Home Page

## 📜 许可证

本插件采用 [GNU General Public License v2.0](LICENSE) 许可证。

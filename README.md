# PostSpawner

![版本](https://img.shields.io/github/v/release/postyizhan/PostSpawner?color=blue&label=版本)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21+-green)
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
| Ratziel(未实现) | `ratziel:物品ID [数量]` | `ratziel:jiao_long` |
| ItemTools(未实现) | `itemtools:物品ID [数量]` | `itemtools:Apple` |

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

## 📜 许可证

本插件采用 [GNU General Public License v2.0](LICENSE) 许可证。

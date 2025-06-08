[简体中文](README) | English

# PostSpawner

![Version](https://img.shields.io/github/v/release/postyizhan/PostSpawner?color=blue&label=Version)
![Minecraft](https://img.shields.io/badge/Minecraft-1.13+-green)
![Language](https://img.shields.io/badge/Language-简体中文|English-orange)

PostSpawner is a lightweight Minecraft plugin for managing monster spawners, providing control over spawner mining and placement.

## 📚 Features

- ✅ **Spawner Management**: Supports silk touch and normal tool mining of spawners
- ✅ **Multi-language Support**: Built-in Chinese and English language files, easily extendable
- ✅ **Rich Action System**: Supports various actions like command execution, item drops, sound effects, etc.
- ✅ **Hook System**: Compatible with PlaceholderAPI, ItemsAdder, MythicMobs, NeigeItems, Oraxen and more
- ✅ **Flexible Configuration**: Fully customizable config and message system
- ✅ **Update Check**: Optional update checking feature

## 💻 Installation

1. Download the latest PostSpawner.jar
2. Place the plugin in your server's plugins folder
3. Start/Restart the server
4. Edit plugins/PostSpawner/config.yml for customization
5. Use `/ps reload` to reload plugin configuration

## 🔧 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/ps reload` | Reload plugin config and language files | `postspawner.command.reload` |
| `/ps version` | Show version information | `postspawner.command.version` |
| `/ps help` | Show help information | `postspawner.command.help` |
| `/ps update` | Check for updates | `postspawner.command.update` |

## 🔒 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `postspawner.use.place` | Place spawners | All players |
| `postspawner.use.break` | Break spawners | All players |
| `postspawner.command.reload` | Reload command | OP |
| `postspawner.command.version` | Version command | All players |
| `postspawner.command.help` | Help command | All players |
| `postspawner.command.update` | Update command | OP |
| `postspawner.admin` | Includes all permissions | OP |

## 🛠️ Actions

PostSpawner provides a rich action system. Use the following action formats in config:

Format: `[]` for required parameters, `()` for optional parameters

| Action Syntax | Description | Example |
|---------------|-------------|---------|
| `[command] [command]` | Make player execute command | `[command] spawn` |
| `[op] [command]` | Temporarily give player OP to execute command | `[op] gamemode creative` |
| `[console] [command]` | Execute command from console | `[console] broadcast Someone mined a spawner!` |
| `[sound] [sound] (volume) (pitch)` | Play sound for player | `[sound] BLOCK_ANVIL_LAND 1.0 1.5` |
| `[message] [text]` | Send message to player | `[message] &aYou successfully mined a spawner!` |
| `[title] [title] (subtitle)` | Show title to player | `[title] &aMining Success &7Got a spawner` |
| `[drop_monster_spawner]` | Drop the broken spawner | `[drop_monster_spawner]` |
| `[drop] [itemID] (amount)` | Drop item at location | `[drop] DIAMOND 5` |
| `[give] [itemID] (amount)` | Give item to player | `[give] DIAMOND_SWORD 1` |
| `[buff] [potion] (duration) (level)` | Give player potion effect | `[buff] SPEED 30 2` |
| `[money] [action] [amount]` | Control player economy | `[money] give 100`, `[money] take 200`, `[money] set 50` |
| `[points] [action] [amount]` | Control player points | `[points] give 100`, `[points] take 200`, `[points] set 50` |

Note: Breaking spawners in creative mode won't trigger any actions.

## 📦 Item Library Support

When using `[drop]` and `[give]` actions, the following item libraries are supported:

| Library | Format | Example |
|---------|--------|---------|
| Minecraft | `minecraft:itemID [amount]` | `minecraft:diamond_sword`, `diamond:5` |
| ItemsAdder | `itemsadder:itemID [amount]` | `itemsadder:ruby_sword` |
| MMOItems | `mmoitems:type:ID [amount]` | `mmoitems:SWORD:EXCALIBUR` |
| NeigeItems | `neigeitems:itemID [amount]` | `neigeitems:test_item` |
| Oraxen | `oraxen:itemID [amount]` | `oraxen:amethyst_helmet` |
| MythicMobs | `mythicmobs:itemID [amount]` | `mythicmobs:special_bow` |
| SX-Item | `sxitem:itemID [arg1:arg2...] [amount]` | `sxitem:magic_wand:arg1:arg2` |
| Zaphkiel | `zaphkiel:itemID [amount]` | `zaphkiel:legendary_axe` |
| CraftEngine | `craftengine:itemID [amount]` | `craftengine:magic_staff` |
| AzureFlow | `azureflow:itemID [amount]` | `azureflow:wizard_robe` |

Simplified formats:
- Default source is minecraft (can be omitted), e.g. `diamond:5` equals `minecraft:diamond:5`
- If amount is 1, can be omitted, e.g. `itemsadder:ruby_sword` equals `itemsadder:ruby_sword 1`

### 📊 Variables

You can use these built-in variables in actions:

- `%player%` - Player name
- `%block_x%` - Block X coordinate
- `%block_y%` - Block Y coordinate
- `%block_z%` - Block Z coordinate
- `%block_world%` - Block world
- `%entity_type%` - Entity type

Additionally, if PlaceholderAPI is installed, you can use PlaceholderAPI variables.

## 📫 Contact

- [![](https://img.shields.io/badge/QQ群-611076407-54B4EF?logo=data:image/png;base64,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)](https://8aka.cn/qq) <-点击加入

- [![](https://img.shields.io/discord/1342805340839870514.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.com/invite/jN4Br8uhSS) <-Click to join

- [![](https://img.shields.io/badge/GitHub-postyizhan-181717?style=plastic&logo=github&logoColor=white)](https://github.com/postyizhan) <- Home Page

## 📜 License

This plugin is licensed under [GNU General Public License v2.0](LICENSE).

package com.github.postyizhan.command

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

/**
 * 命令管理器，负责处理插件的命令
 */
class CommandManager(private val plugin: PostSpawner) : CommandExecutor, TabCompleter {

    /**
     * 注册插件命令
     */
    fun registerCommands() {
        val pluginCommand = plugin.getCommand("ps")
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this)
            pluginCommand.tabCompleter = this
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sendHelpMessage(sender)
            return true
        }

        when (args[0].lowercase()) {
            "reload" -> {
                if (!sender.hasPermission("postspawner.command.reload")) {
                    sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.no-permission")))
                    return true
                }
                
                plugin.reload()
                sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.reload")))
            }
            
            "version" -> {
                if (!sender.hasPermission("postspawner.command.version")) {
                    sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.no-permission")))
                    return true
                }
                sender.sendMessage(MessageUtil.color("&3Post&bSpawner &8| &fv${plugin.description.version}"))
                sender.sendMessage(MessageUtil.color("&7A lightweight Minecraft plugin for managing Monster Spawner "))
                sender.sendMessage(MessageUtil.color("&7"))
                sender.sendMessage(MessageUtil.color("&f• Author: &7postyizhan"))
                sender.sendMessage(MessageUtil.color("&f• GitHub: &7https://github.com/postyizhan/PostSpawner"))
                sender.sendMessage(MessageUtil.color("&f• Discord: &7https://discord.com/invite/jN4Br8uhSS"))
                sender.sendMessage(MessageUtil.color("&f• QQ Group: &7611076407"))
            }
            
            "help" -> {
                if (!sender.hasPermission("postspawner.command.help")) {
                    sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.no-permission")))
                    return true
                }
                
                sendHelpMessage(sender)
            }
            
            "update" -> {
                if (!sender.hasPermission("postspawner.command.update")) {
                    sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.no-permission")))
                    return true
                }
                // 从配置文件读取检查更新提示
                val updateChecking = plugin.getConfigManager().getConfig().getString("system.updater.update_checking")
                    ?: "&8[&3Post&bSpawner&8] &7Checking for updates..."
                sender.sendMessage(MessageUtil.color(updateChecking))
                if (sender is Player) {
                    plugin.sendUpdateInfo(sender)
                } else {
                    plugin.getUpdateChecker().checkForUpdates { isUpdateAvailable, newVersion ->
                        if (isUpdateAvailable) {
                            sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("system.updater.update_available")
                                .replace("{current_version}", plugin.description.version)
                                .replace("{latest_version}", newVersion)))
                            sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("system.updater.update_url")
                                .replace("{current_version}", plugin.description.version)
                                .replace("{latest_version}", newVersion)))
                        } else {
                            sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("system.updater.up_to_date")))
                        }
                    }
                }
            }
            
            else -> {
                sender.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.invalid-command")))
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        val completions = mutableListOf<String>()

        if (args.size == 1) {
            // 根据玩家权限提供不同的命令补全选项
            if (sender.hasPermission("postspawner.command.reload")) completions.add("reload")
            if (sender.hasPermission("postspawner.command.version")) completions.add("version")
            if (sender.hasPermission("postspawner.command.help")) completions.add("help")
            if (sender.hasPermission("postspawner.command.update")) completions.add("update")
            
            return completions.filter { it.startsWith(args[0].lowercase()) }
        }

        return completions
    }

    /**
     * 发送帮助信息
     */
    private fun sendHelpMessage(sender: CommandSender) {
        val helpMessages = MessageUtil.getStringList("messages.help")
        for (message in helpMessages) {
            sender.sendMessage(MessageUtil.color(message))
        }
    }
}

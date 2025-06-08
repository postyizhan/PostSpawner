package com.github.postyizhan

import com.github.postyizhan.command.CommandManager
import com.github.postyizhan.config.ConfigManager
import com.github.postyizhan.hook.HookManager
import com.github.postyizhan.listener.PlayerJoinListener
import com.github.postyizhan.listener.SpawnerListener
import com.github.postyizhan.util.ActionManager
import com.github.postyizhan.util.MessageUtil
import com.github.postyizhan.util.MiniMessageUtil
import com.github.postyizhan.util.UpdateChecker
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class PostSpawner : JavaPlugin() {

    companion object {
        lateinit var instance: PostSpawner
            private set
    }

    private lateinit var configManager: ConfigManager
    private lateinit var hookManager: HookManager
    private lateinit var actionManager: ActionManager
    private lateinit var updateChecker: UpdateChecker
    private lateinit var commandManager: CommandManager
    
    override fun onEnable() {
        instance = this

        // 初始化配置管理器
        configManager = ConfigManager(this).apply { loadAll() }
        
        // 初始化消息工具
        MessageUtil.init(this)
        
        // 初始化动作管理器
        actionManager = ActionManager(this)
        
        // 初始化钩子管理器
        hookManager = HookManager(this)
        
        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(SpawnerListener(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerJoinListener(this), this)
        
        // 注册命令
        commandManager = CommandManager(this)
        commandManager.registerCommands()
        
        // 初始化bStats统计
        val pluginId = 26108
        Metrics(this, pluginId)
        
        // 初始化更新检查器
        updateChecker = UpdateChecker(this, "postyizhan/PostSpawner")
        if (configManager.getConfig().getBoolean("update-checker.enabled", true)) {
            updateChecker.checkForUpdates { isUpdateAvailable, newVersion ->
                if (isUpdateAvailable) {
                    server.consoleSender.sendMessage(MessageUtil.color(
                        MessageUtil.getMessage("system.updater.update_available")
                            .replace("{current_version}", description.version)
                            .replace("{latest_version}", newVersion)
                    ))
                    server.consoleSender.sendMessage(MessageUtil.color(
                        MessageUtil.getMessage("system.updater.update_url")
                            .replace("{current_version}", description.version)
                            .replace("{latest_version}", newVersion)
                    ))
                } else {
                    server.consoleSender.sendMessage(MessageUtil.color(
                        MessageUtil.getMessage("system.updater.up_to_date")
                    ))
                }
            }
        }

        // 输出启动消息
        // 同时支持传统格式和MiniMessage格式
        val startupMessage = "&8[&3Post&bSpawner&8] &a插件加载成功。 &7版本: &f${description.version} &7作者: &fpostyizhan"
        
        if (MessageUtil.isUsingMiniMessage()) {
            MiniMessageUtil.sendMessage(server.consoleSender, MessageUtil.process(startupMessage))
        } else {
            server.consoleSender.sendMessage(MessageUtil.color(startupMessage))
        }
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
        
        // 关闭MiniMessage
        try {
            MiniMessageUtil.shutdown()
        } catch (e: Exception) {
            logger.warning("Failed to shutdown MiniMessage: ${e.message}")
        }
        
        // 输出关闭消息
        val disabledMessage = MessageUtil.getMessage("messages.disabled")
        if (MessageUtil.isUsingMiniMessage()) {
            MiniMessageUtil.sendMessage(server.consoleSender, MessageUtil.process(disabledMessage))
        } else {
            server.consoleSender.sendMessage(MessageUtil.color(disabledMessage))
        }
    }
    
    /**
     * 重新加载插件配置
     */
    fun reload() {
        configManager.loadAll()
        MessageUtil.init(this)
    }
    
    /**
     * 向玩家发送更新检查信息
     */
    fun sendUpdateInfo(player: Player) {
        updateChecker.checkForUpdates { isUpdateAvailable, newVersion ->
            if (isUpdateAvailable) {
                val updateAvailableMsg = MessageUtil.getMessage("system.updater.update_available")
                        .replace("{current_version}", description.version)
                        .replace("{latest_version}", newVersion)
                
                val updateUrlMsg = MessageUtil.getMessage("system.updater.update_url")
                        .replace("{current_version}", description.version)
                        .replace("{latest_version}", newVersion)
                
                MessageUtil.sendMessage(player, updateAvailableMsg)
                MessageUtil.sendMessage(player, updateUrlMsg)
            } else {
                val upToDateMsg = MessageUtil.getMessage("system.updater.up_to_date")
                MessageUtil.sendMessage(player, upToDateMsg)
            }
        }
    }
    
    fun getConfigManager(): ConfigManager = configManager
    fun getHookManager(): HookManager = hookManager
    fun getActionManager(): ActionManager = actionManager
    fun getUpdateChecker(): UpdateChecker = updateChecker
}

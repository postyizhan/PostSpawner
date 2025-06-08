package com.github.postyizhan.util

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.action.ActionFactory
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

/**
 * 动作管理器，处理各种动作
 */
class ActionManager(private val plugin: PostSpawner) {
    private val actionFactory = ActionFactory(plugin)
    
    /**
     * 执行一系列动作
     */
    fun executeActions(player: Player, actions: List<String>, entityType: EntityType? = null, location: Location? = null) {
        for (action in actions) {
            val processedAction = replaceVariables(action, player, entityType, location)
            val actionHandler = actionFactory.getAction(processedAction)
            try {
                actionHandler.execute(player, processedAction, location)
            } catch (e: Exception) {
                plugin.logger.warning(MessageUtil.getMessage("system.debug.action.error").replace("%action%", processedAction))
                plugin.logger.warning("Error details: ${e.message}")
                if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 替换动作中的变量
     */
    private fun replaceVariables(action: String, player: Player, entityType: EntityType?, location: Location? = null): String {
        var result = action.replace("%player%", player.name)
        
        if (entityType != null) {
            result = result.replace("%entity_type%", entityType.name)
        } else {
            result = result.replace("%entity_type%", "PIG") // 默认为猪
        }
        
        // 添加方块相关变量
        if (location != null) {
            result = result.replace("%block_x%", location.blockX.toString())
            result = result.replace("%block_y%", location.blockY.toString())
            result = result.replace("%block_z%", location.blockZ.toString())
            result = result.replace("%block_world%", location.world?.name ?: "unknown")
        }
        
        // 如果启用了PlaceholderAPI，则替换其占位符
        if (plugin.getHookManager().isPlaceholderAPIEnabled()) {
            result = plugin.getHookManager().getPlaceholderAPIHook()!!.setPlaceholders(player, result)
        }
        
        return result
    }
}

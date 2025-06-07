package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 点券动作处理器
 */
class PointsAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        if (!plugin.getHookManager().isPlayerPointsEnabled()) return
        
        val content = extractActionValue(actionValue, ActionType.POINTS.prefix)
        val parts = content.split(" ")
        val operation = parts[0].lowercase()
        val amount = if (parts.size > 1) parts[1].toIntOrNull() ?: 0 else 0
        
        when (operation) {
            "give" -> plugin.getHookManager().getPlayerPointsHook()?.givePoints(player, amount)
            "take" -> plugin.getHookManager().getPlayerPointsHook()?.takePoints(player, amount)
            "set" -> plugin.getHookManager().getPlayerPointsHook()?.setPoints(player, amount)
            else -> logWarning(MessageUtil.getMessage("system.debug.action.invalid_points_operation").replace("%operation%", operation))
        }
    }
}

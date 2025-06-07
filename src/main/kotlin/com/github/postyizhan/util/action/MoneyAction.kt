package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 经济动作处理器
 */
class MoneyAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        if (!plugin.getHookManager().isVaultEnabled()) return
        
        val content = extractActionValue(actionValue, ActionType.MONEY.prefix)
        val parts = content.split(" ")
        val operation = parts[0].lowercase()
        val amount = if (parts.size > 1) parts[1].toDoubleOrNull() ?: 0.0 else 0.0
        
        when (operation) {
            "give" -> plugin.getHookManager().getVaultHook()?.giveMoney(player, amount)
            "take" -> plugin.getHookManager().getVaultHook()?.takeMoney(player, amount)
            "set" -> plugin.getHookManager().getVaultHook()?.setMoney(player, amount)
            else -> logWarning(MessageUtil.getMessage("system.debug.action.invalid_money_operation").replace("%operation%", operation))
        }
    }
}

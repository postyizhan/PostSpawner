package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * 药水效果动作处理器
 */
class BuffAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val content = extractActionValue(actionValue, ActionType.BUFF.prefix)
        val parts = content.split(" ")
        val effectName = parts[0]
        val duration = if (parts.size > 1) parts[1].toIntOrNull() ?: 30 else 30
        val amplifier = if (parts.size > 2) parts[2].toIntOrNull() ?: 0 else 0
        
        try {
            val potionEffect = PotionEffectType.getByName(effectName)
            if (potionEffect != null) {
                player.addPotionEffect(PotionEffect(potionEffect, duration * 20, amplifier))
            } else {
                logWarning(MessageUtil.getMessage("system.debug.action.invalid_effect").replace("%effect%", effectName))
            }
        } catch (e: Exception) {
            logWarning(MessageUtil.getMessage("system.debug.action.invalid_effect").replace("%effect%", effectName))
        }
    }
}

package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * 音效动作处理器
 */
class SoundAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val content = extractActionValue(actionValue, ActionType.SOUND.prefix)
        val parts = content.split(" ")
        val soundName = parts[0]
        val volume = if (parts.size > 1) parts[1].toFloatOrNull() ?: 1.0f else 1.0f
        val pitch = if (parts.size > 2) parts[2].toFloatOrNull() ?: 1.0f else 1.0f
        
        try {
            val sound = Sound.valueOf(soundName)
            player.playSound(player.location, sound, volume, pitch)
        } catch (e: IllegalArgumentException) {
            logWarning(MessageUtil.getMessage("system.debug.action.invalid_sound").replace("%sound%", soundName))
        }
    }
}

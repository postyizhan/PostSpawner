package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 标题动作处理器
 */
class TitleAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val content = extractActionValue(actionValue, ActionType.TITLE.prefix)
        val parts = content.split(" ", limit = 2)
        val title = MessageUtil.color(parts[0])
        val subtitle = if (parts.size > 1) MessageUtil.color(parts[1]) else ""
        
        player.sendTitle(title, subtitle, 10, 70, 20)
    }
}

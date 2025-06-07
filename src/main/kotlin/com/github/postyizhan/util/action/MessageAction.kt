package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 消息动作处理器
 */
class MessageAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val message = extractActionValue(actionValue, ActionType.MESSAGE.prefix)
        player.sendMessage(MessageUtil.color(message))
    }
}

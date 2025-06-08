package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import com.github.postyizhan.util.MiniMessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 消息动作处理器
 */
class MessageAction(private val plugin: PostSpawner) : Action {
    override fun execute(player: Player, action: String, location: Location?) {
        val message = action.substring(action.indexOf("]") + 1).trim()
        
        if (MessageUtil.isUsingMiniMessage()) {
            // 处理消息，支持两种格式共存
            MiniMessageUtil.sendMessage(player, MessageUtil.process(message))
        } else {
            // 使用传统格式
        player.sendMessage(MessageUtil.color(message))
        }
    }
}

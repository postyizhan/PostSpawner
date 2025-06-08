package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import com.github.postyizhan.util.MiniMessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 标题动作处理器
 */
class TitleAction(private val plugin: PostSpawner) : Action {
    override fun execute(player: Player, action: String, location: Location?) {
        val content = action.substring(action.indexOf("]") + 1).trim()
        
        // 分割标题和副标题
        val parts = content.split(" ", limit = 2)
        val title = parts[0]
        val subtitle = if (parts.size > 1) parts[1] else ""
        
        // 标题动画时间配置
        val fadeIn = plugin.getConfigManager().getConfig().getInt("title.fade-in", 10)
        val stay = plugin.getConfigManager().getConfig().getInt("title.stay", 70)
        val fadeOut = plugin.getConfigManager().getConfig().getInt("title.fade-out", 20)
        
        if (MessageUtil.isUsingMiniMessage()) {
            // 处理标题和副标题，支持两种格式共存
            MiniMessageUtil.sendTitle(
                player, 
                MessageUtil.process(title), 
                MessageUtil.process(subtitle),
                fadeIn, stay, fadeOut
            )
        } else {
            // 使用传统格式
            player.sendTitle(MessageUtil.color(title), MessageUtil.color(subtitle), fadeIn, stay, fadeOut)
        }
    }
}

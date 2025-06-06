package com.github.postyizhan.listener

import com.github.postyizhan.PostSpawner
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * 玩家加入事件监听器
 */
class PlayerJoinListener(private val plugin: PostSpawner) : Listener {

    /**
     * 处理玩家加入事件
     */
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        
        // 如果玩家是 OP，发送更新检查信息
        if (player.isOp && plugin.getConfigManager().getConfig().getBoolean("update-checker.enabled", true)) {
            // 在玩家加入后 2 秒发送更新信息，避免消息过多
            plugin.server.scheduler.runTaskLater(plugin, Runnable {
                if (player.isOnline) {
                    plugin.sendUpdateInfo(player)
                }
            }, 40L)  // 40 ticks = 2 seconds
        }
    }
}

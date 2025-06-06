package com.github.postyizhan.hook.impl

import com.github.postyizhan.PostSpawner
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * PlayerPoints 钩子实现
 * 注意：由于 PlayerPoints API 可能不直接可用，我们使用反射来调用方法
 */
class PlayerPointsHook(private val plugin: PostSpawner) {
    
    private var playerPointsPlugin: Plugin? = null
    
    init {
        setupPlayerPoints()
    }
    
    /**
     * 设置 PlayerPoints API
     */
    private fun setupPlayerPoints() {
        playerPointsPlugin = Bukkit.getPluginManager().getPlugin("PlayerPoints")
    }
    
    /**
     * 检查是否启用
     */
    fun isEnabled(): Boolean {
        return playerPointsPlugin != null
    }
    
    /**
     * 给予玩家点券
     */
    fun givePoints(player: Player, amount: Int): Boolean {
        if (!isEnabled()) return false
        try {
            val api = playerPointsPlugin?.javaClass?.getMethod("getAPI")?.invoke(playerPointsPlugin)
            val giveMethod = api?.javaClass?.getMethod("give", java.util.UUID::class.java, Int::class.java)
            return giveMethod?.invoke(api, player.uniqueId, amount) as? Boolean ?: false
        } catch (e: Exception) {
            plugin.logger.warning("Failed to call PlayerPoints API: ${e.message}")
            return false
        }
    }
    
    /**
     * 扣除玩家点券
     */
    fun takePoints(player: Player, amount: Int): Boolean {
        if (!isEnabled()) return false
        try {
            val api = playerPointsPlugin?.javaClass?.getMethod("getAPI")?.invoke(playerPointsPlugin)
            val takeMethod = api?.javaClass?.getMethod("take", java.util.UUID::class.java, Int::class.java)
            return takeMethod?.invoke(api, player.uniqueId, amount) as? Boolean ?: false
        } catch (e: Exception) {
            plugin.logger.warning("Failed to call PlayerPoints API: ${e.message}")
            return false
        }
    }
    
    /**
     * 设置玩家点券
     */
    fun setPoints(player: Player, amount: Int): Boolean {
        if (!isEnabled()) return false
        try {
            val api = playerPointsPlugin?.javaClass?.getMethod("getAPI")?.invoke(playerPointsPlugin)
            val setMethod = api?.javaClass?.getMethod("set", java.util.UUID::class.java, Int::class.java)
            return setMethod?.invoke(api, player.uniqueId, amount) as? Boolean ?: false
        } catch (e: Exception) {
            plugin.logger.warning("Failed to call PlayerPoints API: ${e.message}")
            return false
        }
    }
    
    /**
     * 获取玩家点券
     */
    fun getPoints(player: Player): Int {
        if (!isEnabled()) return 0
        try {
            val api = playerPointsPlugin?.javaClass?.getMethod("getAPI")?.invoke(playerPointsPlugin)
            val lookMethod = api?.javaClass?.getMethod("look", java.util.UUID::class.java)
            return lookMethod?.invoke(api, player.uniqueId) as? Int ?: 0
        } catch (e: Exception) {
            plugin.logger.warning("Failed to call PlayerPoints API: ${e.message}")
            return 0
        }
    }
}
 
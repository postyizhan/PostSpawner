package com.github.postyizhan.hook.impl

import com.github.postyizhan.PostSpawner
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * PlaceholderAPI 钩子实现
 * 使用反射机制调用 PlaceholderAPI
 */
class PlaceholderAPIHook(private val plugin: PostSpawner) {

    /**
     * 检查是否启用
     */
    fun isEnabled(): Boolean {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
    }
    
    /**
     * 替换占位符
     */
    fun setPlaceholders(player: Player?, text: String): String {
        if (!isEnabled() || player == null) return text
        
        try {
            val placeholderAPIClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI")
            val setPlaceholdersMethod = placeholderAPIClass.getMethod("setPlaceholders", Player::class.java, String::class.java)
            return setPlaceholdersMethod.invoke(null, player, text) as String
        } catch (e: Exception) {
            plugin.logger.warning("Failed to call PlaceholderAPI: ${e.message}")
            return text
        }
    }
}

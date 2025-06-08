package com.github.postyizhan.util

import com.github.postyizhan.PostSpawner
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * 消息工具类，负责处理插件的消息
 */
object MessageUtil {
    private var prefix: String = ""
    private lateinit var lang: FileConfiguration
    private lateinit var plugin: PostSpawner
    private var useMiniMessage: Boolean = true

    /**
     * 初始化消息工具类
     */
    fun init(plugin: PostSpawner) {
        this.plugin = plugin
        this.lang = plugin.getConfigManager().getLang()
        this.prefix = color(lang.getString("prefix") ?: "&8[&3Post&bSpawner&8] ")
        this.useMiniMessage = plugin.getConfigManager().getConfig().getBoolean("use-minimessage", true)
        
        // 初始化MiniMessage工具类
        MiniMessageUtil.init(plugin)
    }

    /**
     * 获取插件前缀
     */
    fun getPrefix(): String {
        return prefix
    }

    /**
     * 将消息中的颜色代码转换为颜色
     */
    fun color(message: String): String {
        return message.replace('&', '§')
    }

    /**
     * 处理消息文本，支持传统&颜色代码和MiniMessage格式共存
     * 如果启用了MiniMessage，会优先处理&颜色代码，然后再应用MiniMessage格式
     */
    fun process(message: String): String {
        if (!useMiniMessage) {
            return color(message)
        }
        
        // 检测消息是否同时包含&颜色代码和MiniMessage格式
        val containsLegacyCode = message.contains('&')
        val containsMiniMessage = message.contains('<') && message.contains('>')
        
        if (containsLegacyCode && !containsMiniMessage) {
            // 只有传统颜色代码，转换为颜色再转为MiniMessage格式
            return MiniMessageUtil.legacyToMiniMessage(color(message))
        } else if (!containsLegacyCode && containsMiniMessage) {
            // 只有MiniMessage格式，直接返回
            return message
        } else if (containsLegacyCode && containsMiniMessage) {
            // 同时包含两种格式，先处理&颜色代码，再转为MiniMessage格式
            return MiniMessageUtil.legacyToMiniMessage(color(message))
        } else {
            // 不包含任何格式，直接返回
            return message
        }
    }

    /**
     * 获取消息
     */
    fun getMessage(path: String): String {
        val message = lang.getString(path)
            ?.replace("{prefix}", prefix)
            ?: "$prefix&cMessage not found: $path"
        
        return message
    }

    /**
     * 获取字符串列表
     */
    fun getStringList(path: String): List<String> {
        val list = lang.getStringList(path)
        return if (list.isEmpty()) {
            listOf("$prefix&cMessage list not found list not found: $path")
        } else {
            list.map { it.replace("{prefix}", prefix) }
        }
    }

    /**
     * 格式化消息
     */
    fun formatMessage(message: String, vararg args: Any): String {
        var formatted = message
        for (i in args.indices) {
            formatted = formatted.replace("{$i}", args[i].toString())
        }
        return formatted.replace("{prefix}", prefix)
    }
    
    /**
     * 发送消息给玩家
     * 同时支持传统&颜色代码和MiniMessage格式
     */
    fun sendMessage(player: Player, message: String) {
        if (useMiniMessage) {
            val processedMsg = process(message)
            MiniMessageUtil.sendMessage(player, processedMsg)
        } else {
            player.sendMessage(color(message))
        }
    }
    
    /**
     * 发送ActionBar消息给玩家
     */
    fun sendActionBar(player: Player, message: String) {
        if (useMiniMessage) {
            val processedMsg = process(message)
            MiniMessageUtil.sendActionBar(player, processedMsg)
        } else {
            val actionBarMethod = player.javaClass.getMethod("sendActionBar", String::class.java)
            actionBarMethod.invoke(player, color(message))
        }
    }
    
    /**
     * 发送标题给玩家
     */
    fun sendTitle(player: Player, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        if (useMiniMessage) {
            val processedTitle = process(title)
            val processedSubtitle = process(subtitle)
            MiniMessageUtil.sendTitle(player, processedTitle, processedSubtitle, fadeIn, stay, fadeOut)
        } else {
            player.sendTitle(color(title), color(subtitle), fadeIn, stay, fadeOut)
        }
    }
    
    /**
     * 判断是否启用MiniMessage
     */
    fun isUsingMiniMessage(): Boolean {
        return useMiniMessage
    }
}

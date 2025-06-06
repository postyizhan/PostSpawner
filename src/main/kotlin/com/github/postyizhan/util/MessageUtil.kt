package com.github.postyizhan.util

import com.github.postyizhan.PostSpawner
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.configuration.file.FileConfiguration

/**
 * 消息工具类，负责处理插件的消息
 */
object MessageUtil {
    private var prefix: String = ""
    private lateinit var lang: FileConfiguration
    private lateinit var plugin: PostSpawner

    /**
     * 初始化消息工具类
     */
    fun init(plugin: PostSpawner) {
        this.plugin = plugin
        this.lang = plugin.getConfigManager().getLang()
        this.prefix = color(lang.getString("prefix") ?: "&8[&3Post&bSpawner&8] ")
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
     * 获取消息
     */
    fun getMessage(path: String): String {
        return lang.getString(path)
            ?.replace("{prefix}", prefix)
            ?: "$prefix&cMessage not found: $path"
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
}

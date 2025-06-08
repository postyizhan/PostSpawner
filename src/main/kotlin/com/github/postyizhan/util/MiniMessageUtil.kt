package com.github.postyizhan.util

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import com.github.postyizhan.PostSpawner
import java.util.concurrent.TimeUnit

/**
 * MiniMessage工具类，用于处理MiniMessage格式的文本
 */
class MiniMessageUtil {
    companion object {
        private lateinit var audiences: BukkitAudiences
        private val miniMessage = MiniMessage.miniMessage()
        private val legacySerializer = LegacyComponentSerializer.legacySection()
        
        /**
         * 初始化MiniMessage工具类
         * @param plugin 插件实例
         */
        fun init(plugin: PostSpawner) {
            audiences = BukkitAudiences.create(plugin)
        }
        
        /**
         * 关闭MiniMessage工具类
         */
        fun shutdown() {
            if (::audiences.isInitialized) {
                audiences.close()
            }
        }
        
        /**
         * 将传统颜色代码格式的文本转换为MiniMessage格式
         * @param legacy 传统颜色代码格式的文本
         * @return MiniMessage格式的文本
         */
        fun legacyToMiniMessage(legacy: String): String {
            val component = legacySerializer.deserialize(legacy)
            return miniMessage.serialize(component)
        }
        
        /**
         * 将MiniMessage格式的文本转换为传统颜色代码格式
         * @param miniMsg MiniMessage格式的文本
         * @return 传统颜色代码格式的文本
         */
        fun miniMessageToLegacy(miniMsg: String): String {
            val component = miniMessage.deserialize(miniMsg)
            return legacySerializer.serialize(component)
        }
        
        /**
         * 解析MiniMessage格式的文本为Component
         * @param text MiniMessage格式的文本
         * @return Component对象
         */
        fun parse(text: String): Component {
            return miniMessage.deserialize(text)
        }
        
        /**
         * 解析传统颜色代码格式的文本为Component
         * @param text 传统颜色代码格式的文本
         * @return Component对象
         */
        fun parseLegacy(text: String): Component {
            return legacySerializer.deserialize(text)
        }
        
        /**
         * 向玩家发送MiniMessage格式的消息
         * @param player 玩家
         * @param message MiniMessage格式的消息
         */
        fun sendMessage(player: Player, message: String) {
            audiences.player(player).sendMessage(parse(message))
        }
        
        /**
         * 向命令发送者发送MiniMessage格式的消息
         * @param sender 命令发送者
         * @param message MiniMessage格式的消息
         */
        fun sendMessage(sender: CommandSender, message: String) {
            audiences.sender(sender).sendMessage(parse(message))
        }
        
        /**
         * 向玩家发送MiniMessage格式的ActionBar消息
         * @param player 玩家
         * @param message MiniMessage格式的消息
         */
        fun sendActionBar(player: Player, message: String) {
            audiences.player(player).sendActionBar(parse(message))
        }
        
        /**
         * 向玩家发送MiniMessage格式的标题
         * @param player 玩家
         * @param title 标题
         * @param subtitle 副标题
         * @param fadeIn 淡入时间（单位：tick）
         * @param stay 停留时间（单位：tick）
         * @param fadeOut 淡出时间（单位：tick）
         */
        fun sendTitle(player: Player, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
            // 适配Java 8和Spigot 1.13版本的标题展示
            val titleComponent = parse(title)
            val subtitleComponent = parse(subtitle)
            
            // 使用适配1.13版本的方式创建Title对象
            val titleTimes = net.kyori.adventure.title.Title.Times.of(
                java.time.Duration.ofMillis((fadeIn * 50).toLong()),
                java.time.Duration.ofMillis((stay * 50).toLong()),
                java.time.Duration.ofMillis((fadeOut * 50).toLong())
            )
            
            val titleObj = net.kyori.adventure.title.Title.title(
                titleComponent,
                subtitleComponent,
                titleTimes
            )
            
            audiences.player(player).showTitle(titleObj)
        }
    }
}

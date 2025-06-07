package com.github.postyizhan.hook.impl

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.hook.ItemSourceHook
import com.github.postyizhan.hook.impl.items.*
import com.github.postyizhan.util.MessageUtil
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

/**
 * 物品源钩子管理器
 * 用于管理和注册物品源
 */
class ItemSourceHookManager(private val plugin: PostSpawner) {

    private val sources = mutableListOf<ItemSourceHook>()
    
    init {
        // 注册默认的Minecraft物品源
        registerSource(MinecraftItemSource())
        
        // 尝试注册其他物品库
        tryRegisterSource(ItemsAdderItemSource(), "ItemsAdder")
        tryRegisterSource(OraxenItemSource(), "Oraxen")
        tryRegisterSource(NeigeItemsItemSource(), "NeigeItems")
        tryRegisterSource(MMOItemsItemSource(), "MMOItems")
        tryRegisterSource(MythicMobsItemSource(), "MythicMobs")
        tryRegisterSource(SXItemItemSource(), "SX-Item")
        tryRegisterSource(ZaphkielItemSource(), "Zaphkiel")
        tryRegisterSource(CraftEngineItemSource(), "CraftEngine")
        tryRegisterSource(AzureFlowItemSource(), "AzureFlow")
    }
    
    /**
     * 尝试注册物品源
     */
    private fun tryRegisterSource(source: ItemSourceHook, pluginName: String) {
        try {
            // 检查插件是否存在
            if (plugin.server.pluginManager.getPlugin(pluginName) == null) {
                if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                    plugin.logger.info("$pluginName not found, skipping item source registration")
                }
                return
            }
            
            registerSource(source)
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                plugin.logger.info("Successfully registered $pluginName item source")
            }
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.enabled")
                        .replace("{0}", pluginName)
                )
            )
        } catch (e: Exception) {
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                plugin.logger.log(Level.WARNING, "Failed to register $pluginName item source", e)
            }
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.disabled")
                        .replace("{0}", pluginName)
                )
            )
        }
    }
    
    /**
     * 注册物品源
     */
    fun registerSource(source: ItemSourceHook) {
        sources.add(source)
    }
    
    /**
     * 从物品ID获取物品
     * 格式: [source:]itemId[:amount]
     * 例如: minecraft:diamond_sword:1, itemsadder:ruby_sword, mmoitems:SWORD:EXCALIBUR:1
     */
    fun getItem(itemString: String, player: Player? = null): ItemStack? {
        if (itemString.isEmpty()) return null
        
        try {
            val parts = itemString.split(":")
            
            // 确定物品源和物品ID
            var sourceId = "minecraft"
            var itemId: String
            var amount = 1
            
            when (parts.size) {
                1 -> {
                    // 只有物品ID，默认使用minecraft源
                    itemId = parts[0]
                }
                2 -> {
                    // 可能是 source:itemId 或 itemId:amount
                    if (parts[1].toIntOrNull() != null) {
                        // 是 itemId:amount 格式
                        itemId = parts[0]
                        amount = parts[1].toInt()
                    } else {
                        // 是 source:itemId 格式
                        sourceId = parts[0].lowercase()
                        itemId = parts[1]
                    }
                }
                else -> {
                    // source:itemId:amount 或 source:type:id 等更复杂格式
                    sourceId = parts[0].lowercase()
                    
                    // 最后一部分可能是数量
                    val lastPart = parts.last()
                    if (lastPart.toIntOrNull() != null) {
                        amount = lastPart.toInt()
                        itemId = parts.subList(1, parts.size - 1).joinToString(":")
                    } else {
                        itemId = parts.subList(1, parts.size).joinToString(":")
                    }
                }
            }
            
            // 查找对应的物品源
            val source = sources.find { it.getSourceId() == sourceId }
            if (source == null) {
                plugin.logger.warning("Unknown item source: $sourceId")
                return null
            }
            
            // 获取物品
            val item = source.getItem(itemId, player)
            if (item == null) {
                plugin.logger.warning("Item not found: $sourceId:$itemId")
                return null
            }
            
            // 设置数量
            item.amount = amount
            
            return item
        } catch (e: Exception) {
            plugin.logger.log(Level.WARNING, "Failed to get item: $itemString", e)
            return null
        }
    }
    
    /**
     * 检查物品ID是否有效
     */
    fun isValidItem(itemString: String): Boolean {
        return try {
            getItem(itemString) != null
        } catch (e: Exception) {
            false
        }
    }
}

package com.github.postyizhan.listener

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.CreatureSpawner
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * 刷怪笼事件监听器
 */
class SpawnerListener(private val plugin: PostSpawner) : Listener {

    /**
     * 处理刷怪笼破坏事件
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onSpawnerBreak(event: BlockBreakEvent) {
        if (event.block.type != Material.SPAWNER) return
        
        val player = event.player
        val block = event.block
        val location = block.location
        
        // 检查玩家是否有权限
        if (!player.hasPermission("postspawner.use.break")) {
            event.isCancelled = true
            player.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.spawner.no-permission")))
            return
        }
        
        // 检查创造模式
        if (player.gameMode == GameMode.CREATIVE) {
            return
        }
        
        // 获取刷怪笼实体类型
        val blockState = block.state as? CreatureSpawner ?: return
        val entityType = blockState.spawnedType
        
        // 获取玩家手中的物品
        val tool = player.inventory.itemInMainHand
        val hasSilkTouch = tool.containsEnchantment(Enchantment.SILK_TOUCH)
        
        // 计算挖掘成功率
        val config = plugin.getConfigManager().getConfig()
        var failChance = config.getDouble("spawner.fail-chance", 0.7)
        
        // 如果有精准采集，减少失败几率
        if (hasSilkTouch) {
            failChance -= config.getDouble("spawner.silk-touch-bonus", 0.2)
            
            // 执行精准采集动作
            val silkTouchActions = config.getStringList("spawner.silk-touch-actions")
            if (silkTouchActions.isNotEmpty()) {
                plugin.getActionManager().executeActions(player, silkTouchActions, entityType, location)
            }
        }
        
        // 确保失败几率在有效范围内
        failChance = failChance.coerceIn(0.0, 1.0)
        
        // 随机判断是否成功
        val isSuccess = Random().nextDouble() > failChance
        
        // 记录调试信息
        if (config.getBoolean("debug", false)) {
            val result = if (isSuccess) "success" else "fail"
            val toolType = if (hasSilkTouch) "silk_touch" else "normal"
            val debugMessage = MessageUtil.getMessage("system.debug.spawner.break_info")
                .replace("%player%", player.name)
                .replace("%tool_type%", toolType)
                .replace("%entity_type%", entityType?.name ?: "UNKNOWN")
                .replace("%result%", result)
                .replace("%fail_chance%", failChance.toString())
            plugin.logger.info(debugMessage)
        }
        
        // 处理结果
        if (isSuccess) {
            // 执行成功动作
            val successActions = config.getStringList("spawner.success-actions")
            if (successActions.isNotEmpty()) {
                plugin.getActionManager().executeActions(player, successActions, entityType, location)
            }
            
            // 掉落刷怪笼 - 阻止默认掉落
            event.isDropItems = false
            
            // 创建带有实体类型的刷怪笼物品
            val spawner = ItemStack(Material.SPAWNER)
            val meta = spawner.itemMeta
            meta?.setDisplayName(MessageUtil.color("&f${entityType?.name ?: "PIG"} &7刷怪笼"))
            val lore = ArrayList<String>()
            lore.add(MessageUtil.color("&7实体类型: &f${entityType?.name ?: "PIG"}"))
            meta?.lore = lore
            spawner.itemMeta = meta
            
            // 掉落物品
            player.world.dropItemNaturally(location, spawner)
            
        } else {
            // 执行失败动作
            val failActions = config.getStringList("spawner.fail-actions")
            if (failActions.isNotEmpty()) {
                plugin.getActionManager().executeActions(player, failActions, entityType, location)
            }
            
            // 阻止默认掉落
            event.isDropItems = false
        }
    }
    
    /**
     * 处理刷怪笼放置事件
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onSpawnerPlace(event: BlockPlaceEvent) {
        if (event.block.type != Material.SPAWNER) return
        
        val player = event.player
        val block = event.block
        
        // 检查玩家是否有权限
        if (!player.hasPermission("postspawner.use.place")) {
            event.isCancelled = true
            player.sendMessage(MessageUtil.color(MessageUtil.getMessage("messages.spawner.no-permission")))
            return
        }
        
        // 获取玩家手中的物品
        val item = event.itemInHand
        val meta = item.itemMeta
        
        // 尝试从物品lore获取实体类型
        val lore = meta?.lore
        var entityTypeStr = ""
        
        if (lore != null) {
            for (line in lore) {
                if (line.contains("实体类型:")) {
                    entityTypeStr = line.split(":")[1].trim()
                    break
                } else if (line.contains("Entity Type:")) {
                    entityTypeStr = line.split(":")[1].trim()
                    break
                }
            }
        }
        
        // 获取实体类型
        var entityType: EntityType = EntityType.PIG // 默认为猪
        if (entityTypeStr.isNotEmpty()) {
            try {
                entityType = EntityType.valueOf(entityTypeStr.replace("§f", "").replace("§7", ""))
                
                if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                    plugin.logger.info(
                        MessageUtil.getMessage("system.debug.spawner.lore_found_type")
                            .replace("%entity_type%", entityType.name)
                    )
                }
            } catch (e: IllegalArgumentException) {
                if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                    plugin.logger.warning(
                        MessageUtil.getMessage("system.debug.spawner.invalid_type")
                            .replace("%entity_type%", entityTypeStr)
                    )
                }
                
                entityType = EntityType.PIG // 默认为猪
            }
        } else {
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                plugin.logger.info(MessageUtil.getMessage("system.debug.spawner.default_type"))
            }
        }
        
        // 设置刷怪笼类型
        val blockState = block.state as CreatureSpawner
        try {
            blockState.spawnedType = entityType
            blockState.update()
            
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                plugin.logger.info(
                    MessageUtil.getMessage("system.debug.spawner.set_type_success")
                        .replace("%entity_type%", entityType.name)
                )
            }
            
            // 发送消息
            player.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("messages.spawner.place-success")
                        .replace("%entity_type%", entityType.name)
                )
            )
        } catch (e: Exception) {
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                plugin.logger.warning(
                    MessageUtil.getMessage("system.debug.spawner.set_type_error")
                        .replace("%error%", e.message ?: "未知错误")
                )
            }
        }
    }
} 
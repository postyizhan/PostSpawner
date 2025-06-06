package com.github.postyizhan.util

import com.github.postyizhan.PostSpawner
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.regex.Pattern

/**
 * 动作管理器，处理各种动作
 */
class ActionManager(private val plugin: PostSpawner) {

    /**
     * 执行一系列动作
     */
    fun executeActions(player: Player, actions: List<String>, entityType: EntityType? = null, location: org.bukkit.Location? = null) {
        for (action in actions) {
            val processedAction = replaceVariables(action, player, entityType)
            executeAction(player, processedAction, location)
        }
    }

    /**
     * 替换动作中的变量
     */
    private fun replaceVariables(action: String, player: Player, entityType: EntityType?): String {
        var result = action
            .replace("%player%", player.name)
            .replace("%player_uuid%", player.uniqueId.toString())
            .replace("%world%", player.world.name)
        
        if (entityType != null) {
            result = result.replace("%entity_type%", entityType.name)
        } else {
            result = result.replace("%entity_type%", "PIG") // 默认为猪
        }
        
        // 如果启用了PlaceholderAPI，则替换其占位符
        if (plugin.getHookManager().isPlaceholderAPIEnabled()) {
            result = plugin.getHookManager().getPlaceholderAPIHook()!!.setPlaceholders(player, result)
        }
        
        return result
    }

    /**
     * 执行单个动作
     */
    private fun executeAction(player: Player, action: String, location: org.bukkit.Location? = null) {
        val actualLocation = location ?: player.location
        
        try {
            if (action.startsWith("[command]")) {
                val command = action.substring("[command]".length).trim()
                player.performCommand(command)
            }
            
            else if (action.startsWith("[op]")) {
                val command = action.substring("[op]".length).trim()
                val isOp = player.isOp
                
                try {
                    player.isOp = true
                    player.performCommand(command)
                } finally {
                    player.isOp = isOp
                }
            }
            
            else if (action.startsWith("[console]")) {
                val command = action.substring("[console]".length).trim()
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
            }
            
            else if (action.startsWith("[sound]")) {
                val parts = action.substring("[sound]".length).trim().split(" ")
                val soundName = parts[0]
                val volume = if (parts.size > 1) parts[1].toFloatOrNull() ?: 1.0f else 1.0f
                val pitch = if (parts.size > 2) parts[2].toFloatOrNull() ?: 1.0f else 1.0f
                
                try {
                    val sound = Sound.valueOf(soundName)
                    player.playSound(player.location, sound, volume, pitch)
                } catch (e: IllegalArgumentException) {
                    plugin.logger.warning("无效的音效: $soundName")
                }
            }
            
            else if (action.startsWith("[message]")) {
                val message = action.substring("[message]".length).trim()
                player.sendMessage(MessageUtil.color(message))
            }
            
            else if (action.startsWith("[title]")) {
                val parts = action.substring("[title]".length).trim().split(" ", limit = 2)
                val title = MessageUtil.color(parts[0])
                val subtitle = if (parts.size > 1) MessageUtil.color(parts[1]) else ""
                
                player.sendTitle(title, subtitle, 10, 70, 20)
            }
            
            else if (action.startsWith("[drop_monster_spawner]") && location != null) {
                // 获取当前位置的刷怪笼实体类型
                val block = location.block
                val blockState = block.state
                if (blockState is org.bukkit.block.CreatureSpawner) {
                    val entityType = blockState.spawnedType
                    
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
                }
            }
            
            else if (action.startsWith("[drop]") && location != null) {
                val parts = action.substring("[drop]".length).trim().split(" ")
                val itemName = parts[0]
                val amount = if (parts.size > 1) parts[1].toIntOrNull() ?: 1 else 1
                
                // 处理自定义物品库，暂时不实现
                // 检查是否是原版物品
                try {
                    val material = Material.valueOf(itemName.uppercase())
                    val item = ItemStack(material, amount)
                    player.world.dropItemNaturally(location, item)
                } catch (e: IllegalArgumentException) {
                    plugin.logger.warning("无效的物品: $itemName")
                }
            }
            
            else if (action.startsWith("[give]")) {
                val parts = action.substring("[give]".length).trim().split(" ")
                val itemName = parts[0]
                val amount = if (parts.size > 1) parts[1].toIntOrNull() ?: 1 else 1
                
                // 处理自定义物品库，暂时不实现
                // 检查是否是原版物品
                try {
                    val material = Material.valueOf(itemName.uppercase())
                    val item = ItemStack(material, amount)
                    player.inventory.addItem(item)
                } catch (e: IllegalArgumentException) {
                    plugin.logger.warning("无效的物品: $itemName")
                }
            }
            
            else if (action.startsWith("[buff]")) {
                val parts = action.substring("[buff]".length).trim().split(" ")
                val effectName = parts[0]
                val duration = if (parts.size > 1) parts[1].toIntOrNull() ?: 30 else 30
                val amplifier = if (parts.size > 2) parts[2].toIntOrNull() ?: 0 else 0
                
                try {
                    val potionEffect = PotionEffectType.getByName(effectName)
                    if (potionEffect != null) {
                        player.addPotionEffect(PotionEffect(potionEffect, duration * 20, amplifier))
                    } else {
                        plugin.logger.warning("无效的药水效果: $effectName")
                    }
                } catch (e: Exception) {
                    plugin.logger.warning("无效的药水效果: $effectName")
                }
            }
            
            else if (action.startsWith("[money]") && plugin.getHookManager().isVaultEnabled()) {
                val parts = action.substring("[money]".length).trim().split(" ")
                val operation = parts[0].lowercase()
                val amount = if (parts.size > 1) parts[1].toDoubleOrNull() ?: 0.0 else 0.0
                
                when (operation) {
                    "give" -> plugin.getHookManager().getVaultHook()?.giveMoney(player, amount)
                    "take" -> plugin.getHookManager().getVaultHook()?.takeMoney(player, amount)
                    "set" -> plugin.getHookManager().getVaultHook()?.setMoney(player, amount)
                }
            }
            
            else if (action.startsWith("[points]") && plugin.getHookManager().isPlayerPointsEnabled()) {
                val parts = action.substring("[points]".length).trim().split(" ")
                val operation = parts[0].lowercase()
                val amount = if (parts.size > 1) parts[1].toIntOrNull() ?: 0 else 0
                
                when (operation) {
                    "give" -> plugin.getHookManager().getPlayerPointsHook()?.givePoints(player, amount)
                    "take" -> plugin.getHookManager().getPlayerPointsHook()?.takePoints(player, amount)
                    "set" -> plugin.getHookManager().getPlayerPointsHook()?.setPoints(player, amount)
                }
            }
            
        } catch (e: Exception) {
            plugin.logger.warning("执行动作时发生错误: $action")
            plugin.logger.warning("错误信息: ${e.message}")
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                e.printStackTrace()
            }
        }
    }
} 
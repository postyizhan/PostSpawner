package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.CreatureSpawner
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 掉落刷怪笼动作处理器
 */
class DropMonsterSpawnerAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        if (location == null) return
        
        // 获取当前位置的刷怪笼实体类型
        val block = location.block
        val blockState = block.state
        if (blockState is CreatureSpawner) {
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
}

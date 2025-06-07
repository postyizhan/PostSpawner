package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 掉落物品动作处理器
 */
class DropAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        if (location == null) return
        
        val content = extractActionValue(actionValue, ActionType.DROP.prefix)
        val parts = content.split(" ")
        val itemId = parts[0]
        val amount = if (parts.size > 1) parts[1].toIntOrNull() ?: 1 else 1
        
        // 从物品钩子获取物品
        val item = plugin.getHookManager().getItem(itemId, player)
        if (item != null) {
            item.amount = amount
            player.world.dropItemNaturally(location, item)
        } else {
            // 尝试作为原版物品处理
            try {
                val material = Material.matchMaterial(itemId)
                if (material != null) {
                    val vanillaItem = ItemStack(material, amount)
                    player.world.dropItemNaturally(location, vanillaItem)
                } else {
                    logWarning(MessageUtil.getMessage("system.debug.action.invalid_item").replace("%item%", itemId))
                }
            } catch (e: Exception) {
                logWarning(MessageUtil.getMessage("system.debug.action.invalid_item").replace("%item%", itemId))
            }
        }
    }
}

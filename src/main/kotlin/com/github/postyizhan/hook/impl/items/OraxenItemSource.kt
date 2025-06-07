package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import io.th0rgal.oraxen.api.OraxenItems

/**
 * Oraxen物品源
 */
class OraxenItemSource : ItemSourceHook {

    override fun getSourceId(): String = "oraxen"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // 尝试从Oraxen获取物品
            if (!OraxenItems.exists(itemId)) {
                return null
            }
            return OraxenItems.getItemById(itemId).build()
        } catch (e: Exception) {
            return null
        }
    }
}

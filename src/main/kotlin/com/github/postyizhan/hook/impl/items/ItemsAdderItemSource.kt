package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import dev.lone.itemsadder.api.CustomStack

/**
 * ItemsAdder物品源
 */
class ItemsAdderItemSource : ItemSourceHook {

    override fun getSourceId(): String = "itemsadder"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // 尝试从ItemsAdder获取物品
            val customStack = CustomStack.getInstance(itemId)
            return customStack?.itemStack
        } catch (e: Exception) {
            return null
        }
    }
}

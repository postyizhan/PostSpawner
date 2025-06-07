package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import pers.neige.neigeitems.manager.ItemManager

/**
 * NeigeItems物品源
 */
class NeigeItemsItemSource : ItemSourceHook {

    override fun getSourceId(): String = "neigeitems"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // 尝试从NeigeItems获取物品
            return if (player != null) {
                // 如果提供了玩家，则使用带玩家的API
                ItemManager.getItemStack(itemId, player)
            } else {
                // 否则使用不带玩家的API
                ItemManager.getItemStack(itemId)
            }
        } catch (e: Exception) {
            return null
        }
    }
}

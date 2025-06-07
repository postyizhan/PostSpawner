package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import github.saukiya.sxitem.SXItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * SX-Item物品源
 */
class SXItemItemSource : ItemSourceHook {

    override fun getSourceId(): String = "sxitem"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // SX-Item格式为 ID:参数1:参数2...
            val sxItem = itemId.split(":")
            val sxID = sxItem[0]
            val sxArgs = sxItem.toList().drop(1)
            
            // 获取物品
            return if (player != null) {
                // 如果提供了玩家，则使用带玩家的API
                SXItem.getItemManager().getItem(sxID, player, *sxArgs.toTypedArray())
            } else {
                // SX-Item需要玩家对象
                null
            }
        } catch (e: Exception) {
            return null
        }
    }
}

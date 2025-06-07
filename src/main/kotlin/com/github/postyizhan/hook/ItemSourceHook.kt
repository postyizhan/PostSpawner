package com.github.postyizhan.hook

import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

/**
 * 物品源钩子接口
 * 用于从不同的物品库中获取物品
 */
interface ItemSourceHook {

    /**
     * 获取物品源的唯一标识符
     * 例如: minecraft, itemsadder, mmoitems
     */
    fun getSourceId(): String
    
    /**
     * 从物品ID获取物品
     * @param itemId 物品ID
     * @param player 玩家（某些物品库需要玩家对象来获取物品）
     * @return 物品堆，如果未找到则返回null
     */
    fun getItem(itemId: String, player: Player? = null): ItemStack?
    
    /**
     * 检查物品ID是否有效
     */
    fun isValidItem(itemId: String): Boolean {
        return getItem(itemId) != null
    }
}

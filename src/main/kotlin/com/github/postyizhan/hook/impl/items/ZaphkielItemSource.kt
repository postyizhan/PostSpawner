package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import ink.ptms.zaphkiel.Zaphkiel
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Zaphkiel物品源
 */
class ZaphkielItemSource : ItemSourceHook {

    override fun getSourceId(): String = "zaphkiel"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // 从Zaphkiel获取物品
            return Zaphkiel.api().getItemManager().generateItemStack(itemId, player)
        } catch (e: Exception) {
            return null
        }
    }
}

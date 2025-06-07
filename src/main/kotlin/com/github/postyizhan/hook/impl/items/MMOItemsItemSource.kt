package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.Type

/**
 * MMOItems物品源
 */
class MMOItemsItemSource : ItemSourceHook {

    override fun getSourceId(): String = "mmoitems"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // MMOItems格式为 TYPE:ID
            val parts = itemId.split(":")
            if (parts.size != 2) return null
            
            val typeId = parts[0]
            val id = parts[1]
            
            // 获取物品类型
            val type = Type.get(typeId) ?: return null
            
            // 获取物品
            return MMOItems.plugin.getItem(type, id)
        } catch (e: Exception) {
            return null
        }
    }
}

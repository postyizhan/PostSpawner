package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine
import net.momirealms.craftengine.core.util.Key
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * CraftEngine物品源
 */
class CraftEngineItemSource : ItemSourceHook {

    override fun getSourceId(): String = "craftengine"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            val (namespace, material) = itemId.split(":", limit = 2)
            val craftPlayer = BukkitCraftEngine.instance().adapt(player)
            val item = CraftEngineItems.byId(Key(namespace, material))
            return item?.buildItemStack(craftPlayer)
        } catch (e: Exception) {
            return null
        }
    }
}

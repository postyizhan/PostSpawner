package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import io.rokuko.azureflow.api.AzureFlowAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * AzureFlow物品源
 */
class AzureFlowItemSource : ItemSourceHook {

    override fun getSourceId(): String = "azureflow"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            val factory = AzureFlowAPI.getFactory(itemId)
            val stack = factory?.build()?.virtualItemStack(player) ?: return null
            return stack
        } catch (e: Exception) {
            return null
        }
    }
}

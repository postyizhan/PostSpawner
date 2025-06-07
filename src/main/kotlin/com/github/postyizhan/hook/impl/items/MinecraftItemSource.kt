package com.github.postyizhan.hook.impl.items

import com.github.postyizhan.hook.ItemSourceHook
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

/**
 * Minecraft原版物品源
 */
class MinecraftItemSource : ItemSourceHook {

    override fun getSourceId(): String = "minecraft"
    
    override fun getItem(itemId: String, player: Player?): ItemStack? {
        try {
            // 尝试解析为Material
            val material = Material.matchMaterial(itemId)
            return if (material != null) {
                ItemStack(material)
            } else {
                null
            }
        } catch (e: Exception) {
            return null
        }
    }
}

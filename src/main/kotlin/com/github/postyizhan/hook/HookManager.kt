package com.github.postyizhan.hook

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.hook.impl.ItemSourceHookManager
import com.github.postyizhan.hook.impl.PlaceholderAPIHook
import com.github.postyizhan.hook.impl.PlayerPointsHook
import com.github.postyizhan.hook.impl.VaultHook
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 钩子管理器，负责管理对其他插件的支持
 */
class HookManager(private val plugin: PostSpawner) {

    private var vaultHook: VaultHook? = null
    private var playerPointsHook: PlayerPointsHook? = null
    private var placeholderAPIHook: PlaceholderAPIHook? = null
    private var itemSourceHookManager: ItemSourceHookManager? = null

    init {
        // 初始化钩子
        setupVault()
        setupPlayerPoints()
        setupPlaceholderAPI()
        setupItemSource()
    }

    /**
     * 设置 Vault 经济支持
     */
    private fun setupVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            vaultHook = VaultHook(plugin)
            if (vaultHook!!.isEnabled()) {
                plugin.server.consoleSender.sendMessage(
                    MessageUtil.color(
                        MessageUtil.getMessage("system.hooks.enabled")
                            .replace("{0}", "Vault")
                    )
                )
            } else {
                vaultHook = null
                plugin.server.consoleSender.sendMessage(
                    MessageUtil.color(
                        MessageUtil.getMessage("system.hooks.disabled")
                            .replace("{0}", "Vault Economy")
                    )
                )
            }
        } else {
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.disabled")
                        .replace("{0}", "Vault")
                )
            )
        }
    }

    /**
     * 设置 PlayerPoints 支持
     */
    private fun setupPlayerPoints() {
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
            playerPointsHook = PlayerPointsHook(plugin)
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.enabled")
                        .replace("{0}", "PlayerPoints")
                )
            )
        } else {
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.disabled")
                        .replace("{0}", "PlayerPoints")
                )
            )
        }
    }

    /**
     * 设置 PlaceholderAPI 支持
     */
    private fun setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPIHook = PlaceholderAPIHook(plugin)
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.enabled")
                        .replace("{0}", "PlaceholderAPI")
                )
            )
        } else {
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.disabled")
                        .replace("{0}", "PlaceholderAPI")
                )
            )
        }
    }
    
    /**
     * 设置物品源支持
     */
    private fun setupItemSource() {
        try {
            itemSourceHookManager = ItemSourceHookManager(plugin)
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.enabled")
                        .replace("{0}", "Item Source Manager")
                )
            )
        } catch (e: Exception) {
            plugin.server.consoleSender.sendMessage(
                MessageUtil.color(
                    MessageUtil.getMessage("system.hooks.disabled")
                        .replace("{0}", "Item Source Manager")
                )
            )
        }
    }

    /**
     * 获取 Vault 经济支持
     */
    fun getVaultHook(): VaultHook? {
        return vaultHook
    }

    /**
     * 获取 PlayerPoints 支持
     */
    fun getPlayerPointsHook(): PlayerPointsHook? {
        return playerPointsHook
    }

    /**
     * 获取 PlaceholderAPI 支持
     */
    fun getPlaceholderAPIHook(): PlaceholderAPIHook? {
        return placeholderAPIHook
    }
    
    /**
     * 获取物品源钩子管理器
     */
    fun getItemSourceHookManager(): ItemSourceHookManager? {
        return itemSourceHookManager
    }

    /**
     * Vault 经济支持是否启用
     */
    fun isVaultEnabled(): Boolean {
        return vaultHook != null && vaultHook!!.isEnabled()
    }

    /**
     * PlayerPoints 支持是否启用
     */
    fun isPlayerPointsEnabled(): Boolean {
        return playerPointsHook != null
    }

    /**
     * PlaceholderAPI 支持是否启用
     */
    fun isPlaceholderAPIEnabled(): Boolean {
        return placeholderAPIHook != null
    }
    
    /**
     * 物品源支持是否启用
     */
    fun isItemSourceEnabled(): Boolean {
        return itemSourceHookManager != null
    }
    
    /**
     * 从物品ID获取物品
     */
    fun getItem(itemString: String, player: Player? = null): ItemStack? {
        return if (isItemSourceEnabled()) {
            itemSourceHookManager?.getItem(itemString, player)
        } else null
    }
}

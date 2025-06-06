package com.github.postyizhan.hook

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.hook.impl.PlaceholderAPIHook
import com.github.postyizhan.hook.impl.PlayerPointsHook
import com.github.postyizhan.hook.impl.VaultHook
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Bukkit

/**
 * 钩子管理器，负责管理对其他插件的支持
 */
class HookManager(private val plugin: PostSpawner) {

    private var vaultHook: VaultHook? = null
    private var playerPointsHook: PlayerPointsHook? = null
    private var placeholderAPIHook: PlaceholderAPIHook? = null

    init {
        // 初始化钩子
        setupVault()
        setupPlayerPoints()
        setupPlaceholderAPI()
    }

    /**
     * 设置 Vault 经济支持
     */
    private fun setupVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            vaultHook = VaultHook(plugin)
            if (vaultHook!!.isEnabled()) {
                plugin.logger.info(
                    MessageUtil.formatMessage(
                        MessageUtil.getMessage("system.hooks.enabled"),
                        "Vault"
                    )
                )
            } else {
                vaultHook = null
                plugin.logger.warning(
                    MessageUtil.formatMessage(
                        MessageUtil.getMessage("system.hooks.disabled"),
                        "Vault Economy"
                    )
                )
            }
        } else {
            plugin.logger.info(
                MessageUtil.formatMessage(
                    MessageUtil.getMessage("system.hooks.disabled"),
                    "Vault"
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
            plugin.logger.info(
                MessageUtil.formatMessage(
                    MessageUtil.getMessage("system.hooks.enabled"),
                    "PlayerPoints"
                )
            )
        } else {
            plugin.logger.info(
                MessageUtil.formatMessage(
                    MessageUtil.getMessage("system.hooks.disabled"),
                    "PlayerPoints"
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
            plugin.logger.info(
                MessageUtil.formatMessage(
                    MessageUtil.getMessage("system.hooks.enabled"),
                    "PlaceholderAPI"
                )
            )
        } else {
            plugin.logger.info(
                MessageUtil.formatMessage(
                    MessageUtil.getMessage("system.hooks.disabled"),
                    "PlaceholderAPI"
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
} 
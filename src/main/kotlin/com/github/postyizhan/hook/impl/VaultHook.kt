package com.github.postyizhan.hook.impl

import com.github.postyizhan.PostSpawner
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Vault 钩子实现
 */
class VaultHook(private val plugin: PostSpawner) {
    
    private var economy: Economy? = null
    
    init {
        setupEconomy()
    }
    
    /**
     * 设置经济
     */
    private fun setupEconomy(): Boolean {
        if (Bukkit.getServer().pluginManager.getPlugin("Vault") == null) {
            return false
        }
        
        val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)
        if (rsp != null) {
            economy = rsp.provider
            return true
        }
        
        return false
    }
    
    /**
     * 检查是否启用
     */
    fun isEnabled(): Boolean {
        return economy != null
    }
    
    /**
     * 给予玩家金钱
     */
    fun giveMoney(player: Player, amount: Double): Boolean {
        if (!isEnabled()) return false
        return economy!!.depositPlayer(player, amount).transactionSuccess()
    }
    
    /**
     * 扣除玩家金钱
     */
    fun takeMoney(player: Player, amount: Double): Boolean {
        if (!isEnabled()) return false
        return economy!!.withdrawPlayer(player, amount).transactionSuccess()
    }
    
    /**
     * 设置玩家金钱
     */
    fun setMoney(player: Player, amount: Double): Boolean {
        if (!isEnabled()) return false
        val eco = economy ?: return false
        
        if (eco.has(player, eco.getBalance(player))) {
            eco.withdrawPlayer(player, eco.getBalance(player))
        }
        return eco.depositPlayer(player, amount).transactionSuccess()
    }
    
    /**
     * 获取玩家金钱
     */
    fun getMoney(player: Player): Double {
        if (!isEnabled()) return 0.0
        return economy!!.getBalance(player)
    }
} 
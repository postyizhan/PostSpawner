package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 控制台命令动作处理器
 */
class ConsoleCommandAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val command = extractActionValue(actionValue, ActionType.CONSOLE_COMMAND.prefix)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}

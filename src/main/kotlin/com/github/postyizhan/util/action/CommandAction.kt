package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 命令动作处理器
 */
class CommandAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val command = extractActionValue(actionValue, ActionType.COMMAND.prefix)
        player.performCommand(command)
    }
}

package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * OP命令动作处理器
 */
class OpCommandAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        val command = extractActionValue(actionValue, ActionType.OP_COMMAND.prefix)
        val isOp = player.isOp
        
        try {
            player.isOp = true
            player.performCommand(command)
        } finally {
            player.isOp = isOp
        }
    }
}

package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import com.github.postyizhan.util.MessageUtil
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 未知动作处理器
 */
class UnknownAction(plugin: PostSpawner) : AbstractAction(plugin) {
    override fun execute(player: Player, actionValue: String, location: Location?) {
        logWarning(MessageUtil.getMessage("system.debug.action.unknown_action").replace("%action%", actionValue))
    }
}

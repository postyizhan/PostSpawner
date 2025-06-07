package com.github.postyizhan.util.action

import com.github.postyizhan.PostSpawner
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 动作抽象基类
 */
abstract class AbstractAction(protected val plugin: PostSpawner) : Action {
    /**
     * 提取动作参数
     * @param action 原始动作字符串
     * @param prefix 动作前缀
     * @return 提取后的参数部分
     */
    protected fun extractActionValue(action: String, prefix: String): String {
        return action.substring(prefix.length).trim()
    }
    
    /**
     * 记录警告日志
     * @param message 警告消息
     */
    protected fun logWarning(message: String) {
        plugin.logger.warning(message)
    }
}

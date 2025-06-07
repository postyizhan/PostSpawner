package com.github.postyizhan.util.action

/**
 * 动作类型枚举
 */
enum class ActionType(val prefix: String) {
    COMMAND("[command]"),
    OP_COMMAND("[op]"),
    CONSOLE_COMMAND("[console]"),
    SOUND("[sound]"),
    MESSAGE("[message]"),
    TITLE("[title]"),
    DROP_MONSTER_SPAWNER("[drop_monster_spawner]"),
    DROP("[drop]"),
    GIVE("[give]"),
    BUFF("[buff]"),
    MONEY("[money]"),
    POINTS("[points]"),
    UNKNOWN("");
    
    companion object {
        /**
         * 根据动作内容获取动作类型
         * @param action 动作内容
         * @return 对应的动作类型
         */
        fun fromAction(action: String): ActionType {
            return values().firstOrNull { action.startsWith(it.prefix) } ?: UNKNOWN
        }
    }
}

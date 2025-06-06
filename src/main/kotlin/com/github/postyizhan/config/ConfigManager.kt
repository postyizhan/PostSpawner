package com.github.postyizhan.config

import com.github.postyizhan.PostSpawner
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * 配置管理器，负责处理插件的配置文件
 */
class ConfigManager(private val plugin: PostSpawner) {
    private val configs = mutableMapOf<String, FileConfiguration>()
    private val configFiles = mutableMapOf<String, File>()
    
    /**
     * 加载所有配置文件
     */
    fun loadAll() {
        loadConfig("config.yml")
        
        // 检查并加载语言文件，重载时不保存语言文件
        val langCode = getConfig().getString("language", "zh_CN") ?: "zh_CN"
        loadResource("lang/$langCode.yml", "lang.yml", false)
    }
    
    /**
     * 加载主配置文件
     */
    fun loadConfig(filename: String): FileConfiguration {
        val file = File(plugin.dataFolder, filename)
        
        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource(filename, false)
        }
        
        val config = YamlConfiguration.loadConfiguration(file)
        configs["config"] = config
        configFiles["config"] = file
        
        return config
    }
    
    /**
     * 从资源文件夹加载配置文件
     * @param resourcePath 资源文件路径
     * @param savePath 保存路径
     * @param saveIfNotExists 如果文件不存在是否保存资源文件，默认为true
     */
    fun loadResource(resourcePath: String, savePath: String, saveIfNotExists: Boolean = true): FileConfiguration {
        val resource: InputStream? = plugin.getResource(resourcePath)
        val file = File(plugin.dataFolder, savePath)
        
        if (!file.exists() && saveIfNotExists) {
            file.parentFile.mkdirs()
            if (resource != null) {
                plugin.saveResource(resourcePath, false)
            }
        }
        
        val config = if (resource != null && !file.exists()) {
            // 如果文件不存在，从资源加载
            val reader = InputStreamReader(resource, StandardCharsets.UTF_8)
            YamlConfiguration.loadConfiguration(reader)
        } else {
            // 如果文件存在，从文件加载
            YamlConfiguration.loadConfiguration(file)
        }
        
        val key = savePath.replace(".yml", "")
        configs[key] = config
        configFiles[key] = file
        
        return config
    }
    
    /**
     * 获取主配置文件
     */
    fun getConfig(): FileConfiguration {
        return configs["config"] ?: loadConfig("config.yml")
    }
    
    /**
     * 获取语言配置文件
     */
    fun getLang(): FileConfiguration {
        return configs["lang"] ?: loadResource("lang/zh_CN.yml", "lang.yml", false)
    }
    
    /**
     * 获取指定配置文件
     */
    fun getCustomConfig(name: String): FileConfiguration? {
        return configs[name]
    }
    
    /**
     * 保存主配置文件
     */
    fun saveConfig() {
        val config = configs["config"] ?: return
        val file = configFiles["config"] ?: return
        config.save(file)
    }
    
    /**
     * 保存指定配置文件
     */
    fun saveCustomConfig(name: String) {
        val config = configs[name] ?: return
        val file = configFiles[name] ?: return
        config.save(file)
    }
}

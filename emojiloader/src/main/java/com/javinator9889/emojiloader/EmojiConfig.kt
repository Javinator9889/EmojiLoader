/*
 * Copyright © 2020 - present | EmojiLoader by Javinator9889
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 *
 * Created by Javinator9889 on 17/08/20 - EmojiLoader.
 */
package com.javinator9889.emojiloader

import android.content.Context
import androidx.emoji.text.EmojiCompat

/**
 * Object class for loading a previously set configuration. This is a singleton which stores
 * the first loaded configuration (primarily for avoiding calling the other module or Google Play
 * Services so much, which has a major impact in performance) and returns it unit is reset, it
 * is, forced the reload of the font.
 *
 * Two methods are available:
 *  + [get], which returns the stored config, if any, or creates a new one and returns it.
 *  + [reset], which marks the config to be reset, it is, "reloaded". Take into account that this
 *  method **does not resets** any [EmojiCompat] instance or [EmojiLoader] class. You must call
 *  [set] again and re-create your [EmojiCompat] instance.
 *
 * Note: this class is globally thread-safe/co-routine safe, which means you can access any of
 * its methods securely from any thread.
 */
object EmojiConfig {
    /** Global instance lock - for synchronization purposes **/
    private val instanceLock = Object()

    /** The stored configuration. Initialized when calling [get] **/
    private lateinit var config: EmojiCompat.Config

    /** Whether to reload (or not) the configuration when calling [set] **/
    private var isConfigInit = false

    /**
     * Using the specified [options], loads the [EmojiCompat.Config] configuration file from either
     * Google Play Services (using [EmojiConfigGMSLoader]) or the bundled emoji class
     * ([com.javinator9889.bundledemoji.BundledEmojiConfig]). If [reset] was not called, this
     * method will always return the same [EmojiCompat.Config] object.
     *
     * @param[context] the runtime context of the application. Notice that application's context
     *                 is preferred over an activity's context or fragment's context.
     * @param[options] a set of options that allows customization over the [EmojiCompat.Config]
     *                 instance.
     * @return the [EmojiCompat.Config] instance initialized.
     * @throws[ClassNotFoundException] if [EmojiLoaderOptions.useBundledEmojiCompat] is set but
     * the class [com.javinator9889.bundledemoji.BundledEmojiConfig] is not available (you must
     * "implement" the bundledemoji package).
     */
    fun get(
        context: Context,
        options: EmojiLoaderOptions
    ): EmojiCompat.Config = synchronized(instanceLock) {
        if (isConfigInit && ::config.isInitialized) return config
        val provider = if (options.useBundledEmojiCompat) {
            val className = "${IBundledEmojiConfig.PACKAGE_NAME}.${IBundledEmojiConfig.CLASS_NAME}"
            Class.forName(className).kotlin.objectInstance as IBundledEmojiConfig
        } else {
            EmojiConfigGMSLoader
        }
        config = provider.loadConfig(context)
        config.setReplaceAll(options.replaceAll)
        isConfigInit = true
        return config
    }

    /**
     * Resets the saved configuration and prepares the instance for being able to load the new
     * configuration using [set].
     */
    fun reset() {
        synchronized(instanceLock) { isConfigInit = false }
    }
}
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
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig


object EmojiConfig {
    private lateinit var config: EmojiCompat.Config
    private var useBundledEmojiCompat = false

    fun get(
        context: Context,
        replaceAll: Boolean = true,
        useBundledEmojiCompat: Boolean = false
    ): EmojiCompat.Config {
        if (this.useBundledEmojiCompat != useBundledEmojiCompat && ::config.isInitialized)
            return config
        this.useBundledEmojiCompat = useBundledEmojiCompat
        if (useBundledEmojiCompat) {
            val className = "${BundledEmojiConfig.PACKAGE_NAME}.${BundledEmojiConfig.CLASS_NAME}"
            val bundledProvider =
                Class.forName(className).kotlin.objectInstance as BundledEmojiConfig
            config = bundledProvider.loadConfig(context)
        } else {
            with(
                FontRequest(
                    "com.google.android.gms.fonts",
                    "com.google.android.gms",
                    "Noto Color Emoji Compat",
                    R.array.com_google_android_gms_fonts_certs
                )
            ) {
                config = FontRequestEmojiCompatConfig(context, this)
            }
        }
        config.setReplaceAll(replaceAll)
        return config
    }
}
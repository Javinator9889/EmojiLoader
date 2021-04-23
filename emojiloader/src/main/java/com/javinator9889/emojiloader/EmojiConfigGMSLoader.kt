/*
 * Copyright © 2021 - present | EmojiLoader by Javinator9889
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
 * Created by Javinator9889 on 23/04/21 - EmojiLoader.
 */
package com.javinator9889.emojiloader

import android.content.Context
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig

/**
 * Loads the available emojis using the Google Play Services available Noto Font.
 * Notice that this is the preferred loading method as this reduces the APK size.
 *
 * @see IBundledEmojiConfig
 */
object EmojiConfigGMSLoader : IBundledEmojiConfig {
    private const val PROVIDER_AUTH = "com.google.android.gms.fonts"
    private const val PROVIDER_PACK = "com.google.android.gms"
    private const val QUERY = "Noto Color Emoji Compat"

    override fun loadConfig(context: Context): EmojiCompat.Config =
        with(
            FontRequest(
                PROVIDER_AUTH,
                PROVIDER_PACK,
                QUERY,
                R.array.com_google_android_gms_fonts_certs
            )
        ) {
            FontRequestEmojiCompatConfig(context, this)
        }
}
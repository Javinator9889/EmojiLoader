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

interface IBundledEmojiConfig {
    companion object {
        const val PACKAGE_NAME = "com.javinator9889.bundledemoji"
        const val CLASS_NAME = "BundledEmojiConfig"
    }

    fun loadConfig(context: Context): EmojiCompat.Config
}
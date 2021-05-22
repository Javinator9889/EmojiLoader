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
package com.javinator9889.bundledemoji

import android.content.Context
import androidx.emoji.bundled.BundledEmojiCompatConfig
import com.javinator9889.emojiloader.IBundledEmojiConfig

/**
 * Base object inheriting from [IBundledEmojiConfig] which is used when the
 * [com.javinator9889.emojiloader.EmojiLoaderOptions.useBundledEmojiCompat] is set to `true`.
 *
 * When [com.javinator9889.emojiloader.EmojiConfig] loads the configuration uses Kotlin's
 * reflection to dynamically load the class by accessing package name. Notice that enabling the
 * [com.javinator9889.emojiloader.EmojiLoaderOptions.useBundledEmojiCompat] but not adding the
 * `bundledemoji` package will lead to a [ClassNotFoundException] error.
 *
 * @see com.javinator9889.emojiloader.EmojiConfig
 */
object BundledEmojiConfig : IBundledEmojiConfig {
    override fun loadConfig(context: Context) = BundledEmojiCompatConfig(context)
}
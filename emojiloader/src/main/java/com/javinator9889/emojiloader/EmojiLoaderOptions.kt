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

import kotlinx.coroutines.CoroutineScope

/**
 * Template that allows multiple configurations for the [EmojiLoader]
 * instance. Notice that there is no class that directly inherits from this
 * interface but some objects use it for creating a basic configuration.
 *
 * You won't need to create an own class implementing this interface but changing the
 * [EmojiLoader.options] object.
 */
interface EmojiLoaderOptions {
    /**
     * The scope to which the [EmojiLoader] will be attached. If not provided, [EmojiLoader.options]
     * uses [kotlinx.coroutines.GlobalScope].
     */
    var coroutineScope: CoroutineScope

    /**
     * Determines whether EmojiCompat should replace all the emojis it finds with the EmojiSpans.
     * By default EmojiCompat tries its best to understand if the system already can render an
     * emoji and do not replace those emojis. If not provided, defaults to `false`.
     *
     * @see androidx.emoji.text.EmojiCompat.Config.setReplaceAll
     */
    var replaceAll: Boolean

    /**
     * Determines whether to use the embedded fonts package or accessing Play Services' fonts.
     * Notice that using bundled emoji increases (a lot) application's size, but it's useful when
     * distributing a application from a store different than Play Store or for older devices
     * (Android 4.4 or lower). If not provided, defaults to `false`.
     */
    var useBundledEmojiCompat: Boolean
}
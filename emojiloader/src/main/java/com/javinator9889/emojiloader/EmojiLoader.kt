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
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object EmojiLoader {
    private lateinit var emojiCompat: EmojiCompat
    var coroutineScope: CoroutineScope = GlobalScope

    fun loadAsync(
        context: Context,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Deferred<EmojiCompat> = coroutineScope.async(context = coroutineContext) {
        try {
            if (::emojiCompat.isInitialized)
                return@async emojiCompat
            emojiCompat = EmojiCompat.get()
            return@async emojiCompat
        } catch (_: IllegalStateException) {
            return@async emojiCompat
        } catch (err: Throwable) {
            return@async emojiCompat
        }
    }
}
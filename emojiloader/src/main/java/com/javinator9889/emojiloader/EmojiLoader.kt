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
import android.util.Log
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.EmojiCompat.LOAD_STATE_SUCCEEDED
import kotlinx.coroutines.*
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object EmojiLoader {
    private val TAG = EmojiLoader::class.simpleName!!
    private val instanceLock = Object()
    private lateinit var emojiCompat: EmojiCompat
    var coroutineScope: CoroutineScope = GlobalScope

    fun loadAsync(
        context: Context,
        coroutineContext: CoroutineContext = Dispatchers.IO,
        replaceAll: Boolean = false,
        useBundledEmojiCompat: Boolean = false
    ): Deferred<EmojiCompat> = coroutineScope.async(context = coroutineContext) {
        try {
            synchronized(instanceLock) {
                if (::emojiCompat.isInitialized && emojiCompat.loadState == LOAD_STATE_SUCCEEDED) {
                    Log.d("${TAG}/loadAsync", "Obtaining previously generated instance")
                    return@async emojiCompat
                }
                Log.d("${TAG}/loadAsync", "Trying to fetch singleton for the first time")
                emojiCompat = EmojiCompat.get()
            }
            return@async emojiCompat
        } catch (_: IllegalStateException) {
            Log.d("${TAG}/loadAsync", "The library is not initialized yet so initializing")
            val emojiLoadDeferred = CompletableDeferred<EmojiCompat>()
            val emojiCompat = EmojiConfig.get(context, replaceAll, useBundledEmojiCompat).run {
                EmojiCompat.init(this)
            }
            emojiCompat.registerInitCallback(object : EmojiCompat.InitCallback() {
                override fun onInitialized() {
                    super.onInitialized()
                    emojiCompat.unregisterInitCallback(this)
                    emojiLoadDeferred.complete(EmojiCompat.get())
                }

                override fun onFailed(throwable: Throwable?) {
                    super.onFailed(throwable)
                    emojiCompat.unregisterInitCallback(this)
                    val exception = throwable ?: RuntimeException("EmojiCompat failed to load")
                    emojiLoadDeferred.completeExceptionally(exception)
                }
            })
            with(emojiLoadDeferred.await()) {
                synchronized(instanceLock) {
                    this@EmojiLoader.emojiCompat = this
                }
            }
            return@async this@EmojiLoader.emojiCompat
        } catch (err: Throwable) {
            Log.e(
                "${TAG}/loadAsync",
                "Unexpected error occurred during EmojiCompat initialization!",
                err
            )
            throw err
        }
    }
}
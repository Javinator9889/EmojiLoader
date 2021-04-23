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

/**
 * EmojiLoader is the main class of this module. By using [Kotlin coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
 * this object loads asynchronously the required [EmojiCompat] instance, which usually takes
 * some precious time.
 *
 * This object exposes a single method [loadAsync] as well as the available [options], for
 * customizing the [EmojiCompat] instance. A typical code workflow would be:
 *
 * ```kotlin
 * // Assume we are declaring some private variables at an activity's body
 * private lateinit var loader: Deferred<EmojiCompat>
 *
 * // Assume we are at the "onCreate" method of an activity
 * EmojiLoader.options.coroutineScope = activityScope
 * EmojiLoader.options.replaceAll = true
 *
 * if (!::loader.isInitialized) loader = EmojiLoader.loadAsync(applicationContext)
 *
 * // Assume we are on another method which is called later after initialization.
 * // < CAUTION! As this is a coroutine, you will need a suspendable method >
 * val emojiCompat = loader.await()
 * textView.text = emojiCompat.process("U+1F600")
 * ```
 *
 * Take into account that this instance produces both debugging and error messages through
 * [Log], so please check those if any error happens.
 *
 * Note: this instance is thread-safe/coroutine-safe, which means that it can be accessed
 * by multiple threads at the same time without errors.
 */
object EmojiLoader {
    /** Instance tag user when calling [Log]'s methods **/
    private val TAG = EmojiLoader::class.simpleName!!

    /** Instance lock used for synchronization purposes **/
    private val instanceLock = Object()

    /** [EmojiCompat] saved instance for instant returning when created and configurated **/
    private lateinit var emojiCompat: EmojiCompat

    /**
     * Multiple accessible options used when configuring the [EmojiCompat.Config] instance.
     *
     * Access them by addressing:
     * ```kotlin
     * EmojiLoader.options.replaceAll = true
     * // ...
     * ```
     *
     * @see EmojiLoaderOptions
     */
    val options = object : EmojiLoaderOptions {
        override var coroutineScope: CoroutineScope = GlobalScope
        override var replaceAll: Boolean = true
        override var useBundledEmojiCompat: Boolean = false
    }

    /**
     * Loads asynchronously the [EmojiCompat] client. Notice that this method uses coroutines for
     * handling the process in background, so you will need a suspendable method (or another
     * coroutine scope) for properly await until method's completion.
     */
    fun loadAsync(
        context: Context,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Deferred<EmojiCompat> = options.coroutineScope.async(context = coroutineContext) {
        try {
            synchronized(instanceLock) {
                if (::emojiCompat.isInitialized && emojiCompat.loadState == LOAD_STATE_SUCCEEDED) {
                    Log.d("${TAG}\$loadAsync", "Obtaining previously generated instance")
                    return@async emojiCompat
                }
                Log.d("${TAG}/loadAsync", "Trying to fetch singleton for the first time")
                emojiCompat = EmojiCompat.get()
            }
            return@async emojiCompat
        } catch (_: IllegalStateException) {
            Log.d("${TAG}\$loadAsync", "The library is not initialized yet so initializing")
            val emojiLoadDeferred = CompletableDeferred<EmojiCompat>()
            val emojiCompat = EmojiConfig.get(context, options).run { EmojiCompat.init(this) }
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
                "${TAG}\$loadAsync",
                "Unexpected error occurred during EmojiCompat initialization!",
                err
            )
            throw err
        }
    }
}
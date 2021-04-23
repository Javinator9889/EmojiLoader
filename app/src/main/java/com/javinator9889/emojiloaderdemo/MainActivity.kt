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
package com.javinator9889.emojiloaderdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.text.EmojiCompat
import com.javinator9889.emojiloader.EmojiLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val activityJob = Job()
    private val activityScope = CoroutineScope(Dispatchers.Default + activityJob)
    private var stopped = true
    private lateinit var loader: Deferred<EmojiCompat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EmojiLoader.options.coroutineScope = activityScope
        EmojiLoader.options.replaceAll = true
        if (BuildConfig.FLAVOR == "full") EmojiLoader.options.useBundledEmojiCompat = true
        if (!::loader.isInitialized) {
            loader = EmojiLoader.loadAsync(this)
        }
        startButton.setOnClickListener(this)
    }

    override fun finish() {
        activityJob.cancel("Activity destroyed")
        super.finish()
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        if (v != startButton)
            return
        if (stopped) startButton.text = "Tap to stop"
        stopped = !stopped
        activityScope.launch {
            val emojiCompat = loader.await()
            val emojis = resources.getStringArray(R.array.emojis)
            for (emoji in emojis) {
                withContext(Dispatchers.Main) {
                    emojiView += "${emojiCompat.process(emoji)} "
                    scrollView.fullScroll(View.FOCUS_DOWN)
                }
                if (stopped) break
            }
            withContext(Dispatchers.Main) { startButton.text = "Tap to start" }
            stopped = true
        }
    }
}

internal operator fun TextView.plusAssign(text: CharSequence) = append(text)

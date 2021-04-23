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
 * Global interface that must be implemented by the bundled emoji class
 * itself. Currently, this is used by [com.javinator9889.bundledemoji.BundledEmojiConfig]
 * class, which provides an easy-to-use object for loading all the available emojis at once.
 *
 * Since version 1.0, this is also used by [EmojiConfigGMSLoader] class for loading from
 * Google Play Services.
 *
 * This is interesting for:
 *  + Loading all emojis directly for slow or old phones.
 *  + Be able to use every emoji independently if the phone has or not Google Play Services.
 *  + Use this application with third party providers such as F-Droid or similar.
 *  + Embed the fonts directly into the device so the app can work mostly offline.
 *
 * But, take in mind that embedding the fonts in the APK file carries a HUGE increase in size
 * (estimated about ~4 MB), so prefer using the Google Play Services font loading.
 *
 * @author Javinator9889
 * @since v1.0
 */
interface IBundledEmojiConfig {
    companion object {
        /**
         * Package name used for loading the class using reflection. It's immutable and
         * should not be edited or modified.
         */
        const val PACKAGE_NAME = "com.javinator9889.bundledemoji"

        /**
         * Class name used for loading it using reflection. It's immutable and
         * should not be edited or modified.
         */
        const val CLASS_NAME = "BundledEmojiConfig"
    }

    /**
     * By using the provided [context], loads the configuration telling the [EmojiCompat]
     * library which configuration to use.
     *
     * This method should be overridden by any configuration loader that you may use. By default,
     * the class [EmojiLoader] picks the best option based on your requirements.
     *
     * @param[context] the context in which this method is called. Notice that application's context
     *                 is preferred over the activity's context and/or fragment's context.
     * @return the specific [EmojiCompat.Config] configuration file used by the project.
     */
    fun loadConfig(context: Context): EmojiCompat.Config
}
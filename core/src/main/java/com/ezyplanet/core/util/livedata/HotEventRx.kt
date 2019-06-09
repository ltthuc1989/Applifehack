/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain initView copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ezyplanet.thousandhands.util.livedata

import androidx.lifecycle.Observer

open class HotEventRx<out T>(private val content: T) {


    var hasBeenHandled = false
        private set // Allow external read but not write

//    /**
//     * Returns the content and prevents its use again.
//     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content


}
class HotEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<HotEventRx<T>> {
    override fun onChanged(event: HotEventRx<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}

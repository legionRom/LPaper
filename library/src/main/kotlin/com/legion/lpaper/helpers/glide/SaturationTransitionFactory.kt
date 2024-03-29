/*
 * Copyright (c) 2019. Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.legion.lpaper.helpers.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory

/**
 * Credits: https://github.com/chrisbanes/tivi/
 */
class SaturationTransitionFactory : TransitionFactory<Drawable> {
    override fun build(dataSource: DataSource, isFirstResource: Boolean): Transition<Drawable> {
        return if (isFirstResource && dataSource != DataSource.MEMORY_CACHE) {
            // Only start the transition if this is not a recent load. We approximate that by
            // checking if the image is from the memory cache
            SaturationTransition()
        } else {
            NoTransition<Drawable>()
        }
    }
}

internal class SaturationTransition : Transition<Drawable> {
    override fun transition(current: Drawable, adapter: Transition.ViewAdapter): Boolean {
        saturateDrawableAnimator(current, adapter.view).also {
            it.start()
        }
        // We want Glide to still set the drawable
        return false
    }
}

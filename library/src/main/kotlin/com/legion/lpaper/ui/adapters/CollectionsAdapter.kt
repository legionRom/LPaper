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
package com.legion.lpaper.ui.adapters

import android.view.ViewGroup
import ca.allanwang.kau.utils.inflate
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.legion.lpaper.R
import com.legion.lpaper.data.models.Collection
import com.legion.lpaper.data.models.Wallpaper
import com.legion.lpaper.helpers.glide.loadPicture
import com.legion.lpaper.helpers.utils.MAX_COLLECTIONS_LOAD
import com.legion.lpaper.ui.adapters.viewholders.CollectionHolder
import com.legion.lpaper.ui.adapters.viewholders.LPaperViewClickListener
import jahirfiquitiva.libs.kext.ui.adapters.RecyclerViewListAdapter
import java.util.Collections

class CollectionsAdapter(
    private val manager: RequestManager?,
    private val filledCollectionPreview: Boolean,
    private val provider: ViewPreloadSizeProvider<Wallpaper>,
    private val listener: LPaperViewClickListener<Collection, CollectionHolder>
                        ) :
    RecyclerViewListAdapter<Collection, CollectionHolder>(MAX_COLLECTIONS_LOAD),
    ListPreloader.PreloadModelProvider<Wallpaper> {
    
    override fun doBind(holder: CollectionHolder, position: Int, shouldAnimate: Boolean) =
        holder.setItem(manager, provider, list[position], listener)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder =
        CollectionHolder(
            parent.inflate(
                if (filledCollectionPreview) R.layout.item_collection_filled
                else R.layout.item_collection))
    
    override fun getPreloadItems(position: Int): MutableList<Wallpaper> =
        Collections.singletonList(list[position].bestCover)
    
    override fun getPreloadRequestBuilder(item: Wallpaper): RequestBuilder<*>? =
        manager?.loadPicture(item.thumbUrl, item.thumbUrl, withTransition = false)
    
    override fun onViewRecycled(holder: CollectionHolder) {
        holder.unbind()
        super.onViewRecycled(holder)
    }
    
    override fun getItemId(position: Int) = position.toLong()
}

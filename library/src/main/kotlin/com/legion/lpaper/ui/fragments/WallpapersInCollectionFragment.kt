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
package com.legion.lpaper.ui.fragments

import com.legion.lpaper.data.models.Collection
import com.legion.lpaper.data.models.Wallpaper
import com.legion.lpaper.helpers.extensions.jfilter
import com.legion.lpaper.ui.fragments.base.BaseWallpapersFragment
import jahirfiquitiva.libs.kext.extensions.formatCorrectly

internal class WallpapersInCollectionFragment : BaseWallpapersFragment() {
    
    private var collection: Collection? = null
    private var wallpapers = ArrayList<Wallpaper>()
    private var firstFavsModification = true
    private var fromViewer = false
    
    val newFavs = ArrayList<Wallpaper>()
    
    private fun getWallpapersInCollection(all: ArrayList<Wallpaper>): ArrayList<Wallpaper> {
        collection?.let {
            val collectionName = it.name
            return all.jfilter {
                it.collections.formatCorrectly().replace("_", " ").contains(collectionName, true)
            }
        }
        return ArrayList()
    }
    
    override fun doOnFavoritesChange(data: ArrayList<Wallpaper>) {
        super.doOnFavoritesChange(getWallpapersInCollection(data))
        if (!firstFavsModification) {
            newFavs.clear()
            newFavs.addAll(data)
        } else {
            firstFavsModification = false
        }
    }
    
    override fun doOnWallpapersChange(data: ArrayList<Wallpaper>, fromCollectionActivity: Boolean) {
        super.doOnWallpapersChange(data, fromCollectionActivity)
        getWallpapersInCollection(data).let {
            if (it.isNotEmpty()) wallsAdapter.setItems(it)
        }
    }
    
    override fun loadDataFromViewModel() {
        super.loadDataFromViewModel()
        wallpapersModel?.postResult(wallpapers)
    }
    
    companion object {
        fun create(
            collection: Collection,
            wallpapers: ArrayList<Wallpaper>,
            hasChecker: Boolean,
            fromViewer: Boolean = false
                  ):
            WallpapersInCollectionFragment {
            return WallpapersInCollectionFragment().apply {
                this.collection = collection
                this.wallpapers.clear()
                this.wallpapers.addAll(wallpapers)
                this.hasChecker = hasChecker
                this.fromViewer = fromViewer
            }
        }
    }
    
    override fun canOpenWall(): Boolean = !fromViewer
    override fun autoStartLoad(): Boolean = true
    override fun fromCollectionActivity(): Boolean = true
    override fun fromFavorites(): Boolean = false
    override fun showFavoritesIcon(): Boolean = true
}

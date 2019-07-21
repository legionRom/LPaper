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
package com.legion.lpaper.viewmodels

import jahirfiquitiva.libs.archhelpers.viewmodels.ItemViewModel
import com.legion.lpaper.data.models.Wallpaper
import com.legion.lpaper.data.models.WallpaperInfo
import com.legion.lpaper.helpers.remote.FramesUrlRequests
import jahirfiquitiva.libs.kext.extensions.hasContent

class WallpaperInfoViewModel : ItemViewModel<Wallpaper, WallpaperInfo>() {
    override fun internalLoad(param: Wallpaper): WallpaperInfo =
        FramesUrlRequests.requestFileInfo(param.url, param.dimensions.hasContent())
    
    override fun isOldDataValid(): Boolean = getData()?.isValid == true
}

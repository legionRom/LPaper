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

import com.anjlab.android.iab.v3.BillingProcessor
import jahirfiquitiva.libs.archhelpers.viewmodels.ListViewModel

class IAPViewModel : ListViewModel<Array<String>, IAPItem>() {
    var iapBillingProcessor: BillingProcessor? = null
    override fun internalLoad(param: Array<String>): ArrayList<IAPItem> {
        val iaps = ArrayList<IAPItem>()
        try {
            param.forEach {
                val id = it
                val item = iapBillingProcessor?.getPurchaseListingDetails(id)
                item?.let {
                    val name = it.title.substring(0, it.title.lastIndexOf("(")).trim()
                    iaps.add(IAPItem(id, name, it.priceText.trim()))
                }
            }
        } catch (ignored: Exception) {
        }
        return iaps
    }
}

data class IAPItem(val id: String, val name: String, private val price: String) {
    override fun toString(): String = "$name - $price"
}

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
package com.legion.lpaper.data.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.legion.lpaper.R
import com.legion.lpaper.helpers.utils.LPaperKonfigs
import jahirfiquitiva.libs.kext.extensions.getAppName
import jahirfiquitiva.libs.kext.extensions.hasContent

class LPaperNotificationPublisher(
    private val id: Int,
    private val context: Context?,
    private val mainActivity: Class<*>?,
    private val channel: String,
    private val content: String,
    private val data: Map<String, String>?
                                 ) {
    
    private constructor(bldr: Builder) :
        this(bldr.id, bldr.from, bldr.launch, bldr.channel, bldr.content, bldr.data)
    
    fun post() {
        context?.let {
            val notificationsEnabled = LPaperKonfigs(it).notificationsEnabled
            if (!notificationsEnabled) return
            if (data != null && data.isNotEmpty()) {
                var postedNewWallsNotification = false
                for (i in 0 until data.size) {
                    val dataValue = data.toString().replace("{", "").replace("}", "")
                        .split(",")[i].split("=")
                    val key = dataValue[0]
                    val value = dataValue[1]
                    if (key.equals("new_walls", true)) {
                        postNewWallsNotification(it, value)
                        postedNewWallsNotification = true
                    }
                }
                if (!postedNewWallsNotification && content.hasContent()) {
                    internalPost(it, content)
                }
            } else if (content.hasContent()) {
                internalPost(it, content)
            }
        }
    }
    
    private fun postNewWallsNotification(context: Context, newSize: String) {
        internalPost(context, context.getString(R.string.new_wallpapers_available, newSize))
    }
    
    private fun internalPost(context: Context, content: String) {
        val notificationBuilder = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getAppName())
            .setContentText(content)
            .setAutoCancel(true)
            .setOngoing(false)
            .setColor(ContextCompat.getColor(context, R.color.notification_color))
        
        if (mainActivity != null) {
            val nIntent = Intent(context, mainActivity)
            nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            
            val pendingIntent = PendingIntent.getActivity(
                context, 0, nIntent,
                PendingIntent.FLAG_ONE_SHOT)
            notificationBuilder.setContentIntent(pendingIntent)
        }
        
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        
        notificationBuilder.setSound(ringtoneUri)
        notificationBuilder.setVibrate(longArrayOf(500, 500))
        
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancel(id)
        notificationManager?.notify(id, notificationBuilder.build())
    }
    
    companion object {
        inline fun publish(block: Builder.() -> Unit) = Builder().apply(block).build().post()
    }
    
    class Builder {
        var id: Int = 0
        var from: Context? = null
        var launch: Class<*>? = null
        var channel: String = ""
        var content: String = ""
        var data: Map<String, String>? = null
        fun build() = LPaperNotificationPublisher(this)
    }
}

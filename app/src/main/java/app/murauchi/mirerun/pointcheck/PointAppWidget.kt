package app.murauchi.mirerun.pointcheck

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import io.realm.Realm

class PointAppWidget: AppWidgetProvider() {
    //val realm: Realm = Realm.getDefaultInstance()
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch Activity
            val pendingIntent: PendingIntent = Intent(context, RecyclerViewActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.app_widget_layout
            ).apply {
                setOnClickPendingIntent(R.id.buttonForList, pendingIntent)
            }

            //update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
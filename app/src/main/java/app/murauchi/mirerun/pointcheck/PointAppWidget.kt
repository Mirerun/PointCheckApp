package app.murauchi.mirerun.pointcheck

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import io.realm.Realm
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PointAppWidget: AppWidgetProvider() {
    val realm: Realm = Realm.getDefaultInstance()
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val pointData = readAll() //sort済をfindFirstで持ってくる->期限が１番近いもの
            val widgetType = pointData?.type
            val widgetAmount = pointData?.amount
            val limitDate = pointData?.limitDate
            //Date型からLocalDate型
            val df = SimpleDateFormat("yyyy-MM-dd")
            val target = LocalDate.parse(df.format(limitDate) , DateTimeFormatter.ofPattern("uuuu-MM-dd"))
            val year = target.year.toString()
            val month = (target.monthValue).toString()
            val day = target.dayOfMonth.toString()
            val widgetLimit = "${year}年${month}月${day}日"
            val pendingIntent: PendingIntent = Intent(context, RecyclerViewActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }
            val views: RemoteViews = RemoteViews(context.packageName, R.layout.app_widget_layout)
                .apply {
                //setTextViewText(R.id.titleText, widgetText),
                setOnClickPendingIntent(R.id.buttonForList, pendingIntent)
            }
            views.setTextViewText(R.id.widgetTypeText, widgetType)
            views.setTextViewText(R.id.widgetAmountText, widgetAmount)
            views.setTextViewText(R.id.widgetLimitText, widgetLimit)

            appWidgetManager.updateAppWidget(appWidgetId,views)
        }
        // Perform this loop procedure for each App Widget that belongs to this provider
        /*appWidgetIds.forEach { appWidgetId ->
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
                //setTextViewText(R.id.titleText, widgetText),
                setOnClickPendingIntent(R.id.buttonForList, pendingIntent)
            }
            //update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }*/
    }
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    fun readAll(): Record? {
        return realm.where(Record::class.java).sort("limit").findFirst()
    }
}
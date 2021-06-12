package app.murauchi.mirerun.pointcheck

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import io.realm.Realm
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val PointCheckApp = "com.mirerun.murauchi.PointCheck"

class PointAppWidget: AppWidgetProvider() {
    val realm: Realm = Realm.getDefaultInstance()
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        //appWidgetManager.updateAppWidget(appWidgetId,views)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (context == null || intent == null) return

        when (intent.action) {
            PointCheckApp -> {
                val pendingIntent: PendingIntent = Intent(context, RecyclerViewActivity::class.java)
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }
                val views: RemoteViews =
                    RemoteViews(context.packageName, R.layout.app_widget_layout)
                views.setOnClickPendingIntent(R.id.buttonForList, pendingIntent)

                // ウィジェットを更新
                val myWidget = ComponentName(context, PointAppWidget::class.java)
                val manager = AppWidgetManager.getInstance(context)
                manager.updateAppWidget(myWidget, views)
            }
        }
    }

    //onUpdateのappWidgetId毎の処理はこちらで実装する
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        // データ読み出し
        val pointData = readLately() //sort済をfindFirstで持ってくる->期限が１番近いもの
        val widgetType = pointData?.type
        val widgetAmount = pointData?.amount
        val limitDate = pointData?.limitDate
        //Date型からLocalDate型
        val df = SimpleDateFormat("yyyy-MM-dd")
        val target =
            LocalDate.parse(df.format(limitDate), DateTimeFormatter.ofPattern("uuuu-MM-dd"))
        val year = target.year.toString()
        val month = (target.monthValue).toString()
        val day = target.dayOfMonth.toString()
        val widgetLimit = "${year}年${month}月${day}日"

        // RemoteViews オブジェクトを作成
        val pendingIntent: PendingIntent = Intent(context, RecyclerViewActivity::class.java)
            .let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
        val views: RemoteViews = RemoteViews(context.packageName, R.layout.app_widget_layout)
        views.setTextViewText(R.id.widgetTypeText, widgetType)
        views.setTextViewText(R.id.widgetAmountText, widgetAmount)
        views.setTextViewText(R.id.widgetLimitText, widgetLimit)
        views.setOnClickPendingIntent(R.id.buttonForList, pendingIntent)

        //Button押下通知用のPendingIntentを作成しに登録
        val countIntent =
            Intent(context, PointAppWidget::class.java).apply { action = PointCheckApp }
        val countPendingIntent =
            PendingIntent.getBroadcast(context, 0, countIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.buttonForList, pendingIntent)

        // ウィジェットを更新
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun readLately(): Record? {
        return realm.where(Record::class.java).sort("limit").findFirst()
    }
}

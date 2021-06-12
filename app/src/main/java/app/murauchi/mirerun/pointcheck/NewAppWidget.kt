package app.murauchi.mirerun.pointcheck

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import io.realm.Realm
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

//private const val PointCheckApp = "com.mirerun.murauchi.PointCheck"

//更新されない

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    val realm: Realm = Realm.getDefaultInstance()
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    //val widgetText = context.getString(R.string.appwidget_text)
    val pointData = realm.where(Record::class.java).sort("limit").findFirst()
    /*val record: List<Record> = readAll()
    val dayList: MutableList<Int> = mutableListOf()
    val today = LocalDate.now()
    for (i in record.indices) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val target = LocalDate.parse(sdf.format(record[i].limitDate), dtf)
        val diff = ChronoUnit.DAYS.between(today, target).toInt()
        dayList.add(diff)
    }
    val minDiff = dayList.indexOf(dayList.min()) //最小値は何番目？Int型
    val pointMinData = record[minDiff] //期限が現在に一番近いデータを取り出す */
    val widgetTypeText = pointData?.type
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

    // Construct the RemoteViews object
    val pendingIntent: PendingIntent = Intent(context, RecyclerViewActivity::class.java)
        .let { intent ->
            PendingIntent.getActivity(context, 0, intent, 0)
        }
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.widgetTypeText, widgetTypeText)
    views.setTextViewText(R.id.widgetAmountText, widgetAmount)
    views.setTextViewText(R.id.widgetLimitText, widgetLimit)
    views.setOnClickPendingIntent(R.id.buttonForList, pendingIntent)

    // Instruct the widget manager to update the widget
    /*val myWidget = ComponentName(context, PointAppWidget::class.java)
    val manager = AppWidgetManager.getInstance(context)
    manager.updateAppWidget(myWidget, views)*/
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
    fun readAll(): RealmResults<Record> {
        return realm.where(Record::class.java).findAll().sort("limit")
    }
}
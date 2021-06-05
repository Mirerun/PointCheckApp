package app.murauchi.mirerun.pointcheck

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_first_screen.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.time.LocalDate as LocalDate1

class FirstScreen : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        //お試しデータ削除
        /*val pointList = readAll()
        realm.executeTransaction{
            pointList.deleteAllFromRealm()
        }*/

        /*pointList.forEach{
            Log.d("debug", it.limitDate.toString())
        }*/

        //アプリを開いたときに通知を出す
        val record: List<Record>? = readAll()
        val today = LocalDate1.now() //現在の日付を取得

        @TargetApi(Build.VERSION_CODES.O)
        if (record != null) {
            for (i in record.indices) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
                val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val target = LocalDate1.parse(sdf.format(record[i].limitDate), dtf) //Date型のlimitDateをLocalDateに変換
                if ((target.minusDays(7)).isBefore(today)) {// 期限の1週間前が今日より前だったら
                    val notification = NotificationCompat.Builder(this, "default")
                        .setSmallIcon(R.drawable.icon_v1)
                        .setContentTitle(record[i].type)
                        .setContentText("${record[i].amount}の失効期限が迫っています！")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //channelを作成
                        val name = "title"
                        val descriptionText = "text"
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel("default", name, importance).apply {
                            description = descriptionText
                        }
                        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.createNotificationChannel(channel)
                    }

                    with(NotificationManagerCompat.from(this)) {
                            // notificationId is a unique int for each notification that you must define
                        notify(1, notification)
                    }

                }
            }
        }


        saveScreenButton.setOnClickListener{
            val toMainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(toMainActivityIntent)
        }

        listScreenButton.setOnClickListener {
            val toRecyclerViewActivityIntent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(toRecyclerViewActivityIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    /*@TargetApi(Build.VERSION_CODES.O)
    fun today(): String {
        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return current.format(formatter) //ここで初めて年月日
    }*/

    fun readAll(): RealmResults<Record> {
        return realm.where(Record::class.java).findAll()
    }
}
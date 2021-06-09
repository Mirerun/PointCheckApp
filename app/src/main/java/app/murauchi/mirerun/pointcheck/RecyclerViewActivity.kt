package app.murauchi.mirerun.pointcheck

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RecyclerViewActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val record: List<Record> = readAll()
        val pointList = readAll()
        //データ削除
        /*realm.executeTransaction{
            pointList.deleteAllFromRealm()
        }*/

        //ダイアログ表示
        if (record.isEmpty()) {
            val customTitle = getLayoutInflater().inflate(R.layout.customtitle, null, false)
            AlertDialog.Builder(this)
                .setCustomTitle(customTitle)
                .setMessage("ポイントの有効期限を追加しましょう！\n 期限が1週間をきると通知されます。")
                .setPositiveButton("追加"){ dialog, which ->
                    val toMainActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(toMainActivityIntent)
                }
                .show()
        }


        //アプリを開いたときに通知を出す
        val today = LocalDate.now() //現在の日付を取得

        @TargetApi(Build.VERSION_CODES.O)

        for (i in record.indices) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
            val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val target = LocalDate.parse(sdf.format(record[i].limitDate), dtf) //Date型のlimitDateをLocalDateに変換
                //Log.d("daydebug", target.minusDays(7).toString())
            if ((target.minusDays(7)).isBefore(today)) {// 期限の1週間前が今日より前だったら
                val notification = NotificationCompat.Builder(this, "default")
                        .setSmallIcon(R.drawable.notice_icon)
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
                        notify(record[i].id.hashCode(), notification)
                    }

            }
        }

        val adapter = RecyclerViewAdapter(this, pointList,true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val toMainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(toMainActivityIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    /*fun create(type: String, amount: String, limit: Int) {
        realm.executeTransaction {
            val record = it.createObject(Record::class.java)
            record.type = type
            record.amount = amount
            record.limit = limit
        }
    }*/

    fun readAll(): RealmResults<Record> {
        return realm.where(Record::class.java).findAll().sort("limit")
    }


    fun read(): Record? {
        return realm.where(Record::class.java).findFirst()
    }

}
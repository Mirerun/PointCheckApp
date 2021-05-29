package app.murauchi.mirerun.pointcheck

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecyclerViewActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val pointList = readAll()

        val adapter = RecyclerViewAdapter(this, pointList,true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val toMainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(toMainActivityIntent)
        }
        //adapter.addAll()
    }

    fun create(type: String, amount: String, limit: Int) {
        realm.executeTransaction {
            val record = it.createObject(Record::class.java)
            record.type = type
            record.amount = amount
            record.limit = limit
            /*if (record.limit < (today().toInt() - 7)) {
                val notification = NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.drawable.icon_v2)
                    .setContentTitle(record.type)
                    .setContentText("${record.amount}の失効期限が迫っています！")
                    .build()
                //notificationManager.notify(SystemClock.uptimeMillis().toInt(), notification)

                val manager = NotificationManagerCompat.from(this)
                manager.notify(1, notification)
            }*/
        }
    }

    fun readAll(): RealmResults<Record> {
        return realm.where(Record::class.java).findAll().sort("limit")
    }

    /*@TargetApi(Build.VERSION_CODES.O)
    fun push() {
        val record: Record? = read() //一番上findfirstはおそらく一番早く期限が切れるもの、毎日アプリを開くとして(自動機能削除はないけど)
        if (record != null) {
            if (record.limit < (today().toInt() - 7)) { //1週間前だったら
                val notification = NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.drawable.icon_v2)
                    .setContentTitle(record.type)
                    .setContentText("${record.amount}の失効期限が迫っています！")
                    .build()
                //notificationManager.notify(SystemClock.uptimeMillis().toInt(), notification)

                val manager = NotificationManagerCompat.from(this)
                manager.notify(1, notification)
            }
        }
    }*/

    /*@TargetApi(Build.VERSION_CODES.O)
    fun today(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        Log.d("today", current.format(formatter).toString())
        return current.format(formatter) //ここで初めて年月日
    }*/

    fun read(): Record? {
        return realm.where(Record::class.java).findFirst()
    }

}
package app.murauchi.mirerun.pointcheck

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //このスペースは画面が作られたときに1回だけ実行

        //realmのお試しデータ消す
        /*val pointList = readAll()
        realm.executeTransaction{
            pointList.deleteAllFromRealm()
        }*/

        //通知を出す
        val record: List<Record>? = readAll()

        @TargetApi(Build.VERSION_CODES.O)
        if (record != null) {
            for (i in record.indices) {
                if (record[i].limit < (today().toInt() - 7)) {//1週間前だったら
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

        //Integer.parseInt(yearEditText.text.toString()) 数値型に変える（文字列型)
        saveButton.setOnClickListener {
            val type: String = typeEditText.text.toString()
            val amount: String = amountEditText.text.toString()
            val limit: Int =
                yearEditText.text.toString().toInt() * 10000 + monthEditText.text.toString()
                    .toInt() * 100 + dayEditText.text.toString().toInt()
            save(type, amount, limit)

            val toRecyclerViewActivityIntent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(toRecyclerViewActivityIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun readAll(): RealmResults<Record> {
        return realm.where(Record::class.java).findAll()
    }

    fun read(): Record? {
        return realm.where(Record::class.java).findFirst()
    }

    fun save(type: String, amount: String, limit: Int) {
        //保存する処理
        val record: Record? = read()
        realm.executeTransaction {
            //データの作成
            val newData: Record = it.createObject(Record::class.java)
            newData.type = type
            newData.amount = amount
            newData.limit = limit
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun today(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        Log.d("today", current.format(formatter).toString())
        return current.format(formatter) //ここで初めて年月日
    }
}
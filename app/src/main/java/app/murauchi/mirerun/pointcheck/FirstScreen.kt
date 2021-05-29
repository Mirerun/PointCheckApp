package app.murauchi.mirerun.pointcheck

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_first_screen.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FirstScreen : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        //アプリを開いたときに通知を出す
        val record: List<Record>? = readAll()

        @TargetApi(Build.VERSION_CODES.O)
        if (record != null) {
            for (i in record.indices) {
                //memo: record[i].limit < (today().toInt() - 7
                if (ChronoUnit.DAYS.between(record[i].limitDate, LocalDate.now()) >= 7) {//1週間差があったら
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
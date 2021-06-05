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
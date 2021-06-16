package app.murauchi.mirerun.pointcheck

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //このスペースは画面が作られたときに1回だけ実行

        //realmのお試しデータ消す
        /*val pointList = readAll()
        realm.executeTransaction{
            pointList.deleteAllFromRealm()
        }*/

        daySelectButton.setOnClickListener {
            showDatePicker()
        }

        //Integer.parseInt(yearEditText.text.toString()) 数値型に変える（文字列型)
        saveButton.setOnClickListener {
            val type: String = typeEditText.text.toString()
            val amount: String = amountEditText.text.toString()
            val year:String = yearText.text.toString()
            val month:String = monthText.text.toString()
            val day:String = dayText.text.toString()
            if (type.length == 0){
                Toast.makeText(this, "ポイントを入力してください", Toast.LENGTH_LONG).show();return@setOnClickListener
            }
            if (amount.length == 0){
                Toast.makeText(this, "ポイント量を入力してください", Toast.LENGTH_LONG).show();return@setOnClickListener
            }
            if (month.length == 0){
                Toast.makeText(this, "期限を入力してください", Toast.LENGTH_LONG).show();return@setOnClickListener
            }
            val limit: Int =
                year.toInt() * 10000 + month.toInt() * 100 + day.toInt()
            val newmonth = month.toInt() -1
            val limitDate : Date = GregorianCalendar(year.toInt(), newmonth, day.toInt()).time

            save(type, amount, limit, limitDate)

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

    fun save(type: String, amount: String, limit: Int,  limitDate: Date) {
        //保存する処理
        //val record: Record? = read()
        realm.executeTransaction {
            //データの作成
            val newData: Record = it.createObject(Record::class.java)
            newData.type = type
            newData.amount = amount
            newData.limit = limit
            newData.limitDate = limitDate
        }
    }

    private fun showDatePicker() {
        val today = LocalDate.now() //現在日付をLocalDate型で取得
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth->
                yearText.text = year.toString()
                monthText.text = (month + 1).toString()
                dayText.text = dayOfMonth.toString()
            },
                today.format(DateTimeFormatter.ofPattern("yyyy")).toInt(), //LocalDate型->String型->Int型、パターンで年を指定
                today.format(DateTimeFormatter.ofPattern("MM")).toInt() -1,
                today.format(DateTimeFormatter.ofPattern("dd")).toInt())
        datePickerDialog.show()
    }
}


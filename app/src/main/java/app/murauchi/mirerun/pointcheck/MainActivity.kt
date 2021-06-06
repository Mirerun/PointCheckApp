package app.murauchi.mirerun.pointcheck

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
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

        /*daySelectButton.setOnClickListener {
            showDatePicker()
        }*/

        //Integer.parseInt(yearEditText.text.toString()) 数値型に変える（文字列型)
        saveButton.setOnClickListener {
            val type: String = typeEditText.text.toString()
            val amount: String = amountEditText.text.toString()
            val limit: Int =
                yearEditText.text.toString().toInt() * 10000 + monthEditText.text.toString()
                    .toInt() * 100 + dayEditText.text.toString().toInt()

            //val date = LocalDate.parse("${yearEditText.text}${month}${day}")//yyyyMMdd型になった
            //val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            //date.format(formatter)
            //val limitDate : Date = stringToDate("${yearEditText.text}${month}${day}")
            //val limitDate : Date = SimpleDateFormat("yyyyMMdd").parse("${yearEditText.text}${month}${day}")
            val newmonth = monthEditText.text.toString().toInt() -1
            val limitDate : Date = GregorianCalendar(yearEditText.text.toString().toInt(), newmonth, dayEditText.text.toString().toInt()).time
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

    /*fun stringToDate(pattern: String = "yyyyMMdd"): Date {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null
        }
        val date = sdFormat.let {
            try {
                it.parse(this.toString())
            } catch (e: ParseException){
                null
            }
        }
        return date
    }*/

    /*private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth->
                dayText.text = "${year}年${month + 1}月${dayOfMonth}日まで"
            },
            2021,
            5,
            1)
        datePickerDialog.show()
    }*/
}
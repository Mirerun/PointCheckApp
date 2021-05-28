package app.murauchi.mirerun.pointcheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //realmのデータ消す
        //val pointList = readAll()
        //realm.executeTransaction{
        //    pointList.deleteAllFromRealm()
        //}

        //このスペースは画面が作られたときに1回だけ実行
        //Integer.parseInt(yearEditText.text.toString()) 数値型に変える（文字列型)
        saveButton.setOnClickListener {
            val type: String = typeEditText.text.toString()
            val amount : String = amountEditText.text.toString()
            val limit : Int = yearEditText.text.toString().toInt() * 10000 + monthEditText.text.toString().toInt() * 100 + dayEditText.text.toString().toInt()
            save(type,amount,limit)

            val toRecyclerViewActivityIntent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(toRecyclerViewActivityIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun readAll(): RealmResults<Record> { //試しに書いたデータを消す用
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
}
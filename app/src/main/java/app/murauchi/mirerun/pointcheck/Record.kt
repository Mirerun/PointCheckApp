package app.murauchi.mirerun.pointcheck

import io.realm.RealmObject
import java.util.*

open class Record (
        @Primarykey open var id : String = UUID.randomUUID().toString(), //IDを付けるなら必要
        open var type: String = "",
        open var amount: String = "",
        open var limit: Int = 0
) : RealmObject() {
        annotation class Primarykey
}


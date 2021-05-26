package app.murauchi.mirerun.pointcheck

import io.realm.RealmObject

open class Record (
        open var type: String = "",
        open var amount: String = "",
        open var limit: Int = 0
) : RealmObject()


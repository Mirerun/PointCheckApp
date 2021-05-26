package app.murauchi.mirerun.pointcheck

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class PointCheckApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val realmConfig : RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)



    }
}
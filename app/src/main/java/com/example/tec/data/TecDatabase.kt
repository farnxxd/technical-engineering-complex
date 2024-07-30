package com.example.tec.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tec.data.interest.Interest
import com.example.tec.data.interest.InterestDao
import com.example.tec.data.member.Member
import com.example.tec.data.member.MemberDao

/**
 * A database which includes two tables, one for [Member]s and another for [Interest]s.
 * A ready-to-use database named tec_database is provided with information about [Member]s and
 * [Interest]s.
 *      Attention: there is no option provided for migration. so I recommend take backups before
 *          applying any change. Also it'll be better if any migration methods are provided.
 */
@Database(entities = [Member::class, Interest::class], version = 1, exportSchema = false)
abstract class TecDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun interestDao() : InterestDao

    companion object{
        @Volatile
        private var instance: TecDatabase? = null

        fun getDatabase(context: Context): TecDatabase {
            // If the instance is not null, return it, otherwise create a new instance of database.
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, TecDatabase::class.java, "tec_database")
                    .createFromAsset("database/tec_database.db")
                    .build()
                    .also { instance = it }
            }
        }
    }
}
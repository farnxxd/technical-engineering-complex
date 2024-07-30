package com.example.tec.data.interest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestDao {

    /**
     * When user selects the desired field in GroupScreen, [getInterests] is called in order to list
     * all the [Interest]s stored in database. The reason behind two field is because electrical and
     * mechanical groups share the same screen.
     */
    @Query("SELECT * FROM interests " +
            "WHERE (field = :fieldOne OR field = :fieldTwo) " +
            "COLLATE LOCALIZED")
    fun getInterests(fieldOne: String, fieldTwo: String): Flow<List<Interest>>

    /**
     * The stages are list as buttons in GroupScreen. So for the buttons' name, [getEducationalStages]
     * is called because Civil group has different number of stages and the returned list is not
     * static.
     */
    @Query("SELECT DISTINCT stage FROM interests")
    fun getEducationalStages(): Flow<List<String>>

    /**
     * In HomeScreen, there is a card for statistics and all [Interest]s number is needed. so
     * [getInterestsNumber] gets called.
     */
    @Query("SELECT COUNT(*) FROM interests")
    fun getInterestsNumber(): Flow<Int>

    /**
     * This function was used for adding [Interest]s in database.
     */
    @Insert
    suspend fun addInterest(interest: Interest)
}
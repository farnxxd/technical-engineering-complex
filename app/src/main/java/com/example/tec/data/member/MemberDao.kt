package com.example.tec.data.member

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    /**
     * When user selects the desired field in GroupScreen, [getDeanOfFaculty] is called in order to
     * show dean of the field. The reason behind two field provided as parameter is electrical and
     * mechanical groups share the same screen.
     */
    @Query("SELECT * FROM members " +
            "WHERE (field = :fieldOne OR field = :fieldTwo) " +
            "AND dean_of_faculty = 1")
    fun getDeanOfFaculty(fieldOne: String, fieldTwo: String): Flow<Member?>

    /**
     * When user selects the desired field in GroupScreen, [getAcademics] is called in order to
     * show all the academics assigned to the field. The reason behind two field provided as parameter is electrical and
     *      * mechanical groups share the same screen.
     */
    @Query("SELECT * FROM members " +
            "WHERE (field = :fieldOne OR field = :fieldTwo) " +
            "AND dean_of_faculty = 0 " +
            "ORDER BY last_name " +
            "COLLATE LOCALIZED")
    fun getAcademics(fieldOne: String, fieldTwo: String): Flow<List<Member>>

    /**
     * When user selects the desired field in GroupScreen, [getAuthorities] is called in order to
     * show all the authorities assigned to the field. The reason behind two field provided as parameter is electrical and
     *      * mechanical groups share the same screen.
     */
    @Query("SELECT * FROM members " +
            "WHERE (field = :fieldOne OR field = :fieldTwo) " +
            "AND dean_of_faculty IS null " +
            "ORDER BY last_name " +
            "COLLATE LOCALIZED")
    fun getAuthorities(fieldOne: String, fieldTwo: String): Flow<List<Member>>

    /**
     * This function gets the dean of whole complex. The result will be shown in HomeScreen in
     * third card.
     */
    @Query("SELECT * FROM members " +
            "WHERE interest IS NOT NULL AND dean_of_faculty = 0")
    fun getDeanOfComplex(): Flow<Member?>

    /**
     * This function returns whole academics number. The result will be shown in HomeScreen in
     * second card.
     */
    @Query("SELECT COUNT(*) FROM  members " +
            "WHERE dean_of_faculty IS NOT NULL")
    fun getAcademicsNumber(): Flow<Int>

    /**
     * This function returns whole authorities number. The result will be shown in HomeScreen in
     * second card.
     */
    @Query("SELECT COUNT(*) FROM members " +
            "WHERE dean_of_faculty IS NULL")
    fun getAuthoritiesNumber(): Flow<Int>

    /**
     * This function was used for adding [Member]s in database.
     */
    @Insert
    suspend fun addMember(member: Member)

    /**
     * This function is useful when [Member]'s information needs to be updated.
     */
    @Update
    suspend fun updateMember(member: Member)
}
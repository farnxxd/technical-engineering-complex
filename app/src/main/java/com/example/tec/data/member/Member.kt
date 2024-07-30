package com.example.tec.data.member

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This data class is defined for any [Member]s, so there wouldn't be any necessity to declare
 * separate member like dean, academic and authority. But there are attributes defined in this class,
 * in order to make a difference between them.
 */
@Entity(tableName = "members")
@Immutable
data class Member(

    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,

    /**
     * [field] A field that the member has studied and is assigned to the place.
     */
    val field: String? = null,
    /**
     * [interest] If the member is a dean of building's faculty or dean of the field, it'll be filled
     * with value. And if the member is an academic or an authority, it'll be null.
     */
    val interest: String? = null,
    /**
     * [title] This is rank of the member. If it's null, then member is an authority for sure.
     */
    val title: String? = null,
    /**
     * [contactInfo] Either contains email of academics or phone number of interior department.
     */
    val contactInfo: String,
    /**
     * [deanOfFaculty] Indicates if member is a dean or not.
     */
    @ColumnInfo(name = "dean_of_faculty") val deanOfFaculty: Boolean? = null,
    /**
     * [imageBytes] An array of bytes which the encoded picture is stored in database, and will be
     * restored as [ByteArray] or null. Then a function for conversion will be called.
     */
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val imageBytes: ByteArray? = null
) {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        return id == other.id
    }
}
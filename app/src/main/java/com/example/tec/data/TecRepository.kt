package com.example.tec.data

import android.database.Cursor
import com.example.tec.data.interest.Interest
import com.example.tec.data.interest.InterestDao
import com.example.tec.data.member.Member
import com.example.tec.data.member.MemberDao
import kotlinx.coroutines.flow.Flow


class TecRepository(
    private val memberDao: MemberDao,
    private val interestDao: InterestDao
) {
    /**
     * Methods used in GroupViewModel.
     */
    fun getDeanOfFaculty(fieldOne: String, fieldTwo: String = ""): Flow<Member?> =
        memberDao.getDeanOfFaculty(fieldOne, fieldTwo)

    fun getAcademics(fieldOne: String, fieldTwo: String = ""): Flow<List<Member>> =
        memberDao.getAcademics(fieldOne, fieldTwo)

    fun getAuthorities(fieldOne: String, fieldTwo: String = ""): Flow<List<Member>> =
        memberDao.getAuthorities(fieldOne, fieldTwo)

    fun getInterests(fieldOne: String, fieldTwo: String = ""): Flow<List<Interest>> =
        interestDao.getInterests(fieldOne, fieldTwo)

    /**
     * Methods used in HomeViewModel.
     */
    fun getDeanOfComplex(): Flow<Member?> = memberDao.getDeanOfComplex()

    fun getAcademicsNumber(): Flow<Int> = memberDao.getAcademicsNumber()

    fun getAuthoritiesNumber(): Flow<Int> = memberDao.getAuthoritiesNumber()

    fun getInterestsNumber(): Flow<Int> = interestDao.getInterestsNumber()

    /**
     * Methods used for updating.
     */
    suspend fun addMember(member: Member) = memberDao.addMember(member)

    suspend fun updateMember(member: Member) = memberDao.updateMember(member)

    suspend fun addInterest(interest: Interest) = interestDao.addInterest(interest)
}
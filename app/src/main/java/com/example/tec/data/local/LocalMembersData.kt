package com.example.tec.data.local

import com.example.tec.R
import com.example.tec.data.member.Member

object LocalMembersData {

    /**
     * Empty [Member] is needed for null safety. Because sometimes the return value from database
     * is null.
     */
    val emptyMember = Member(
        id = 0,
        firstName = "",
        lastName = "",
        contactInfo = ""
    )

    /**
     * Former deans are listed in second card.
     */
    val formerDeans = listOf(
        Member(
            id = 401,
            firstName = "حسین",
            lastName = "معز",
            title = "دوره اول",
            contactInfo = ""
        ),
        Member(
            id = 402,
            firstName = "سید علی",
            lastName = "صدیق ضیابری",
            title = "دوره دوم",
            contactInfo = ""
        ),
        Member(
            id = 401,
            firstName = "فرزانه",
            lastName = "اسدی ملک جهان",
            title = "دوره سوم",
            contactInfo = ""
        )
    )
}
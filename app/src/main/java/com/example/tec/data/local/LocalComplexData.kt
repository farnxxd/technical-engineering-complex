package com.example.tec.data.local

import com.example.tec.R

object LocalComplexData {

    /**
     * First card in HomeScreen.
     */
    val complexHistory = Pair(
        R.string.complex_history,
        R.string.complex_history_description
    )

    /**
     * Second card uses database information.
     */

    /**
     * Third card in HomeScreen.
     */
    val inAGlimpseTitles = listOf(
        R.string.academics,
        R.string.students,
        R.string.employees,
        R.string.educational_groups,
        R.string.fields
    )

    /**
     * Fourth card in HomeScreen.
     */
    val facilitiesList = listOf(
        Pair(R.string.classrooms, 15),
        Pair(R.string.computer_sites, 3),
        Pair(R.string.architectural_atelier, 52),
        Pair(R.string.civil_laboratory, 1),
        Pair(R.string.electrical_laboratory, 2),
        Pair(R.string.mechanical_workshop, 3)
    )
}
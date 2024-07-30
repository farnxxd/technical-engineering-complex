package com.example.tec.data.interest

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * This data class is defined in order to store information about interests which is used to show
 * in GroupScreen divided by stages.
 */
@Entity(tableName = "interests")
data class Interest(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val field: String,
    val stage: String
)

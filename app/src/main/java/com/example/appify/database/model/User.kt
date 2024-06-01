package com.example.appify.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey
    val id: Int,
    val name: String
): Parcelable

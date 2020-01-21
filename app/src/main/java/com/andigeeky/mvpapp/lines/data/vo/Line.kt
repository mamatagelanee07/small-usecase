package com.andigeeky.mvpapp.lines.data.vo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Lines", primaryKeys = ["id"])
data class Line(
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("modeName")
    val modeName: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String
)

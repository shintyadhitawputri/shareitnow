package com.dicoding.mysubmissionstory.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("lon")
    val lon: Any? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("lat")
    val lat: Any? = null
)
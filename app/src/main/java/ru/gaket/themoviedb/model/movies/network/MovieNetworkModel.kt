package ru.gaket.themoviedb.model.movies.network

import com.google.gson.annotations.SerializedName

data class MovieNetworkModel(

        @SerializedName("poster_path")
        val posterPath: String,

        @SerializedName("id")
        val id: Int,

        @SerializedName("title")
        val title: String
)
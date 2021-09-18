package ru.gaket.themoviedb.data.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gaket.themoviedb.di.BaseUrlQualifier
import ru.gaket.themoviedb.data.movies.network.MoviesApi
import javax.inject.Inject

interface MoviesDbClient {
	fun moviesApi(): MoviesApi
}

class MoviesDbClientImpl @Inject constructor(
	@BaseUrlQualifier private val baseUrl: String
) : MoviesDbClient {
	
	// If you decide to play with this app more than a few times,
	// please, create your own api key, so this one does not get banned:
	// https://www.themoviedb.org/documentation/api
	private val apiKey = "c058d9a291e7f1dd69f97f1afac69b61"
	
	private val client = OkHttpClient.Builder()
		.addInterceptor(QueryInterceptorI(hashMapOf("api_key" to apiKey)))
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.build()
	
	private val retrofit = Retrofit.Builder()
		.client(client)
		.baseUrl(baseUrl)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	
	private val moviesApi = retrofit.create(MoviesApi::class.java)
	
	override fun moviesApi(): MoviesApi = moviesApi
}
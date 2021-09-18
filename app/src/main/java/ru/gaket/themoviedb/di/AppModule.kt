package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.core.navigation.WebNavigator
import ru.gaket.themoviedb.core.navigation.WebNavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
	
	@Binds
	abstract fun bindWebNavigator(
		impl: WebNavigatorImpl
	): WebNavigator
}
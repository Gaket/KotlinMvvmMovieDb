package ru.gaket.themoviedb.core.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface WebNavigator {
	fun navigateTo(url: String): Boolean
}

/**
 * Class responsible for the navigation
 */
class WebNavigatorImpl @Inject constructor(
	@ApplicationContext private val context: Context
) : WebNavigator {

	/**
	 * Open a browser for a given url
	 *
	 * @return [true] if navigation succeded, [false] otherwise
	 */
	override fun navigateTo(url: String): Boolean {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		browserIntent.flags = browserIntent.flags or Intent.FLAG_ACTIVITY_NEW_TASK
		return try {
			context.startActivity(browserIntent)
			true
		} catch (e: ActivityNotFoundException) {
			false
		}
	}
}
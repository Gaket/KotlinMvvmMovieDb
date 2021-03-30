package ru.gaket.themoviedb

object FeatureToggles {

  const val MaintenanceModeText = "MaintenanceModeText"
  const val SearchSupported = "SearchSupported"
  const val SearchHint = "SearchHint"

  val defaults = mapOf(
    Pair(SearchSupported, true),
    Pair(SearchHint, "Start typing in a search bar to find a movie"),
    Pair(MaintenanceModeText, "Sorry, we are taking some time off"),
  )
}

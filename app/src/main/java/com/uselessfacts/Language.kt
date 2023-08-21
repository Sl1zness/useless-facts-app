package com.uselessfacts

sealed class Language(val id: String, val name: String) {
    object English : Language("en", "English")
    object Deutsch : Language("de", "Deutsch")
}

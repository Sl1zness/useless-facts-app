package com.uselessfacts.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uselessfacts.Language
import com.uselessfacts.R
import com.uselessfacts.model.UselessFactsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

const val TAG = "ERROR_WHILE_GETTING_RESPONSE"

@HiltViewModel
class UselessFactsViewModel @Inject constructor(val retrofitInstance: UselessFactsApi) : ViewModel() {
    var isDropdownOpened by mutableStateOf(false)
        private set

    var dropdownHeader by mutableStateOf(Language.English.name)
        private set

    var currentUselessFact by mutableStateOf("")
        private set

    var currentLanguageId by mutableStateOf(Language.English.id)
        private set

    var currentFactSource by mutableStateOf("")
        private set

    var isButtonEnabled by mutableStateOf(true)
        private set

    init {
        getDailyFact()
    }

    fun changeDropdownState(state: Boolean? = null) {
        isDropdownOpened = state ?: !isDropdownOpened
    }

    fun getNextFact() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                retrofitInstance.getUselessFact(currentLanguageId)
            } catch (e: IOException) {
                Log.e(TAG, "IOException. Common reason: no internet connection")
                currentUselessFact = "Oops. Something gone wrong. Check your internet connection"
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HTTPException. Common reason: unexpected response")
                currentUselessFact = "Oops. Something gone wrong. Server responded in unexpected way"
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                currentUselessFact = response.body()!!.text
                currentFactSource = "Source: " + response.body()!!.source
            }
            else Log.e(TAG, "Response not successful")
        }
    }

    fun getDailyFact() {
        viewModelScope.launch(Dispatchers.Main) {
            val response = try {
                retrofitInstance.getFactOfTheDay(currentLanguageId)
            } catch (e: IOException) {
                Log.e(TAG, "IOException. Common reason: no internet connection")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HTTPException. Common reason: unexpected response")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                currentUselessFact = "Today's fact: " + response.body()!!.text
                currentFactSource = "Source: " + response.body()!!.source
            }
            else Log.e(TAG, "Response not successful")
        }
    }

    fun changeCurrentLanguageId(langId: String) {
        currentLanguageId = langId
    }

    fun changeDropdownHeader(header: String) {
        dropdownHeader = header
    }

    fun enableButton(isEnabled: Boolean) {
        isButtonEnabled = isEnabled
    }
}
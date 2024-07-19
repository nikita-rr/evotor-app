/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.network.EvotorApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface EvotorUiState {
    data class Success(val message: String) : EvotorUiState
    data class Error(val message: String) : EvotorUiState
    object Loading : EvotorUiState
    object Initial : EvotorUiState
}

class EvotorViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var evotorUiState: EvotorUiState by mutableStateOf(EvotorUiState.Initial)
        private set

    init {

    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun makeRequest() {
        viewModelScope.launch {
            evotorUiState = EvotorUiState.Loading
            evotorUiState = try {
                val listResult = EvotorApi.retrofitService.getReceipts()
                EvotorUiState.Success(
                    "Success: ${listResult.toString()}"
                )
            } catch (e: IOException) {
                EvotorUiState.Error(message = e.message.toString())
            } catch (e: HttpException) {
                EvotorUiState.Error(message = e.message.toString())
            }
        }
    }
}

package com.example.tec.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tec.TecApplication
import com.example.tec.ui.scaffold.groups.GroupViewModel
import com.example.tec.ui.scaffold.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire TEC app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(tecApplication().container.tecRepository)
        }

        // Initializer for GroupViewModel
        initializer {
            GroupViewModel(tecApplication().container.tecRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [TecApplication].
 */
fun CreationExtras.tecApplication(): TecApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TecApplication)
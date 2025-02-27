package com.govi.todoapp

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Govi on 27,February,2025
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    val navHostController: NavHostController
) : ViewModel()
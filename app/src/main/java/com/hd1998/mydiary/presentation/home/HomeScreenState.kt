package com.hd1998.mydiary.presentation.home

import androidx.compose.foundation.ScrollState
import com.hd1998.mydiary.domain.model.Dairy

data class HomeScreenState(
    val list : List<Dairy> = emptyList() ,
    val loading: Boolean = false,
    val error: String = ""
    )
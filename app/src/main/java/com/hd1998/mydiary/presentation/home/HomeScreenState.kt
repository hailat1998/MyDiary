package com.hd1998.mydiary.presentation.home

import com.hd1998.mydiary.domain.model.Diary

data class HomeScreenState(
    val list : List<Diary> = emptyList(),
    val loading: Boolean = false,
    val error: String = ""
    )
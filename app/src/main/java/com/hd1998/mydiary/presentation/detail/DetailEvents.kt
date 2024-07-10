package com.hd1998.mydiary.presentation.detail

sealed class DetailEvents {
    data class Save(val s: String):DetailEvents()
    data class Delete(val s: String):DetailEvents()
}
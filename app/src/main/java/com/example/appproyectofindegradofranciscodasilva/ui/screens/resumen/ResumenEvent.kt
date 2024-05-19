package com.example.appproyectofindegradofranciscodasilva.ui.screens.resumen

sealed class ResumenEvent {
    class OnTrimesterSelected(val quarter: String) : ResumenEvent()
    class OnYearSelected(val year: String) : ResumenEvent()
    object OnTrimesterMenuExpandedChanged : ResumenEvent()
    object OnYearMenuExpandedChanged : ResumenEvent()
    object MessageSeen : ResumenEvent()

}
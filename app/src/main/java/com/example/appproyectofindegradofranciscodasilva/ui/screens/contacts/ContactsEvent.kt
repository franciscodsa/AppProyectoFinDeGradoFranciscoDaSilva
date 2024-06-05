package com.example.appproyectofindegradofranciscodasilva.ui.screens.contacts

sealed class ContactsEvent {
    object SetUserRole : ContactsEvent()
    object LoadContacts : ContactsEvent()
    object LoadCurrentUser : ContactsEvent()
    object MessageSeen : ContactsEvent()
}
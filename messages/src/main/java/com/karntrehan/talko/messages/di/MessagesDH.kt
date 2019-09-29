package com.karntrehan.talko.messages.di

import com.karntrehan.talko.TalkoApp

/*Dependency holder for Messages module*/
object MessagesDH {

    lateinit var messagesComponent: MessagesComponent

    fun init(application: TalkoApp) {
        if (!::messagesComponent.isInitialized)
            messagesComponent = DaggerMessagesComponent
                .builder()
                .coreComponent(application.coreComponent)
                .build()
    }

}
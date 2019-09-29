package com.karntrehan.talko.messages.di

import com.karntrehan.talko.dependencies.CoreComponent
import com.karntrehan.talko.messages.landing.MessagesFragment
import dagger.Component


@MessagesScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [MessagesModule::class]
)
interface MessagesComponent {
    fun inject(characterSearchFragment: MessagesFragment)
}
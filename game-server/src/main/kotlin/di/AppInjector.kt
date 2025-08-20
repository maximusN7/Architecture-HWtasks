package org.example.di

object AppInjector {

    val component: AppComponent by lazy { DaggerAppComponent.create() }
}

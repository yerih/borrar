package com.mivuelto.di

import android.content.Context
import com.mivuelto.MainApplication
import com.mivuelto.sdk.DevEngRequesterImpl
import com.mivuelto.sdk.DeviceEngineRequester
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SdkModuleDI{

    @Provides
    @Singleton
    fun provideEngineRequester(
        @ApplicationContext context: Context // Hilt provee esto automáticamente
    ): DeviceEngineRequester {
        val app = context as MainApplication
        return DevEngRequesterImpl(app)
    }
}


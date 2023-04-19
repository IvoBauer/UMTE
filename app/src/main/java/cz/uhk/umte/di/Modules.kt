package cz.uhk.umte.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import cz.uhk.umte.data.datastore.DataStorage
import cz.uhk.umte.data.db.AppDatabase
import cz.uhk.umte.data.remote.ApiConfig
import cz.uhk.umte.data.remote.service.SpaceXAPIService
import cz.uhk.umte.di.repositories.SpaceXRepository
import cz.uhk.umte.ui.async.launches.LaunchesViewModel
import cz.uhk.umte.ui.async.rocket.RocketDetailViewModel
import cz.uhk.umte.ui.datastore.DataStoreViewModel
import cz.uhk.umte.ui.feeds.FeedVM
import cz.uhk.umte.ui.schemes.SchemeVM
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

val appModules by lazy { listOf(dataModule, uiModule) }

val dataModule = module {
    repositories()
    apiServices()
    dataStorage()
    db()
}

val uiModule = module {
    viewModel { LaunchesViewModel(get()) }
    viewModel { (rocketId: String) -> RocketDetailViewModel(rocketId, get()) }
    viewModel { FeedVM(get()) }
    viewModel { SchemeVM(get()) }
    viewModel { DataStoreViewModel(get()) }
}

private fun Module.repositories() {
    single { SpaceXRepository(get()) }
}

private fun Module.apiServices() {
    single { createRetrofit(createClient()) }
    single { get<Retrofit>().create(SpaceXAPIService::class.java) }
}

private fun Module.dataStorage() {
    single { DataStorage(androidApplication().dataStore) }
}

private fun Module.db() {
    // DB
    single {
        Room
            .databaseBuilder(
                context = androidApplication(),
                klass = AppDatabase::class.java,
                name = AppDatabase.Name,
            )
            .build()
    }
    // Dao
    single { get<AppDatabase>().feedDao() }
    single { get<AppDatabase>().schemeDao() }
}

private val json = Json {
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalSerializationApi::class)
private fun createRetrofit(
    client: OkHttpClient,
    baseUrl: String = ApiConfig.BaseApiUrl,
) = Retrofit.Builder().apply {
    client(client)
    baseUrl(baseUrl)
    addConverterFactory(
        json.asConverterFactory(
            "application/json".toMediaType()
        )
    )
}.build()

private fun createClient() = OkHttpClient.Builder().build()

private const val DataStoreName = "UMTEDataStore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DataStoreName)
package cz.uhk.umte.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import cz.uhk.umte.data.db.AppDatabase
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
    db()
}

val uiModule = module {
    viewModel { FeedVM(get()) }
    viewModel { SchemeVM(get()) }
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

private fun createClient() = OkHttpClient.Builder().build()

private const val DataStoreName = "UMTEDataStore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DataStoreName)
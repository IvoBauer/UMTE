package cz.uhk.umte.ui.datastore

import cz.uhk.umte.data.datastore.DataStorage
import cz.uhk.umte.ui.base.BaseViewModel

class DataStoreViewModel(
    private val dataStorage: DataStorage,
) : BaseViewModel() {

    val textFlow = dataStorage.textFlow

    suspend fun isChecked() = dataStorage.isChecked()

    fun save(text: String, checked: Boolean) {
        launch {
            dataStorage.setText(text)
            dataStorage.setChecked(checked)
        }
    }
}
package com.example.util.simpletimetracker.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.util.simpletimetracker.core.R
import com.example.util.simpletimetracker.core.repo.ResourceRepo
import com.example.util.simpletimetracker.domain.interactor.BackupInteractor
import com.example.util.simpletimetracker.domain.resolver.BackupRepo
import com.example.util.simpletimetracker.navigation.Router
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewModel @Inject constructor(
    private val resourceRepo: ResourceRepo,
    private val router: Router,
    private val backupInteractor: BackupInteractor
) : ViewModel() {

    val progressVisibility: LiveData<Boolean> = MutableLiveData(false)

    fun onSaveBackup(uriString: String) = viewModelScope.launch {
        showProgress(true)

        val resultCode = backupInteractor.saveBackupFile(uriString)

        if (resultCode == BackupRepo.ResultCode.SUCCESS) {
            R.string.message_backup_saved
        } else {
            R.string.message_save_error
        }.let(::showMessage)

        showProgress(false)
    }

    fun onRestoreBackup(uriString: String) = viewModelScope.launch {
        showProgress(true)

        val resultCode = backupInteractor.restoreBackupFile(uriString)

        if (resultCode == BackupRepo.ResultCode.SUCCESS) {
            R.string.message_backup_restored
        } else {
            R.string.message_restore_error
        }.let(::showMessage)

        showProgress(false)
    }

    private fun showMessage(stringResId: Int) {
        stringResId.let(resourceRepo::getString).let(router::showSystemMessage)
    }

    private fun showProgress(isVisible: Boolean) {
        (progressVisibility as MutableLiveData).value = isVisible
    }
}
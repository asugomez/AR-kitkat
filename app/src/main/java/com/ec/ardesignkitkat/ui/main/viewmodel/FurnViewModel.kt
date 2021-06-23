package com.ec.ardesignkitkat.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ec.ardesignkitkat.data.FurnitureRepository
import com.ec.ardesignkitkat.data.model.Furniture
import kotlinx.coroutines.launch

class FurnViewModel(application: Application): AndroidViewModel(application) {

    private val furnitureRepository by lazy { FurnitureRepository.newInstance(application) }

    val furnitures = MutableLiveData<FurnViewModel.ViewState>()

    sealed class ViewState {
        object Loading : ViewState()
        data class Content(val furnitures: List<Furniture>) : ViewState()
        data class Error(val message: String) : ViewState()
    }

    fun getUserFurnitures(idUser: Int, hash: String){
        viewModelScope.launch {
            furnitures.value = FurnViewModel.ViewState.Loading
            try {
                furnitures.value = FurnViewModel.ViewState.Content(furnitures = furnitureRepository.getUsersFurnitures(idUser,hash))
            } catch (e: Exception) {
                furnitures.value = FurnViewModel.ViewState.Error(e.message.orEmpty())
            }
        }
    }


}
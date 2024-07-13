package com.example.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.data.model.DetailUserResponse
import com.example.githubuser.data.remote.ApiConfig
import com.example.githubuser.data.model.ItemsItem
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.repository.UserUiState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _uiDetailState = MutableLiveData<UserUiState<DetailUserResponse>>()
    val uiDetailState: LiveData<UserUiState<DetailUserResponse>> = _uiDetailState

    private val _detailUser = MutableLiveData<UserEntity?>()
    val detailUser: LiveData<UserEntity?> = _detailUser

    companion object{
        private const val TAG = "DetailUserViewModel"
        private const val USER_ID = ""
    }

    init{
        getDetailUsername(USER_ID)
    }

    fun getDetailUsername(username: String) {
        _uiDetailState.value = UserUiState.Loading
        viewModelScope.launch {
           try {
               _uiDetailState.value = UserUiState.Success(userRepository.getDetailUser(username))
           } catch (e: Exception){
               _uiDetailState.value = UserUiState.Error(e.message.toString())
           }
        }
    }

    fun addFavorite(){
        val currentState = _uiDetailState.value
        if (currentState is UserUiState.Success) {
            viewModelScope.launch {
                val user = UserEntity(
                        username = currentState.data.login.toString(),
                        avatarUrl = currentState.data.avatarUrl
                )
                userRepository.addFavorite(user)
                getLocalUser(currentState.data.login.toString())
            }
        } else {
            Log.e(TAG, "Cannot add favorite: No user data available or current state is not success")
        }
    }

    fun deleteFavorite(){
        val currentState = _uiDetailState.value
        if (currentState is UserUiState.Success) {
        viewModelScope.launch {
            val user = UserEntity(
                    username = currentState.data.login.toString(),
                    avatarUrl = currentState.data.avatarUrl
                )
                userRepository.deleteFavorite(user)
            getLocalUser(currentState.data.login.toString())
        }
        } else {
            Log.e(TAG, "Cannot delete favorite: No user data available or current state is not success")
        }
    }

    fun getLocalUser(username: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(username)
            _detailUser.postValue(user)
        }
    }
}
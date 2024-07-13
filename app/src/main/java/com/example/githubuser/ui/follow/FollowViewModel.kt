package com.example.githubuser.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.model.ItemsItem
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.repository.UserUiState
import kotlinx.coroutines.launch

class FollowViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _listFollowersState = MutableLiveData<UserUiState<List<ItemsItem>>>()
    val listFollowersState : LiveData<UserUiState<List<ItemsItem>>> = _listFollowersState

    private val _listFollowingState = MutableLiveData<UserUiState<List<ItemsItem>>>()
    val listFollowingState : LiveData<UserUiState<List<ItemsItem>>> = _listFollowingState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getListFollowers(username: String) {
        _isLoading.value = true
        _listFollowersState.value = UserUiState.Loading
        viewModelScope.launch {
            try {
                _listFollowersState.value = UserUiState.Success(userRepository.getFollowers(username))
            } catch (e: Exception){
                _listFollowersState.value = UserUiState.Error(e.message.toString())
            }
        }
    }

    fun getListFollowing(username: String) {
        _isLoading.value = true
        _listFollowingState.value = UserUiState.Loading
        viewModelScope.launch {
            try {
                _listFollowingState.value = UserUiState.Success(userRepository.getFollowing(username))
            } catch (e: Exception){
                _listFollowingState.value = UserUiState.Error(e.message.toString())
            }
        }
    }
}
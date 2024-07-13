package com.example.githubuser.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.di.Injection
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.ui.detail.DetailUserViewModel
import com.example.githubuser.ui.favorite.FavoriteViewModel
import com.example.githubuser.ui.follow.FollowViewModel
import com.example.githubuser.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository):
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{modelClass.isAssignableFrom(MainViewModel::class.java) -> {
            MainViewModel(userRepository) as T
        }
            modelClass.isAssignableFrom(DetailUserViewModel::class.java) -> {
            DetailUserViewModel(userRepository) as T
        }
            modelClass.isAssignableFrom(FollowViewModel::class.java) -> {
            FollowViewModel(userRepository) as T
        }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
            FavoriteViewModel(userRepository) as T
        }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
            SettingViewModel(userRepository) as T
        }
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
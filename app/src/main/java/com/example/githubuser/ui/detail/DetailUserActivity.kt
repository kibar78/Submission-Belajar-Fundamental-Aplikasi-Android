package com.example.githubuser.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.model.DetailUserResponse
import com.example.githubuser.ui.adapter.SectionPagerAdapter
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.repository.UserUiState
import com.example.githubuser.ui.main.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private val detailViewModel by viewModels<DetailUserViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private var favoriteStatus: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel.uiDetailState.observe(this){detailUiState ->
            when(detailUiState){
                is UserUiState.Loading ->{
                    showLoading(true)
                }
                is UserUiState.Success ->{
                    setDetailUser(detailUiState.data)
                    showLoading(false)
                }
                is UserUiState.Error ->{
                    showLoading(false)
                }
            }
        }

        if (intent.getStringExtra(EXTRA_USERNAME) != null){
            val username = intent.getStringExtra(EXTRA_USERNAME)
            binding.fabFavorite.setOnClickListener {
                if (favoriteStatus){
                    detailViewModel.deleteFavorite()
                } else {
                    detailViewModel.addFavorite()
                }
            }
            detailViewModel.detailUser.observe(this) {user ->
                favoriteStatus = if (user!=null){
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                    true
                }else{
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                    false
                }
            }
            detailViewModel.getDetailUsername(username.toString())
            detailViewModel.getLocalUser(username.toString())

            val sectionsPagerAdapter = SectionPagerAdapter(this)
            sectionsPagerAdapter.username = username.toString()
            val viewPager = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs = binding.tabs
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }
    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setDetailUser(detailUser: DetailUserResponse){
        Glide.with(this)
            .load(detailUser.avatarUrl)
            .circleCrop()
            .into(binding.ivAvatar)
        binding.tvName.text = detailUser.name.toString()
        binding.tvRepository.text = detailUser.publicRepos.toString()
        binding.tvFollowers.text = detailUser.followers.toString()
        binding.tvFollowing.text = detailUser.following.toString()

        if (detailUser.location == null){
            binding.tvLocation.text = "-"
        } else{
            binding.tvLocation.text = detailUser.location.toString()
        }

        if(detailUser.email == null){
            binding.tvEmail.text = "-"
        }else{
            binding.tvEmail.text = detailUser.email.toString()
        }
    }

    companion object{
        const val EXTRA_USERNAME = "user.name"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
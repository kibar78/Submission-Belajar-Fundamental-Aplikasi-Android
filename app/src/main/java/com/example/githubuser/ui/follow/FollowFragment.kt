package com.example.githubuser.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.data.model.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.repository.UserUiState
import com.example.githubuser.ui.main.ViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class FollowFragment : Fragment() {

    private lateinit var rvFollow: RecyclerView

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel by viewModels<FollowViewModel>{
    ViewModelFactory.getInstance(requireActivity().application)
    }

    private var position: Int? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            position = it?.getInt(ARG_PARAM1)
            username = it?.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFollow = binding.rvFollow

        followViewModel.listFollowersState.observe(requireActivity()){stateFollowers->
            when(stateFollowers){
                is UserUiState.Loading ->{
                    showLoading(true)
                }
                is UserUiState.Success ->{
                    setUser(stateFollowers.data)
                    showLoading(false)
                }
                is UserUiState.Error ->{
                    showLoading(false)
                }
            }
        }
        followViewModel.listFollowingState.observe(requireActivity()){stateFollowing->
            when(stateFollowing){
                is UserUiState.Loading ->{
                    showLoading(true)
                }
                is UserUiState.Success ->{
                    setUser(stateFollowing.data)
                    showLoading(false)
                }
                is UserUiState.Error ->{
                    showLoading(false)
                }
            }
        }
        if (position == 1){
            followViewModel.getListFollowers(username.toString())
        }else{
            followViewModel.getListFollowing(username.toString())
        }
    }

    private fun setUser(dataUsers: List<ItemsItem?>) {
        val adapter = UserAdapter()
        adapter.submitList(dataUsers)
        rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: Int, param2: String) =
                FollowFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
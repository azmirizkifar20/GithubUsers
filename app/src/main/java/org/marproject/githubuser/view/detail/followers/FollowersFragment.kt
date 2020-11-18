package org.marproject.githubuser.view.detail.followers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_followers.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.marproject.githubuser.data.network.response.UserResponse
import org.marproject.githubuser.databinding.FragmentFollowersBinding
import org.marproject.githubuser.utils.adapter.UserAdapter

class FollowersFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle

            return fragment
        }
    }

    // binding
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    // viewModel
    private val viewModel: FollowersViewModel by viewModel()

    // util
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get arguments
        val username = arguments?.getString(ARG_USERNAME)

        if (username != null) {
            // init adapter
            adapter = UserAdapter()

            with(binding.recyclerviewUser) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = this@FollowersFragment.adapter
            }

            // fetch data
            viewModel.fetchData(username).observe(this, observer)

            // loading
            viewModel.isLoading.observe(this, {
                if (it == false) binding.loading.visibility = View.GONE
                else binding.loading.visibility = View.VISIBLE
            })
        }
    }

    private val observer = Observer<List<UserResponse>> {
        if (it != null)
            adapter.setUsers(it)

        if (it.isEmpty())
            view_empty.visibility = View.VISIBLE
        else
            view_empty.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
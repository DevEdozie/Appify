package com.example.appify.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appify.R
import com.example.appify.adapter.UserAdapter
import com.example.appify.database.model.User
import com.example.appify.utilities.SessionManager
import com.example.appify.databinding.FragmentHomeBinding
import com.example.appify.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // ViewModel instance for HomeFragment
    private val viewModel by viewModels<HomeViewModel>()

    // Retrieve the arguments passed to this fragment
    private val args: HomeFragmentArgs by navArgs()

    // Adapter for the user RecyclerView
    private lateinit var userAdapter: UserAdapter

    // User object for the current user
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Get the current user from the arguments, ensuring it's non-null
        currentUser = args.user!!

        // Add the new user to the ViewModel
        addNewUser()

        // Display the username
        displayUsername()

        // Set up the RecyclerView for users
        setUpUsersRecyclerView()

        // Set up logout button
        setUpLogoutButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Override the back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Close the app when the back button is pressed
                    requireActivity().finish()
                }
            })
    }

    private fun displayUsername() {
        // Retrieve the username from SessionManager and display it
        val username = "Welcome ${SessionManager.getUsername(requireContext())}"
        binding.usernameEt.text = username
    }

    private fun addNewUser() {
        // Add the current user to the ViewModel
        viewModel.addUser(currentUser)
    }

    // Function to set up the RecyclerView for users
    private fun setUpUsersRecyclerView() {
        // Initialize the UserAdapter
        userAdapter = UserAdapter()

        // Configure the RecyclerView with a layout manager and the adapter
        binding.userRecyclerview.apply {
            layoutManager = LinearLayoutManager(context) // Set the layout manager
            adapter = userAdapter // Set the adapter
        }

        // Observe users LiveData and update the RecyclerView when the data changes
        viewModel.getAllUsers().observe(viewLifecycleOwner) { users ->
            userAdapter.differ.submitList(users) // Submit the users list to the adapter
        }
    }

    private fun setUpLogoutButton() {
        binding.logoutFab.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "${SessionManager.getUsername(requireContext())} LOGGED OUT",
                Toast.LENGTH_SHORT
            ).show()
            val isRemeberMeChecked = SessionManager.getRememberMeStatus(requireContext())
            if (!isRemeberMeChecked) {
                // Remove Saved user from shared preferences
                SessionManager.clearData(requireContext())
            }else{
                SessionManager.saveLoginState(requireContext(),false)
            }
            // Navigate to login screen
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
}

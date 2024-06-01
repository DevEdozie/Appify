package com.example.appify.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appify.utilities.LoadingDialog
import com.example.appify.database.model.User
import com.example.appify.databinding.FragmentLoginBinding
import com.example.appify.network.BaseResponse
import com.example.appify.network.response.UserResponse
import com.example.appify.utilities.SessionManager
import com.example.appify.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    // Binding object instance for accessing UI elements
    private lateinit var binding: FragmentLoginBinding

    // ViewModel instance for handling login-related logic
    private val viewModel by viewModels<LoginViewModel>()

    // List to hold user data from the API
    private var users: List<UserResponse>? = null

    // Variable to hold error messages
    private var errorMessage: String? = null

    // Variable to hold user ID
    private var userId: Int? = null

    // Lazy initialization of the loading dialog
    private val loadingDialog by lazy {
        LoadingDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Check if the user is already logged in
        checkLoginStatus()

        // Observe user data from the ViewModel
        observeUsersData()

        // Fetch user data from the API
        viewModel.getUsers()

        // Set OnClickListener for the login button
        binding.loginBtn.setOnClickListener {
            showLoading()
            validateFields()
        }

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

    // Function to check the login status and navigate accordingly
    private fun checkLoginStatus() {
        val context = requireContext()
        val isLoggedIn = SessionManager.getLoginState(context)

        if (isLoggedIn) {
            // Navigate to the home fragment if the user is logged in
            val userId = SessionManager.getUserId(context)
            val username = SessionManager.getUsername(context) ?: return
            val currentUser = User(userId, username)
            val directions = LoginFragmentDirections.actionLoginFragmentToHomeFragment(currentUser)
            findNavController().navigate(directions)
        } else {
            // Populate the fields if the user is not logged in
            val firstName = SessionManager.getFirstName(context)
            val lastName = SessionManager.getLastName(context)
            val password = "password"

            binding.firstNameEt.setText(firstName)
            binding.lastNameEt.setText(lastName)
            binding.passwordEt.setText(password)
        }
    }


    // Function to observe user data from the ViewModel
    private fun observeUsersData() {
        viewModel.usersResult.observe(viewLifecycleOwner) { usersResponse ->
            when (usersResponse) {
                is BaseResponse.Loading -> {
                    // Show loading state
                    showLoading()
                }

                is BaseResponse.Success -> {
                    // On success, update user list and show success message
                    users = usersResponse.data
                    stopLoading()
                    Toast.makeText(
                        requireContext(),
                        "DATA FETCHED SUCCESSFULLY",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is BaseResponse.Error -> {
                    // On error, update error message and show error message
                    stopLoading()
                    errorMessage = usersResponse.message
                    Toast.makeText(
                        requireContext(),
                        "ERROR: ${usersResponse.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Function to validate input fields before login
    private fun validateFields() {
        val firstName = binding.firstNameEt.text.toString().trim()
        val lastName = binding.lastNameEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        val isRememberMeChecked = binding.rememberMeCheckBox.isChecked
        val defaultPassword = "password"

        // Check if all fields are filled
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && password.isNotEmpty()) {
            // Check if the entered password matches the default password
            if (password != defaultPassword) {
                stopLoading()
                Toast.makeText(requireContext(), "Password is 'password'", Toast.LENGTH_LONG).show()
            } else {
                // Proceed with login if fields are valid
                doLogin(firstName, lastName, isRememberMeChecked)
            }
        } else {
            stopLoading()
            // Set error messages for empty fields
            if (firstName.isEmpty()) {
                binding.firstNameEt.error = "Field can't be empty"
            }
            if (lastName.isEmpty()) {
                binding.lastNameEt.error = "Field can't be empty"
            }
            if (password.isEmpty()) {
                binding.passwordEt.error = "Field can't be empty"
            }
        }
    }

    // Function to handle the login logic
    private fun doLogin(firstName: String, lastName: String, checkboxState: Boolean) {
        val name = "$firstName $lastName"

        // Check if user data is available
        if (users == null) {
            stopLoading()
            Toast.makeText(requireContext(), "User data is not available", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Check if the entered name matches any user name in the list
        val isUserFound = users?.any { user -> user.name == name } ?: false
        val user = users?.find { user -> user.name == name }

        // Extract the user ID
        if (user != null) {
            userId = user.id
        }

        if (isUserFound) {
            // Show success message and navigate to home fragment
            Toast.makeText(requireContext(), "SUCCESS: User found", Toast.LENGTH_SHORT).show()
            // Save session details
            SessionManager.saveUsername(requireContext(), name)
            userId?.let { SessionManager.saveUserId(requireContext(), it) }
            SessionManager.saveRememberMeStatus(requireContext(), checkboxState)
            SessionManager.saveFirstName(requireContext(), firstName)
            SessionManager.saveLastName(requireContext(), lastName)
            SessionManager.saveLoginState(requireContext(),true)

            stopLoading()
            val currentUser = userId?.let { User(it, name) }
            val directions = LoginFragmentDirections.actionLoginFragmentToHomeFragment(currentUser)
            findNavController().navigate(directions)
        } else {
            stopLoading()
            // Show error message if user is not found
            Toast.makeText(requireContext(), "ERROR: User not found", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to show the loading dialog
    private fun showLoading() {
        loadingDialog.show()
    }

    // Function to stop the loading dialog
    private fun stopLoading() {
        loadingDialog.cancel()
    }
}

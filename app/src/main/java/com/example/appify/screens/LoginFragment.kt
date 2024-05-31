package com.example.appify.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appify.R
import com.example.appify.SessionManager
import com.example.appify.databinding.FragmentLoginBinding
import com.example.appify.network.BaseResponse
import com.example.appify.network.response.UserResponse
import com.example.appify.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    // Binding object instance
    private lateinit var binding: FragmentLoginBinding

    // ViewModel instance for Login
    private val viewModel by viewModels<LoginViewModel>()

    // List to hold users data
    private var users: List<UserResponse>? = null

    // Variable to hold error message
    private var errorMessage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Observe user data from ViewModel
        observeUsersData()

        // Fetch users data
        viewModel.getUsers()

        // Set OnClickListener for login button
        binding.loginBtn.setOnClickListener {
            Log.i("USERS", "BUTTON CLICKED>>>>>")
            validateFields()
        }

        return binding.root
    }

    // Method to observe users data
    private fun observeUsersData() {
        viewModel.usersResult.observe(viewLifecycleOwner) { usersResponse ->
            when (usersResponse) {
                is BaseResponse.Loading -> {
                    // Show loading state
                    Log.i("USERS", "LOADING: $usersResponse")
                    Toast.makeText(requireContext(), "LOADING", Toast.LENGTH_SHORT).show()
                }

                is BaseResponse.Success -> {
                    // On success, update users list and show success message
                    users = usersResponse.data
                    Log.i("USERS", "SUCCESS: ${usersResponse.data}")
                    Toast.makeText(requireContext(), "SUCCESS", Toast.LENGTH_LONG).show()
                }

                is BaseResponse.Error -> {
                    // On error, update error message and show error message
                    errorMessage = usersResponse.message
                    Log.i("USERS", "ERROR: ${usersResponse.message}")
                    Toast.makeText(requireContext(), "ERROR: ${usersResponse.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Method to validate input fields
    private fun validateFields() {
        val firstName = binding.firstNameEt.text.toString().trim()
        val lastName = binding.lastNameEt.text.toString().trim()
        val password = binding.passwordEt.text.toString().trim()
        val defaultPassword = "password"

        // Check if all fields are filled
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && password.isNotEmpty()) {
            // Check if the entered password matches the default password
            if (password != defaultPassword) {
                Toast.makeText(requireContext(), "Password is 'password'", Toast.LENGTH_LONG).show()
            } else {
                // Proceed with login if fields are valid
                doLogin(firstName, lastName)
            }
        } else {
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

    // Method to handle login logic
    private fun doLogin(firstName: String, lastName: String) {
        val name = "$firstName $lastName"

        // Check if user data is available
        if (users == null) {
            Toast.makeText(requireContext(), "User data is not available", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the entered name matches any user name in the list
        val isUserFound = users?.any { user -> user.name == name } ?: false

        if (isUserFound) {
            // Show success message and navigate to home fragment
            Toast.makeText(requireContext(), "SUCCESS: User found", Toast.LENGTH_SHORT).show()
            SessionManager.saveUsername(requireContext(), name)
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        } else {
            // Show error message if user is not found
            Toast.makeText(requireContext(), "ERROR: User not found", Toast.LENGTH_SHORT).show()
        }
    }
}

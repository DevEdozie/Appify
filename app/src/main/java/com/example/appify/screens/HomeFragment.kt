package com.example.appify.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appify.R
import com.example.appify.SessionManager
import com.example.appify.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        displayUsername()
        return binding.root
    }

    private fun displayUsername(){
       val username = "Welcome ${SessionManager.getUsername(requireContext())}"
       binding.usernameEt.text = username
    }


}
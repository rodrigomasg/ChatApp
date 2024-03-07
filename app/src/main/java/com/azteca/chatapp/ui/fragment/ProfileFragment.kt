package com.azteca.chatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.azteca.chatapp.R
import com.azteca.chatapp.common.Service.Companion.getCurrentUid
import com.azteca.chatapp.common.Service.Companion.getInfUser
import com.azteca.chatapp.data.model.UserModel
import com.azteca.chatapp.data.model.UserModelResponse
import com.azteca.chatapp.databinding.FragmentProfileBinding
import java.sql.Timestamp

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userModel: UserModelResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponent()
    }

    private fun initComponent() {
        binding.profileIvBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.updateBtnSend.setOnClickListener { validateData() }
        binding.profileTvLogout.setOnClickListener { userLogout() }

        getProfileUser()
    }

    private fun getProfileUser() {
        if (getCurrentUid() != null) {
            getInfUser(getCurrentUid()!!).get().addOnCompleteListener {
                userModel = it.result.toObject(UserModelResponse::class.java)
                if (userModel != null) {
                    binding.profileEtUsername.setText(userModel!!.username)
                    binding.profileEtNumber.setText(userModel!!.phone)
                }
            }
        }
    }

    private fun validateData() {
        val newUsername = binding.profileEtUsername.text.toString().trim()
        if (newUsername.isNotEmpty()
            && userModel != null
        ) {
            val sendModel = UserModel(
                userModel!!.userId,
                userModel!!.phone,
                newUsername,
                Timestamp(System.currentTimeMillis())
            )
            getInfUser(getCurrentUid()!!).set(sendModel).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.profile_data_updated,
                        Toast.LENGTH_LONG
                    ).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun userLogout() {
        /*FirebaseAuth.getInstance().signOut()
        requireActivity().finish()
        SharedPrefs(requireContext()).setValueLogin(false)
        startActivity(Intent(requireContext(), LoginActivity::class.java))*/
    }
}
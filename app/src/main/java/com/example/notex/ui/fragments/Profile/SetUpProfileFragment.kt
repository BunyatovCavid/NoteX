package com.example.notex.ui.fragments.Profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.notex.Independents.helper.toast
import com.example.notex.R
import com.example.notex.data.models.UserModel
import com.example.notex.databinding.FragmentSetUpProfileBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetUpProfileFragment : Fragment(R.layout.fragment_set_up_profile) {

    private var _binding:FragmentSetUpProfileBinding? =null
    private val binding get() = _binding!!
    private val userViewModel:UserViewModel by viewModels()
    private var userImageUrl:String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSetUpProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getUser()
        userViewModel.user.observe(viewLifecycleOwner, {result->
            Glide.with(this)
                .load(result.imageUrl)
                .into(binding.userImage)
            binding.editTextTextName.setText(result.name)
            binding.editTextTextEmailAddress.setText(result.email)
        })


        binding.userImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }


        binding.savebutton.setOnClickListener{
            var name =binding.editTextTextName.text
            var email =binding.editTextTextEmailAddress.text
            var image =binding.userImage.drawable

            if(name.isNotBlank() && email.isNotBlank()&&
                image!=null) {
                var userModel = UserModel()
                userModel.name =name.toString()
                userModel.email =email.toString()
                userModel.imageUrl = userImageUrl

                userViewModel.updateUser(userModel)
                userViewModel.response?.observe(viewLifecycleOwner, {result->
                    if(result=="Sucess")
                        context?.toast(result)
                    else
                        context?.toast(result)
                })
            } else {
                context?.toast("Please fill all area")
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
           var imageUri = data?.data
            binding.userImage.setImageURI(imageUri)

            val storageReference = FirebaseStorage.getInstance().getReference("profile_images/${FirebaseAuth.getInstance().currentUser?.uid}.jpg")

            imageUri?.let {
                storageReference.putFile(it).addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        userImageUrl = imageUrl
                    }.addOnFailureListener {
                        context?.toast("Şəkil yüklənmədi: ${it.message}")
                    }
                }.addOnFailureListener {
                    context?.toast("Yükləmə uğursuz oldu: ${it.message}")
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.INVISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.VISIBLE
        }
    }

}
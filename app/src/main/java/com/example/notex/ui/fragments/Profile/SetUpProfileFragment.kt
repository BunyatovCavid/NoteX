package com.example.notex.ui.fragments.Profile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
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
    private lateinit var nav:replaceFragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetUpProfileBinding.inflate(inflater, container, false)
        nav = replaceFragments()
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val datas = arguments?.getParcelable<UserModel>("User")
            Glide.with(this)
                .load(datas?.imageUrl)
                .into(binding.userImage)
            binding.editTextTextName.setText(datas?.name)

        binding.userImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }


        binding.savebutton.setOnClickListener{
            Glide.with(this)
                .load(userImageUrl)
                .into(binding.userImage)

            var name =binding.editTextTextName.text
            var image =binding.userImage.drawable



            if(name.isNotBlank()&&
                image!=null) {
                var userModel = UserModel()
                userModel.name =name.toString()
                userModel.imageUrl = userImageUrl

                userViewModel.updateUser(userModel)
                userViewModel.response?.observe(viewLifecycleOwner, {result->
                    if(result=="Success"){
                        context?.toast(result)
                        nav.replace(this, R.id.action_setUpProfileFragment_to_profileFragment2)
                }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->{
                nav.replace(this, R.id.action_setUpProfileFragment_to_profileFragment2)
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
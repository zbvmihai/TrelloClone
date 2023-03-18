package com.zabava.trelloclone.activities


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivityMyProfileBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.User
import java.io.IOException

@Suppress("DEPRECATION")
class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mSelectedImageFileURI: Uri? = null

    private var binding: ActivityMyProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        setupActionBar()

        binding?.toolbarMyProfileActivity?.setNavigationOnClickListener { onBackPressed() }

        FirestoreClass().loadUserData(this)

        binding?.ivUserProfileImage?.setOnClickListener{

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){

                showImageChooser()

            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }else{
                Toast.makeText(this,"Permission for storage denied.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){
            mSelectedImageFileURI = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileURI)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding?.ivUserProfileImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMyProfileActivity)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar?.title = resources.getString(R.string.my_profile)

    }

    fun setUserDataInUI(user: User){
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding?.ivUserProfileImage!!)

        binding?.etName?.setText(user.name)
        binding?.etEmail?.setText(user.email)
        if (user.mobile != 0L){
            binding?.etMobile?.setText(user.mobile.toString())
        }
    }
}
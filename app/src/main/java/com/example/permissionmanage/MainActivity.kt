package com.example.permissionmanage


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissionmanage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    companion object {
        private const val OPEN_FILE_REQUEST_CODE = 1
        private const val OPEN_FOLDER_REQUEST_CODE = 2
        private const val MEDIA_LOCATION_PERMISSION_REQUEST_CODE = 3
        private const val CHOOSE_FILE = 4

        //var downloadImageUrl = "https://cdn.pixabay.com/photo/2020/04/21/06/41/bulldog-5071407_1280.jpg"
        private const val PERMISSION_READ_EXTERNAL_STORAGE = 5

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnrequest?.setOnClickListener {
            openFolderLocation()
        }
        binding?.btnimagelocation?.setOnClickListener {
            fetchMediaLocation()
        }
    }

    private fun openFolderLocation() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        startActivityForResult(intent, OPEN_FOLDER_REQUEST_CODE)
    }
    private fun fetchMediaLocation() {
        if (Build.VERSION.SDK_INT<= Build.VERSION_CODES.Q){
            openChooser()
        }else{
            if (isPermissionGrantedForMediaLocationAccess(this)){
                openChooser()
            }else{
                requestPermissionForMediaLocation(this)
            }
        }
    }

    private fun openChooser() {
        val intent  = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false)
        intent.action =Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), CHOOSE_FILE)
    }

    //Request Permission For Read Storage
    private fun isPermissionGrantedForMediaLocationAccess(context: Context): Boolean {
        val result: Int =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        return result == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermissionForMediaLocation(context: Context){
        ActivityCompat.requestPermissions(
            context as Activity
            , arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION)
            , MEDIA_LOCATION_PERMISSION_REQUEST_CODE
        )
    }

}
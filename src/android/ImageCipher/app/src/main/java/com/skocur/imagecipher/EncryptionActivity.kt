package com.skocur.imagecipher

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.activity_encryption.*

class EncryptionActivity : AppCompatActivity() {

    private val REQUEST_PHOTO_GALLERY = 0
    private lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encryption)

        buttonEncLoadImage.setOnClickListener {
            // TODO: Add - asking user for permission to access smartphone's storage
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, REQUEST_PHOTO_GALLERY)
        }

        buttonEncrypt.setOnClickListener {
            val text : String = textToEncrypt.text.toString()
            var option = 3 // Default encryption is set to Low Level Bit Encryption

            if (radioSingleColorEncryption.isChecked) {
                option = 1
            } else if (radioMultiColorEncryption.isChecked) {
                option = 2
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO_GALLERY) {
            if (data != null) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)

                imageFromGalleryEnc.setImageBitmap(bitmap)
                imageFromGalleryEnc.visibility = View.VISIBLE
            }
        }
    }
}

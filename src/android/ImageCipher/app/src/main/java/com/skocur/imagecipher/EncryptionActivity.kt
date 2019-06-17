package com.skocur.imagecipher

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.skocur.imagecipher.encrypters.Encrypter
import com.skocur.imagecipher.encrypters.LowLevelBitEncryption
import kotlinx.android.synthetic.main.activity_encryption.*

class EncryptionActivity : AppCompatActivity() {

    private val REQUEST_PHOTO_GALLERY = 0
    private lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encryption)

        buttonEncLoadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, REQUEST_PHOTO_GALLERY)
        }

        buttonEncrypt.setOnClickListener {
            val text : String = textToEncrypt.text.toString()

            // Default encryption is set to Low Level Bit Encryption
            var encrypter : Encrypter = LowLevelBitEncryption(bitmap, contentResolver, applicationContext)
            if (radioSingleColorEncryption.isChecked) {

            } else if (radioMultiColorEncryption.isChecked) {

            }

            encrypter.encrypt(text)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO_GALLERY) {
            if (data != null) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                //val uri: Uri = intent.data
                imageFromGalleryEnc.setImageBitmap(bitmap)
                imageFromGalleryEnc.visibility = View.VISIBLE
            }
        }
    }
}

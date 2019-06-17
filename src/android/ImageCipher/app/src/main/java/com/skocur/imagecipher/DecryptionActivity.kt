package com.skocur.imagecipher

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.activity_decryption.*

class DecryptionActivity : AppCompatActivity() {

    private val REQUEST_PHOTO_GALLERY = 0
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decryption)

        buttonLoadImageDec.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, REQUEST_PHOTO_GALLERY)
        }

        buttonDecrypt.setOnClickListener {
            if (radioLowLevelBitDecryption.isChecked) {
                val decrypter = Decrypter(bitmap)

                decryptedText.text = decrypter.decryptLowLevelBits()
                decryptedText.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PHOTO_GALLERY) {
            if (data != null) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)

                imageFromGalleryDec.setImageBitmap(bitmap)
                imageFromGalleryDec.visibility = View.VISIBLE
            }
        }
    }
}

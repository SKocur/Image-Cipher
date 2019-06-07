package com.skocur.imagecipher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_encryption.*

class EncryptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encryption)

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
}

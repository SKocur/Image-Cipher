package com.skocur.imagecipher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainCardEncryption.setOnClickListener {
            val intent = Intent(this, EncryptionActivity::class.java)

            startActivity(intent)
        }

        mainCardDecryption.setOnClickListener {
            val intent = Intent(this, DecryptionActivity::class.java)

            startActivity(intent)
        }
    }
}

package com.skocur.imagecipher

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken?
                ) {}

                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                }
            }).check()

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

package com.example.firebaseremoteconfig

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseremoteconfig.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayAdapter: ArrayAdapter<*>


        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)


        binding.jsonBtn.setOnClickListener {
            remote_method()
        }

    }

    fun remote_method() {
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val json_test = mFirebaseRemoteConfig.getString("json_test")
                try {
                    val array = JSONArray(json_test)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(
                            binding.edittext.getText().toString().trim().toInt()
                        )
                        val id = obj.getString("Id")
                        val name = obj.getString("Name")
                        val gender = obj.getString("Gender")
                        binding.jsonText.setText(
                            String.format(
                                "Id: %s\nName: %s\nGender: %s\n",
                                id ,
                                name,
                                gender,
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "onComplete: " + e.message)
                }
            }
        }
    }
}
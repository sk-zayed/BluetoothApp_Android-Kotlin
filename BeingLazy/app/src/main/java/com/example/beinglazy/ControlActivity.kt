package com.example.beinglazy

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.text.Html
import android.util.Log
import android.widget.Toast
import com.example.beinglazy.databinding.ControlActivityBinding
import java.io.IOException

class ControlActivity : AppCompatActivity() {

    lateinit var binding: ControlActivityBinding

    companion object {
        var m_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var bluetoothSocket: BluetoothSocket? = null
        lateinit var progress: ProgressDialog
        lateinit var bluetoothAdapter: BluetoothAdapter
        var isConnected: Boolean = false
        lateinit var address: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ControlActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"))

        address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS).toString()

        ConnectToDevice(this).execute()

        binding.light.setOnClickListener {
            sendCommand("2")
            if (binding.light.text.toString().equals("ON LIGHT")) {
                binding.light.setText("OFF LIGHT")
            } else {
                binding.light.setText("ON LIGHT")
            }
        }
        binding.fan.setOnClickListener {
            sendCommand("3")
            if (binding.fan.text.toString().equals("ON FAN")) {
                binding.fan.setText("OFF FAN")
            } else {
                binding.fan.setText("ON FAN")
            }
        }
        binding.socket1.setOnClickListener {
            sendCommand("4")
            if (binding.socket1.text.toString().equals("ON SOCKET 1")) {
                binding.socket1.setText("OFF SOCKET 1")
            } else {
                binding.socket1.setText("ON SOCKET 1")
            }
        }
        binding.socket2.setOnClickListener {
            sendCommand("5")
            if (binding.socket2.text.toString().equals("ON SOCKET 2")) {
                binding.socket2.setText("OFF SOCKET 2")
            } else {
                binding.socket2.setText("ON SOCKET 2")
            }
        }

        binding.disconnect.setOnClickListener { disconnect() }

    }

    private fun sendCommand(input: String) {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.close()
                bluetoothSocket = null
                isConnected = false
                Toast.makeText(this, "Disconnted..", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context
        val ca = ControlActivity()

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        @SuppressLint("MissingPermission")
        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (bluetoothSocket == null || !isConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
                    bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_UUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
                ControlActivity().finfun()
            } else {
                isConnected = true
            }
            progress.dismiss()
        }
    }

    fun finfun() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
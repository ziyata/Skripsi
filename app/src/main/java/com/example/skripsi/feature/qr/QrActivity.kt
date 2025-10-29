package com.example.skripsi.feature.qr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.skripsi.databinding.ActivityQrBinding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@androidx.camera.core.ExperimentalGetImage
class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    @Volatile private var handledOnce = false

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) startCamera() else Toast.makeText(this, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(options)

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(cameraExecutor) { imageProxy: ImageProxy ->
                if (handledOnce) { imageProxy.close(); return@setAnalyzer }
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            if (barcodes.isNotEmpty()) {
                                val raw = barcodes.first().rawValue ?: ""
                                handlePayload(raw)
                                handledOnce = true
                            }
                        }
                        .addOnFailureListener { /* optionally log */ }
                        .addOnCompleteListener { imageProxy.close() }
                } else {
                    imageProxy.close()
                }
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal memulai kamera: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun handlePayload(raw: String) {
        // Format: item:{id} | cart:{base64json} | table:{id}
        when {
            raw.startsWith("item:") -> {
                val id = raw.removePrefix("item:").toIntOrNull()
                if (id != null) {
                    setResult(RESULT_OK, intent.putExtra("qr_type", "item").putExtra("item_id", id))
                    finish()
                } else {
                    Toast.makeText(this, "QR item tidak valid", Toast.LENGTH_SHORT).show()
                    handledOnce = false
                }
            }
            raw.startsWith("table:") -> {
                val tableId = raw.removePrefix("table:").toIntOrNull()
                if (tableId != null) {
                    setResult(RESULT_OK, intent.putExtra("qr_type", "table").putExtra("table_id", tableId))
                    finish()
                } else {
                    Toast.makeText(this, "QR meja tidak valid", Toast.LENGTH_SHORT).show()
                    handledOnce = false
                }
            }
            raw.startsWith("cart:") -> {
                val payload = raw.removePrefix("cart:")
                setResult(RESULT_OK, intent.putExtra("qr_type", "cart").putExtra("cart_payload", payload))
                finish()
            }
            else -> {
                Toast.makeText(this, "Format QR tidak dikenali", Toast.LENGTH_SHORT).show()
                handledOnce = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

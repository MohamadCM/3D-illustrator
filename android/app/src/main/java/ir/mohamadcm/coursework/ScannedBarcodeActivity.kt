package ir.mohamadcm.coursework

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import ir.mohamadcm.coursework.databinding.ActivityScannedBarcodeBinding

class ScannedBarcodeActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private lateinit var camera: Camera
    private lateinit var cameraProvider: ProcessCameraProvider

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 1003
    }

    private val screenAspectRatio by lazy {
        val metrics = DisplayMetrics().also { binding.previewView.display.getRealMetrics(it) }
        2
    }
    private lateinit var binding: ActivityScannedBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannedBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isCameraPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }

        binding.imgFlashLight.setOnClickListener {
            if (camera != null) {
                if (camera.cameraInfo.torchState.value == TorchState.ON) {
                    //setFlashOffIcon()
                    camera.cameraControl.enableTorch(false)
                } else {
                    //setFlashOnIcon()
                    camera.cameraControl.enableTorch(true)
                }
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(camProvider: ProcessCameraProvider) {
        cameraProvider = camProvider

        val previewUseCase = Preview.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .setTargetAspectRatio(screenAspectRatio as Int)
            .build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
        val barcodeScanner = BarcodeScanning.getClient()
        val analysisUseCase = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .setTargetAspectRatio(screenAspectRatio as Int)
            .build().also {
                it.setAnalyzer(
                    ContextCompat.getMainExecutor(this),
                    { imageProxy ->
                        processImageProxy(barcodeScanner, imageProxy)
                    }
                )
            }
        val useCaseGroup = UseCaseGroup.Builder().addUseCase(previewUseCase).addUseCase(
            analysisUseCase
        ).build()

        camera = cameraProvider.bindToLifecycle(
            this,
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build(),
            useCaseGroup
        )
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {

        // This scans the entire screen for barcodes
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    if (!barcodeList.isNullOrEmpty()) {
                        if (!barcodeList[0].rawValue.isNullOrEmpty()) {
                            Log.e(TAG, "processImageProxy: " + barcodeList[0].rawValue)
                            cameraProvider.unbindAll()
                            //setFlashOffIcon()
                            Snackbar.make(
                                this@ScannedBarcodeActivity, binding.clMain,
                                "${barcodeList[0].rawValue!!}", Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction("Retry") {
                                    startCamera()
                                }
                                .show()
                            /*val myIntent = Intent(this@ScannedBarcodeActivity, ModelViewer::class.java)
                            myIntent.putExtra("model", barcodeList[0].rawValue!!) //Optional parameters
                            this@ScannedBarcodeActivity.startActivity(myIntent)*/
                            // ...
                            // Instantiate the RequestQueue.
                            val queue = Volley.newRequestQueue(this)
                            val url = barcodeList[0].rawValue

                            // Request a string response from the provided URL.
                            val stringRequest = StringRequest(
                                Request.Method.GET, url,
                                Response.Listener<String> { response ->
                                    // Display the first 500 characters of the response string.
                                    Log.e("SHIT","Response is: $response")
                                    val sceneViewerIntent = Intent(Intent.ACTION_VIEW)

                                    sceneViewerIntent.data =
                                        Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=$response")
                                    sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
                                    startActivity(sceneViewerIntent)
                                },
                                Response.ErrorListener { Log.e("SHIT","Response is: did not work!") })

                                // Add the request to the RequestQueue.
                            queue.add(stringRequest)

                        }
                    }
                }.addOnFailureListener {
                    image.close()
                    imageProxy.close()
                    Log.e(TAG, "processImageProxy: ", it)
                }.addOnCompleteListener {
                    image.close()
                    imageProxy.close()
                }
        }
    }

    override fun onResume() {
        super.onResume()
        // setFlashOffIcon()
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                startCamera()
            } else {
                //show custom dialog of camera permission if permission is permanently denied
                Toast.makeText(
                    applicationContext,
                    "Please allow camera permission!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (isCameraPermissionGranted()) {
                    startCamera()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please allow camera permission!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

/*    private fun setFlashOffIcon() {
        binding.imgFlashLight.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_flash_off_24,
                null
            )
        )
    }*/

    /* private fun setFlashOnIcon(){
         binding.imgFlashLight.setImageDrawable(
             ResourcesCompat.getDrawable(
                 resources,
                 R.drawable.ic_flash_on_24,
                 null
             )
         )
     }*/
}
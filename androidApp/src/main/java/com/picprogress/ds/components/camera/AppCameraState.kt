package com.picprogress.ds.components.camera

import android.content.Context
import android.util.Log
import android.view.Surface
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import com.picprogress.model.camera.CameraLens
import com.picprogress.model.camera.FlashMode
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppCameraState(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) {

    private var isTakingPicture = false

    private val temporaryFolder by lazy {
        File(context.filesDir, CameraTempFolder).also {
            it.deleteRecursively()
            it.mkdirs()
        }
    }

    private var currentCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private val imageCaptureUseCase: ImageCapture = ImageCapture.Builder()
        .setTargetRotation(Surface.ROTATION_0)
        .setResolutionSelector(
            ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                .build()
        ).build()

    private val previewUseCase: Preview = Preview.Builder()
        .setTargetRotation(Surface.ROTATION_0)
        .setResolutionSelector(
            ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
                .build()
        ).build()

    fun createPreviewView(context: Context): PreviewView = PreviewView(context).apply {
        setBackgroundColor(Color.Black.toArgb())
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        scaleType = PreviewView.ScaleType.FIT_CENTER
        implementationMode = PreviewView.ImplementationMode.PERFORMANCE
    }.also { previewUseCase.setSurfaceProvider(it.surfaceProvider) }

    private suspend fun initialize(): Boolean {
        return try {
            with(context.getCameraProvider()) {
                unbindAll()
                bindToLifecycle(
                    lifecycleOwner, currentCameraSelector, previewUseCase, imageCaptureUseCase
                )
            }
            true
        } catch (ex: Exception) {
            Log.e("CameraPreview", "Use case binding failed", ex)
            false
        }
    }

    fun takePicture(
        onSuccess: (File) -> Unit,
    ) {
        isTakingPicture = true
        val tempFile = File(temporaryFolder, "file-${System.currentTimeMillis()}.jpg")
        val metadata = ImageCapture.Metadata()
        metadata.isReversedHorizontal = currentCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA

        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile)
            .setMetadata(metadata)
            .build()

        imageCaptureUseCase.takePicture(
            outputOptions,
            context.executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.toFile()?.let(onSuccess)
                    isTakingPicture = false
                }

                override fun onError(exception: ImageCaptureException) {
                    isTakingPicture = false
                }
            }
        )
    }

    fun setFlashMode(flashMode: FlashMode) {
        imageCaptureUseCase.flashMode = when (flashMode) {
            FlashMode.OFF -> ImageCapture.FLASH_MODE_OFF
            FlashMode.ON -> ImageCapture.FLASH_MODE_ON
            FlashMode.AUTO -> ImageCapture.FLASH_MODE_AUTO
        }
    }

    suspend fun setCameraLens(cameraLens: CameraLens): Boolean {
        currentCameraSelector = when (cameraLens) {
            CameraLens.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
            CameraLens.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
        }
        return initialize()
    }

    companion object {
        private const val CameraTempFolder = "camera"
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            continuation.resume(future.get())
        }, executor)
    }
}

private val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

@Composable
fun rememberCameraControlState(context: Context, lifecycleOwner: LifecycleOwner) = remember {
    AppCameraState(context, lifecycleOwner)
}
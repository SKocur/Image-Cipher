package com.imagecipher.app.ui.viewmodel

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.imagecipher.app.Decrypter
import com.imagecipher.app.encrypters.Encrypter
import com.imagecipher.app.encrypters.EncrypterManager
import com.imagecipher.app.encrypters.EncrypterType
import com.imagecipher.app.plugin.lib.annotations.IcAlgorithmSpecification
import com.imagecipher.app.ui.imageprocessing.ComposeImagePainter
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.io.File
import javax.imageio.ImageIO

class ImageCipherViewModel {
    private val coroutineScope = CoroutineScope(Dispatchers.Swing + SupervisorJob())
    
    var selectedImagePath by mutableStateOf<String?>(null)
        private set
    
    var originalImage by mutableStateOf<ImageBitmap?>(null)
        private set
        
    var processedImage by mutableStateOf<ImageBitmap?>(null)
        private set
    
    var isEncryptionMode by mutableStateOf(true)
        private set
        
    var selectedAlgorithm by mutableStateOf<Any?>(null)
        private set
        
    var inputText by mutableStateOf("")
    
    var outputText by mutableStateOf("")
        private set
        
    var availableAlgorithms by mutableStateOf<List<AlgorithmItem>>(emptyList())
        private set
    
    // Pixel Traversal State
    var selectedStartPoint by mutableStateOf<Offset?>(null)
        private set
        
    var isTraversalRunning by mutableStateOf(false)
        private set
        
    var traversalProgress by mutableStateOf(0f)
        private set
    
    data class AlgorithmItem(
        val name: String,
        val data: Any
    )
    
    fun loadImage(imagePath: String) {
        try {
            val file = File(imagePath)
            if (!file.exists()) {
                println("Image file does not exist: $imagePath")
                return
            }
            
            selectedImagePath = imagePath
            
            val bufferedImage = ImageIO.read(file)
            originalImage = bufferedImage.toComposeImageBitmap()
            
            updateAvailableAlgorithms()
            println("Image loaded successfully: $imagePath")
        } catch (e: Exception) {
            println("Failed to load image: $imagePath")
            e.printStackTrace()
        }
    }
    
    fun toggleMode() {
        isEncryptionMode = !isEncryptionMode
        selectedAlgorithm = null
        updateAvailableAlgorithms()
        println("Mode toggled to: ${if (isEncryptionMode) "Encryption" else "Decryption"}")
    }
    
    fun selectAlgorithm(algorithm: AlgorithmItem) {
        selectedAlgorithm = algorithm.data
        println("Algorithm selected: ${algorithm.name}")
    }
    
    private fun updateAvailableAlgorithms() {
        val path = selectedImagePath ?: return
        
        availableAlgorithms = if (isEncryptionMode) {
            getEncryptionAlgorithms(path)
        } else {
            getDecryptionAlgorithms()
        }
    }
    
    private fun getEncryptionAlgorithms(imagePath: String): List<AlgorithmItem> {
        val algorithms = mutableListOf<AlgorithmItem>()
        
        for (i in 1..3) {
            val encrypter = EncrypterManager.getEncrypter(EncrypterType.getType(i), imagePath)
            if (encrypter != null) {
                val name = getAlgorithmName(encrypter.javaClass)
                algorithms.add(AlgorithmItem(name, encrypter))
            }
        }
        
        return algorithms
    }
    
    private fun getDecryptionAlgorithms(): List<AlgorithmItem> {
        return listOf(
            AlgorithmItem("Single Color Decryption", "Single Color Decryption"),
            AlgorithmItem("Multi Color Decryption", "Multi Color Decryption"),
            AlgorithmItem("Low Level Bit Decryption", "Low Level Bit Decryption")
        )
    }
    
    private fun getAlgorithmName(algorithmClass: Class<*>): String {
        val specifications = algorithmClass.getDeclaredAnnotationsByType(IcAlgorithmSpecification::class.java)
        return if (specifications.isNotEmpty()) {
            specifications[0].algorithmName
        } else {
            algorithmClass.simpleName
        }
    }
    
    fun executeAlgorithm() {
        val algorithm = selectedAlgorithm ?: return
        val imagePath = selectedImagePath ?: return
        
        if (isEncryptionMode) {
            executeEncryption(algorithm as Encrypter)
        } else {
            executeDecryption(algorithm as String, imagePath)
        }
    }
    
    private fun executeEncryption(encrypter: Encrypter) {
        try {
            encrypter.use { enc ->
                enc.encrypt(inputText)
                outputText = "Encryption completed successfully"
                println("Encryption executed successfully")
            }
        } catch (e: Exception) {
            outputText = "Encryption failed: ${e.message}"
            println("Encryption failed")
            e.printStackTrace()
        }
    }
    
    private fun executeDecryption(algorithmName: String, imagePath: String) {
        try {
            val result = when (algorithmName) {
                "Single Color Decryption" -> Decrypter.decrypt(imagePath)
                "Multi Color Decryption" -> Decrypter.decryptBlue(imagePath)
                "Low Level Bit Decryption" -> {
                    val decrypter = Decrypter(imagePath)
                    decrypter.decryptLowLevelBits()
                }
                else -> "Unknown decryption algorithm"
            }
            
            outputText = result ?: "Decryption completed but no result"
            println("Decryption executed successfully")
        } catch (e: Exception) {
            outputText = "Decryption failed: ${e.message}"
            println("Decryption failed")
            e.printStackTrace()
        }
    }
    
    fun setStartPoint(point: Offset) {
        selectedStartPoint = point
        println("Start point selected: (${point.x.toInt()}, ${point.y.toInt()})")
    }
    
    fun runPixelTraversal(
        algorithm: String,
        animationPause: Int,
        iterations: Int,
        penColor: Color
    ) {
        val startPoint = selectedStartPoint
        val imagePath = selectedImagePath
        
        if (startPoint == null || imagePath == null) {
            outputText = "Please select a start point on the image first"
            return
        }
        
        isTraversalRunning = true
        traversalProgress = 0f
        
        println("Starting $algorithm traversal from (${startPoint.x.toInt()}, ${startPoint.y.toInt()})")
        
        try {
            val painter = ComposeImagePainter(
                imagePath = imagePath,
                startPoint = startPoint,
                penColor = penColor,
                animationPause = animationPause,
                iterations = iterations
            )
            
            coroutineScope.launch {
                try {
                    val resultImage = when (algorithm) {
                        "BFS" -> painter.paintBFS { progress, intermediateImage ->
                            traversalProgress = progress
                            processedImage = intermediateImage
                        }
                        "DFS" -> painter.paintDFS { progress, intermediateImage ->
                            traversalProgress = progress
                            processedImage = intermediateImage
                        }
                        else -> {
                            outputText = "Unknown algorithm: $algorithm"
                            isTraversalRunning = false
                            return@launch
                        }
                    }
                    
                    processedImage = resultImage
                    isTraversalRunning = false
                    traversalProgress = 1f
                    outputText = "$algorithm traversal completed from (${startPoint.x.toInt()}, ${startPoint.y.toInt()})"
                    
                } catch (e: Exception) {
                    outputText = "Traversal failed: ${e.message}"
                    isTraversalRunning = false
                    println("Pixel traversal failed")
                    e.printStackTrace()
                }
            }
            
        } catch (e: Exception) {
            outputText = "Failed to start traversal: ${e.message}"
            isTraversalRunning = false
            println("Failed to start pixel traversal")
            e.printStackTrace()
        }
    }
    
    fun resetTraversal() {
        selectedStartPoint = null
        isTraversalRunning = false
        traversalProgress = 0f
        processedImage = null
    }
}

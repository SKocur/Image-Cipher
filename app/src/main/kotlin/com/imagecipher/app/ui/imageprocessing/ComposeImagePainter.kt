package com.imagecipher.app.ui.imageprocessing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO

class ComposeImagePainter(
    private val imagePath: String,
    private val startPoint: Offset,
    private val penColor: Color,
    private val animationPause: Int,
    private val iterations: Int
) {
    
    suspend fun paintBFS(onProgress: (Float, ImageBitmap) -> Unit): ImageBitmap = withContext(Dispatchers.IO) {
        println("Starting BFS painting from (${startPoint.x.toInt()}, ${startPoint.y.toInt()})")
        
        val originalImage = ImageIO.read(java.io.File(imagePath))
        val workingImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_RGB)
        val graphics = workingImage.createGraphics()
        graphics.drawImage(originalImage, 0, 0, null)
        graphics.dispose()
        
        val visited = Array(workingImage.width) { BooleanArray(workingImage.height) }
        val queue: Queue<Pair<Int, Int>> = LinkedList()
        val targetColor = workingImage.getRGB(startPoint.x.toInt(), startPoint.y.toInt())
        val newColor = penColor.toArgb()
        
        println("Target color: ${Integer.toHexString(targetColor)}, New color: ${Integer.toHexString(newColor)}")
        
        if (targetColor == newColor) {
            println("Start point color is same as pen color, no changes needed")
            return@withContext workingImage.toComposeImageBitmap()
        }
        
        queue.offer(startPoint.x.toInt() to startPoint.y.toInt())
        visited[startPoint.x.toInt()][startPoint.y.toInt()] = true
        
        var processedPixels = 0
        var iteration = 0
        
        while (queue.isNotEmpty() && iteration < iterations) {
            val currentLevelSize = queue.size
            
            repeat(currentLevelSize) {
                if (queue.isEmpty()) return@repeat
                
                val (x, y) = queue.poll()
                workingImage.setRGB(x, y, newColor)
                processedPixels++
                
                // Add neighboring pixels
                val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
                for ((dx, dy) in directions) {
                    val nx = x + dx
                    val ny = y + dy
                    
                    if (nx in 0 until workingImage.width && 
                        ny in 0 until workingImage.height &&
                        !visited[nx][ny] && 
                        workingImage.getRGB(nx, ny) == targetColor) {
                        
                        visited[nx][ny] = true
                        queue.offer(nx to ny)
                    }
                }
            }
            
            iteration++
            val progress = iteration.toFloat() / iterations
            
            withContext(Dispatchers.Swing) {
                onProgress(progress, workingImage.toComposeImageBitmap())
            }
            
            if (animationPause > 0) {
                delay(animationPause.toLong())
            }
        }
        
        println("BFS completed: processed $processedPixels pixels in $iteration iterations")
        workingImage.toComposeImageBitmap()
    }
    
    suspend fun paintDFS(onProgress: (Float, ImageBitmap) -> Unit): ImageBitmap = withContext(Dispatchers.IO) {
        println("Starting DFS painting from (${startPoint.x.toInt()}, ${startPoint.y.toInt()})")
        
        val originalImage = ImageIO.read(java.io.File(imagePath))
        val workingImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_RGB)
        val graphics = workingImage.createGraphics()
        graphics.drawImage(originalImage, 0, 0, null)
        graphics.dispose()
        
        val visited = Array(workingImage.width) { BooleanArray(workingImage.height) }
        val stack: Stack<Pair<Int, Int>> = Stack()
        val targetColor = workingImage.getRGB(startPoint.x.toInt(), startPoint.y.toInt())
        val newColor = penColor.toArgb()
        
        println("Target color: ${Integer.toHexString(targetColor)}, New color: ${Integer.toHexString(newColor)}")
        
        if (targetColor == newColor) {
            println("Start point color is same as pen color, no changes needed")
            return@withContext workingImage.toComposeImageBitmap()
        }
        
        stack.push(startPoint.x.toInt() to startPoint.y.toInt())
        
        var processedPixels = 0
        var iteration = 0
        
        while (stack.isNotEmpty() && iteration < iterations) {
            val (x, y) = stack.pop()
            
            if (x !in 0 until workingImage.width || 
                y !in 0 until workingImage.height ||
                visited[x][y] || 
                workingImage.getRGB(x, y) != targetColor) {
                continue
            }
            
            visited[x][y] = true
            workingImage.setRGB(x, y, newColor)
            processedPixels++
            
            // Add neighboring pixels
            val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            for ((dx, dy) in directions) {
                val nx = x + dx
                val ny = y + dy
                stack.push(nx to ny)
            }
            
            if (processedPixels % 50 == 0) {
                iteration++
                val progress = iteration.toFloat() / iterations
                
                withContext(Dispatchers.Swing) {
                    onProgress(progress, workingImage.toComposeImageBitmap())
                }
                
                if (animationPause > 0) {
                    delay(animationPause.toLong())
                }
            }
        }
        
        println("DFS completed: processed $processedPixels pixels in $iteration iterations")
        workingImage.toComposeImageBitmap()
    }
}
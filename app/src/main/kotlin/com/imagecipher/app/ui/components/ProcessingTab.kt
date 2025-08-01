package com.imagecipher.app.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.imagecipher.app.ui.viewmodel.ImageCipherViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun ProcessingTab(
    viewModel: ImageCipherViewModel,
    modifier: Modifier = Modifier
) {
    var showPixelTraversal by remember { mutableStateOf(false) }
    var selectedAlgorithm by remember { mutableStateOf("BFS") }
    var animationPause by remember { mutableStateOf("0") }
    var iterations by remember { mutableStateOf("10") }
    var selectedColor by remember { mutableStateOf(Color.Red) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Before/After Images
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Image Processing Result",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Before Image (Clickable for start point selection)
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Before ${if (showPixelTraversal) "(Click to select start point)" else ""}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        var imageSize by remember { mutableStateOf(IntSize.Zero) }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    2.dp,
                                    if (showPixelTraversal && viewModel.selectedStartPoint != null) 
                                        MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            viewModel.originalImage?.let { image ->
                                Image(
                                    bitmap = image,
                                    contentDescription = "Original image - click to select start point",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .onSizeChanged { imageSize = it }
                                        .pointerInput(showPixelTraversal) {
                                            if (showPixelTraversal) {
                                                detectTapGestures { offset ->
                                                    // Convert screen coordinates to image coordinates
                                                    val imageRatio = image.width.toFloat() / image.height.toFloat()
                                                    val displayRatio = imageSize.width.toFloat() / imageSize.height.toFloat()
                                                    
                                                    val (imageX, imageY) = if (imageRatio > displayRatio) {
                                                        // Image is wider - letter-boxed top/bottom
                                                        val scaledHeight = imageSize.width / imageRatio
                                                        val offsetY = (imageSize.height - scaledHeight) / 2
                                                        val relativeY = (offset.y - offsetY) / scaledHeight
                                                        offset.x / imageSize.width to relativeY
                                                    } else {
                                                        // Image is taller - letter-boxed left/right
                                                        val scaledWidth = imageSize.height * imageRatio
                                                        val offsetX = (imageSize.width - scaledWidth) / 2
                                                        val relativeX = (offset.x - offsetX) / scaledWidth
                                                        relativeX to offset.y / imageSize.height
                                                    }
                                                    
                                                    if (imageX in 0f..1f && imageY in 0f..1f) {
                                                        val actualX = imageX * image.width
                                                        val actualY = imageY * image.height
                                                        viewModel.setStartPoint(Offset(actualX, actualY))
                                                    }
                                                }
                                            }
                                        },
                                    contentScale = ContentScale.Fit
                                )
                                
                                // Show start point indicator
                                if (showPixelTraversal && viewModel.selectedStartPoint != null) {
                                    val startPoint = viewModel.selectedStartPoint!!
                                    val relativeX = startPoint.x / image.width
                                    val relativeY = startPoint.y / image.height
                                    
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .wrapContentSize(Alignment.TopStart)
                                            .offset(
                                                x = with(LocalDensity.current) { (relativeX * imageSize.width).toDp() },
                                                y = with(LocalDensity.current) { (relativeY * imageSize.height).toDp() }
                                            )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(
                                                    Color.Red,
                                                    shape = androidx.compose.foundation.shape.CircleShape
                                                )
                                                .border(
                                                    2.dp,
                                                    Color.White,
                                                    shape = androidx.compose.foundation.shape.CircleShape
                                                )
                                        )
                                    }
                                }
                            } ?: run {
                                Text(
                                    text = "No image loaded",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // After Image
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "After",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            viewModel.processedImage?.let { image ->
                                Image(
                                    bitmap = image,
                                    contentDescription = "Processed image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            } ?: run {
                                Text(
                                    text = "No processed image",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Processing Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Processing Controls",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showPixelTraversal = !showPixelTraversal },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (showPixelTraversal) "Hide Pixel Traversal" else "Show Pixel Traversal")
                    }
                    
                    Button(
                        onClick = {
                            viewModel.processedImage?.let { image ->
                                val dialog = FileDialog(Frame(), "Save Processed Image", FileDialog.SAVE)
                                dialog.file = "processed_image.png"
                                dialog.isVisible = true
                                
                                if (dialog.file != null) {
                                    val file = File(dialog.directory, dialog.file)
                                    // TODO: Convert ImageBitmap to BufferedImage and save
                                    // This would require additional conversion logic
                                }
                            }
                        },
                        enabled = viewModel.processedImage != null
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save")
                    }
                }
            }
        }
        
        // Pixel Traversal Controls (expandable)
        if (showPixelTraversal) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Pixel Traversal Visualization",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Algorithm Selection
                    Text(
                        text = "Traversal Algorithm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOf("BFS" to "Breadth First Search", "DFS" to "Depth First Search").forEach { (value, label) ->
                            Row(
                                modifier = Modifier
                                    .selectable(
                                        selected = selectedAlgorithm == value,
                                        onClick = { selectedAlgorithm = value },
                                        role = Role.RadioButton
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedAlgorithm == value,
                                    onClick = null
                                )
                                Text(
                                    text = label,
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    HorizontalDivider()
                    
                    // Animation Settings
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = animationPause,
                            onValueChange = { animationPause = it },
                            label = { Text("Pause (ms)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = iterations,
                            onValueChange = { iterations = it },
                            label = { Text("Iterations") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Color Selection
                    Text(
                        text = "Pen Color",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Color.Red to "Red",
                            Color.Green to "Green", 
                            Color.Blue to "Blue",
                            Color.Yellow to "Yellow"
                        ).forEach { (color, name) ->
                            FilterChip(
                                onClick = { selectedColor = color },
                                label = { Text(name) },
                                selected = selectedColor == color
                            )
                        }
                    }
                    
                    HorizontalDivider()
                    
                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val pauseTime = animationPause.toIntOrNull() ?: 0
                                val iterationCount = iterations.toIntOrNull() ?: 10
                                viewModel.runPixelTraversal(
                                    algorithm = selectedAlgorithm,
                                    animationPause = pauseTime.coerceAtLeast(0),
                                    iterations = iterationCount.coerceAtLeast(1),
                                    penColor = selectedColor
                                )
                            },
                            modifier = Modifier.weight(1f),
                            enabled = viewModel.originalImage != null && 
                                     viewModel.selectedStartPoint != null &&
                                     !viewModel.isTraversalRunning
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Run $selectedAlgorithm")
                        }
                        
                        Button(
                            onClick = { viewModel.resetTraversal() },
                            enabled = !viewModel.isTraversalRunning
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset")
                        }
                    }
                    
                    // Progress indicator
                    if (viewModel.isTraversalRunning) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Running $selectedAlgorithm traversal...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            LinearProgressIndicator(
                                progress = { viewModel.traversalProgress },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    
                    // Start point info
                    viewModel.selectedStartPoint?.let { point ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = "Start point: (${point.x.toInt()}, ${point.y.toInt()})",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
        
        // Processing Status
        if (viewModel.outputText.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Processing Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = viewModel.outputText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
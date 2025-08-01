package com.imagecipher.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imagecipher.app.ui.components.ImageTab
import com.imagecipher.app.ui.components.ProcessingTab
import com.imagecipher.app.ui.viewmodel.ImageCipherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: ImageCipherViewModel = remember { ImageCipherViewModel() }
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val tabs = listOf("Image & Encryption", "Processing")
    
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (selectedTabIndex) {
            0 -> ImageTab(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
            1 -> ProcessingTab(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
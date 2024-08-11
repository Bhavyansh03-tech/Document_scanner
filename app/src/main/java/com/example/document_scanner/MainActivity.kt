package com.example.document_scanner

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.document_scanner.ui.theme.DocumentScannerTheme
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocumentScannerTheme {

                val options = GmsDocumentScannerOptions.Builder()
                    .setGalleryImportAllowed(false)
                    .setResultFormats(
                        GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                        GmsDocumentScannerOptions.RESULT_FORMAT_PDF
                    )
                    .setPageLimit(2)
                    .setScannerMode(SCANNER_MODE_FULL)
                    .build()

                var selectedImageUri by remember {
                    mutableStateOf(Uri.EMPTY)
                }

                val scanner = GmsDocumentScanning.getClient(options)

                val scannerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult()
                ) { result ->
                    if (result.resultCode == RESULT_OK) {
                        val scannerResult =
                            GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                        scannerResult?.pages?.let { pages ->
                            for (page in pages) {
                                selectedImageUri = pages[0].imageUri
                            }
                        }
                    }
                }

                // Context
                val context = LocalContext.current as Activity

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop, modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                     Button(onClick = {
                         scanner.getStartScanIntent(context)
                             .addOnSuccessListener { intentSender ->
                                 scannerLauncher.launch(
                                     IntentSenderRequest
                                         .Builder(intentSender)
                                         .build()
                                 )
                             }
                     }) {
                         Text(text = "Start Scanning")
                     }
                }
            }
        }
    }
}
# Document Scanner with Google ML Kit and Jetpack Compose
Integrate document scanning functionality into your Android app using Google ML Kit and Jetpack Compose.

## Features
- Scans documents using Google ML Kit's Document Scanner.
- Supports JPEG and PDF formats.
- Customizable scanner options (page limit, scanner mode, etc.).
- Simple and user-friendly UI with Jetpack Compose.

## Screenshot
<img src="https://github.com/user-attachments/assets/ad4eaa67-ced9-4031-9336-bf5674829469" alt="First Screenshot" style="width: 200px; height: auto; margin-right: 10px;">
<img src="https://github.com/user-attachments/assets/c13d8917-60c0-4e4c-8f2c-c54da33a9051" alt="Second Screenshot" style="width: 200px; height: auto; margin-right: 10px;">
<img src="https://github.com/user-attachments/assets/230b31f9-ee2d-435a-bbaf-ecca58c34fee" alt="Third Screenshot" style="width: 200px; height: auto;">
  
## Getting Started
### Installation

1. Clone the repository:

   ```sh
     git clone https://github.com/Bhavyansh03-tech/Document_scanner.git
   ```
   
2. Open the project in Android Studio.
3. Build the project and run it on an emulator or a physical device.

### Setup
1. **Dependencies**
Add the following dependencies to your `build.gradle` file:
  ```kotlin
    implementation 'com.google.mlkit:document-scanner:16.0.0-beta1'
    implementation 'io.coil-kt:coil-compose:2.7.0'
  ```

2. **Usage**
Here's the code to implement the document scanner:
```kotlin
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
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Create a new Pull Request.

## Contact

For questions or feedback, please contact [@Bhavyansh03-tech](https://github.com/Bhavyansh03-tech).

---

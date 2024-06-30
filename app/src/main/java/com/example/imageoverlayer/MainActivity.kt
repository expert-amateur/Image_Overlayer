package com.example.imageoverlayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.graphicsLayer
import coil.compose.rememberAsyncImagePainter
import kotlin.math.roundToInt
import com.example.imageoverlayer.ui.theme.ImageOverlayerTheme

data class ImageState(
    var offsetX: Float = 0f,
    var offsetY: Float = 0f,
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var rotation: Float = 0f
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageOverlayerTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current

    var imageUri1 by remember { mutableStateOf<Uri?>(null) }
    var imageUri2 by remember { mutableStateOf<Uri?>(null) }
    var showSliders1 by remember { mutableStateOf(false) }
    var showSliders2 by remember { mutableStateOf(false) }
    var aspectRatioX1 by remember { mutableStateOf(16f) }
    var aspectRatioY1 by remember { mutableStateOf(9f) }
    var aspectRatioX2 by remember { mutableStateOf(16f) }
    var aspectRatioY2 by remember { mutableStateOf(9f) }

    val launcher1 = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        imageUri1 = uri
    }

    val launcher2 = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        imageUri2 = uri
    }

    val imageState1 = remember { mutableStateOf(ImageState()) }
    val imageState2 = remember { mutableStateOf(ImageState()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Row {
            Button(onClick = { launcher1.launch(arrayOf("image/*")) }) {
                Text("Open Gallery 1")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { launcher2.launch(arrayOf("image/*")) }) {
                Text("Open Gallery 2")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            imageUri1?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = null,
                    modifier = Modifier
                        .offset { IntOffset(imageState1.value.offsetX.roundToInt(), imageState1.value.offsetY.roundToInt()) }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, rotation ->
                                imageState1.value = imageState1.value.copy(
                                    offsetX = imageState1.value.offsetX + pan.x,
                                    offsetY = imageState1.value.offsetY + pan.y,
                                    scaleX = imageState1.value.scaleX * zoom,
                                    scaleY = imageState1.value.scaleY * zoom,
                                    rotation = imageState1.value.rotation + rotation
                                )
                            }
                        }
                        .graphicsLayer {
                            scaleX = imageState1.value.scaleX
                            scaleY = imageState1.value.scaleY
                            rotationZ = imageState1.value.rotation // Apply the rotation
                        }
                        .clickable {
                            showSliders1 = true
                            showSliders2 = false
                        }
                        .aspectRatio(aspectRatioX1 / aspectRatioY1),
                    contentScale = ContentScale.Crop
                )
            }

            imageUri2?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = null,
                    modifier = Modifier
                        .offset { IntOffset(imageState2.value.offsetX.roundToInt(), imageState2.value.offsetY.roundToInt()) }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, rotation ->
                                imageState2.value = imageState2.value.copy(
                                    offsetX = imageState2.value.offsetX + pan.x,
                                    offsetY = imageState2.value.offsetY + pan.y,
                                    scaleX = imageState2.value.scaleX * zoom,
                                    scaleY = imageState2.value.scaleY * zoom,
                                    rotation = imageState2.value.rotation + rotation
                                )
                            }
                        }
                        .graphicsLayer {
                            scaleX = imageState2.value.scaleX
                            scaleY = imageState2.value.scaleY
                            rotationZ = imageState2.value.rotation // Apply the rotation
                        }
                        .clickable {
                            showSliders1 = false
                            showSliders2 = true
                        }
                        .aspectRatio(aspectRatioX2 / aspectRatioY2),
                    contentScale = ContentScale.Crop
                )
            }

            if (showSliders1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text("Aspect Ratio X: ${aspectRatioX1.roundToInt()}")
                    Slider(
                        value = aspectRatioX1,
                        onValueChange = { aspectRatioX1 = it },
                        valueRange = 1f..100f
                    )
                    Text("Aspect Ratio Y: ${aspectRatioY1.roundToInt()}")
                    Slider(
                        value = aspectRatioY1,
                        onValueChange = { aspectRatioY1 = it },
                        valueRange = 1f..100f
                    )
                }
            }

            if (showSliders2) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text("Aspect Ratio X: ${aspectRatioX2.roundToInt()}")
                    Slider(
                        value = aspectRatioX2,
                        onValueChange = { aspectRatioX2 = it },
                        valueRange = 1f..100f
                    )
                    Text("Aspect Ratio Y: ${aspectRatioY2.roundToInt()}")
                    Slider(
                        value = aspectRatioY2,
                        onValueChange = { aspectRatioY2 = it },
                        valueRange = 1f..100f
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageOverlayerTheme {
        MyApp()
    }
}

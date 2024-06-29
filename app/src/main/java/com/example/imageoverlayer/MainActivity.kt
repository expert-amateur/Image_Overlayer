package com.example.imageoverlayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        imageUri = uri
    }

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotationState by remember { mutableFloatStateOf(0f) } // State to keep track of rotation
    var scaleXState by remember { mutableFloatStateOf(1f) } // State to keep track of scaleX
    var scaleYState by remember { mutableFloatStateOf(1f) } // State to keep track of scaleY

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Button(onClick = { launcher.launch(arrayOf("image/*")) }) {
            Text("Open Gallery")
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rotation ->
                            offsetX += pan.x
                            offsetY += pan.y
                            scaleXState *= zoom // Update the scaleX state
                            scaleYState *= zoom // Update the scaleY state
                            rotationState += rotation // Update the rotation state
                        }
                    }
                    .graphicsLayer {
                        scaleX = scaleXState
                        scaleY = scaleYState
                        rotationZ = rotationState // Apply the rotation
                    },
                contentScale = ContentScale.Crop
            )
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

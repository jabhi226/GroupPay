package com.example.grouppay.ui.features.addParticipant.view

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.grouppay.R
import com.example.grouppay.domain.GroupMember
import com.example.grouppay.ui.Testing
import com.example.grouppay.ui.features.addParticipant.viewModel.AddParticipantViewModel
import com.example.grouppay.ui.features.core.view.components.CommonButton
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.utils.showToast
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantDetailsScreen(
    navController: NavController = rememberNavController(),
    groupId: String?,
    participantId: String?
) {

    val viewModel: AddParticipantViewModel = koinViewModel()
    val groupMember by viewModel.groupMember.collectAsState(null)
    val state by viewModel.uiEvents.collectAsState(null)
    val context = LocalContext.current

    LaunchedEffect(participantId) {
        viewModel.getParticipantDetails(participantId)
    }

    LaunchedEffect(state) {
        when (state) {
            AddParticipantViewModel.UiEvents.ShowSuccess -> {
                context.showToast("Group member ${groupMember?.name} added.")
                navController.navigateUp()
            }

            is AddParticipantViewModel.UiEvents.ShowError -> {
                context.showToast((state as AddParticipantViewModel.UiEvents.ShowError).errorMessage)
            }

            null -> {}
        }

    }

    GroupPayTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 40.dp),
                    onClick = {
                        viewModel.saveNewParticipantInTheGroup(groupId, groupMember)
                    }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_user),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "add_member"
                        )
                        CommonText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Save Member",
                            textColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            topBar = {
                TopAppBar(title = {
                    CommonText(
                        text = "Add Group Member",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                })
            }
        ) { innerPadding ->
            DetailsScreen(modifier = Modifier.padding(innerPadding)) {
                viewModel.updateGroupMemberName(it)
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    groupMember: GroupMember = Testing.getParticipent(),
    updateGroupMemberName: (String) -> Unit = {}
) {

    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    // For opening Gallery (Scoped Storage)
    val openGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profileImageUri = uri
        }
    )

    // For opening Camera (Scoped Storage)
    val openCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                context.showToast("Image Captured!")
            }
        }
    )

    // Show the bottom sheet when button is clicked
    val scaffoldState = rememberBottomSheetScaffoldState()

    if (isBottomSheetVisible) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Button to open Gallery
                    CommonButton(
                        text = "Select Profile Image from Gallery",
                        onClick = {
                            openGalleryLauncher.launch("image/*")
                            isBottomSheetVisible = false // Close bottom sheet
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to open Camera
                    CommonButton(
                        text = "Capture Profile Image with Camera",
                        onClick = {
                            val photoUri = createImageUri(context)
                            // Launch camera intent to take a picture
                            openCameraLauncher.launch(photoUri)
                            isBottomSheetVisible = false // Close bottom sheet
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            sheetPeekHeight = 0.dp // Hide the bottom sheet peek
        ) {}
    }

    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3F)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {

                val profile = createRef()
                val profileEdit = createRef()

                Image(
                    painter = if (profileImageUri == null) {
                        painterResource(R.drawable.ic_add_user)
                    } else {
                        rememberAsyncImagePainter(model = profileImageUri)
                    },
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .size(200.dp)
                        .background(color = MaterialTheme.colorScheme.secondary)
                        .constrainAs(ref = profile, constrainBlock = {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                )
                Image(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(color = MaterialTheme.colorScheme.secondary)
                        .size(48.dp)
                        .padding(8.dp)
                        .constrainAs(ref = profileEdit, constrainBlock = {
                            bottom.linkTo(profile.bottom)
                            end.linkTo(profile.end)
                        })
                        .clickable {
                            isBottomSheetVisible = true
                        },
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2F)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            Color.Transparent
                        ),
                        tileMode = TileMode.Repeated
                    )
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            DetailBox()
            DetailBox()
        }
        CommonOutlinedTextField(
            modifier = Modifier.padding(16.dp),
            text = groupMember.name,
            hint = "Group member name",
            updateText = {
                updateGroupMemberName(it)
            }
        )
    }
}

@Composable
fun DetailBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(120.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonText(text = "Balance", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(16.dp))
            CommonText(text = "123", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }
    }
}


fun createImageUri(context: Context): Uri {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ProfileImages") // Scoped directory
    }

    // Insert into the MediaStore to create the URI
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: Uri.EMPTY
}
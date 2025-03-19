package com.example.grouppay.ui.features.participantDetails.view

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grouppay.R
import com.example.grouppay.domain.entities.GroupMember
import com.example.grouppay.ui.features.participantDetails.viewModel.ParticipantDetailsViewModel
import com.example.grouppay.ui.features.core.view.components.CommonButton
import com.example.grouppay.ui.features.core.view.components.CommonText
import com.example.grouppay.ui.features.core.view.components.CommonOutlinedTextField
import com.example.grouppay.ui.features.utils.showToast
import com.example.grouppay.ui.theme.GroupPayTheme
import org.koin.androidx.compose.koinViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import coil3.compose.AsyncImage

@Composable
fun ParticipantDetailsScreen(
    navController: NavController = rememberNavController(),
    groupId: String?,
    participantId: String?
) {

    val viewModel: ParticipantDetailsViewModel = koinViewModel()
    val groupMember by viewModel.groupMember.collectAsState(null)
    val state by viewModel.uiEvents.collectAsState(null)
    val context = LocalContext.current

    LaunchedEffect(participantId, groupId) {
        viewModel.getParticipantDetails(participantId, groupId)
    }

    LaunchedEffect(state) {
        when (state) {
            ParticipantDetailsViewModel.UiEvents.ShowSuccess -> {
                context.showToast("Group member ${groupMember?.name} added.")
                navController.navigateUp()
            }

            is ParticipantDetailsViewModel.UiEvents.ShowError -> {
                context.showToast((state as ParticipantDetailsViewModel.UiEvents.ShowError).errorMessage)
            }

            null -> {}
        }

    }

    GroupPayTheme {
        DetailsScreen(
            groupMember = groupMember,
            updateGroupMemberName = {
                viewModel.updateGroupMemberName(it)
            },
            updateGroupMemberProfileUri = {
                viewModel.updateGroupMemberProfileUri(it)
            }
        ) {
            viewModel.saveNewParticipantInTheGroup(
                groupId,
                groupMember
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    groupMember: GroupMember?,
    updateGroupMemberName: (String) -> Unit = {},
    updateGroupMemberProfileUri: (String) -> Unit = {},
    onFabClicked: () -> Unit = {}
) {

    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    LaunchedEffect(groupMember?.profilePictureUriPath) {
        println("===> ${groupMember?.profilePictureUriPath}")
        profileImageUri = groupMember?.profilePictureUriPath?.let {
            Uri.parse(
                it
            )
        }
        println("===> $profileImageUri")
    }

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Hidden && isBottomSheetVisible) {
            isBottomSheetVisible = false
        }
    }

    val openGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profileImageUri = uri
            uri?.let {
                updateGroupMemberProfileUri(it.toString())
            }
        }
    )

    val openCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            profileImageUri = if (success) {
                photoUri?.let {
                    updateGroupMemberProfileUri(it.toString())
                }
                photoUri
            } else {
                null
            }
        }
    )

    fun openCamera() {
        photoUri = createImageUri(context)
        photoUri?.let {
            openCameraLauncher.launch(it)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                context.showToast("Camera permission denied")
            }
        }
    )

    fun checkAndOpenCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                CommonButton(
                    text = "Select Profile Image from Gallery",
                    onClick = {
                        openGalleryLauncher.launch("image/*")
                        isBottomSheetVisible = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CommonButton(
                    text = "Capture Profile Image with Camera",
                    onClick = {
                        checkAndOpenCameraPermission()
                        isBottomSheetVisible = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 40.dp),
                    onClick = {
                        onFabClicked()
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
            Column(
                modifier = modifier.padding(innerPadding),
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

                        if (profileImageUri == null) {
                            Image(
                                painter = painterResource(R.drawable.ic_add_user),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(top = 40.dp)
                                    .clip(RoundedCornerShape(100.dp))
                                    .size(200.dp)
                                    .background(color = MaterialTheme.colorScheme.primary)
                                    .constrainAs(ref = profile, constrainBlock = {
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    })

                            )
                        } else {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 40.dp)
                                    .clip(RoundedCornerShape(100.dp))
                                    .size(200.dp)
                                    .constrainAs(ref = profile, constrainBlock = {
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Image(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .clickable {
                                    isBottomSheetVisible = true
                                }
                                .background(color = MaterialTheme.colorScheme.primary)
                                .size(48.dp)
                                .padding(8.dp)
                                .constrainAs(ref = profileEdit, constrainBlock = {
                                    bottom.linkTo(profile.bottom)
                                    end.linkTo(profile.end)
                                }),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                    DetailBox(
                        title = "Expenses",
                        value = groupMember?.amountBorrowedFromGroup.toString(),
                        valueColorResourceId = groupMember?.amountBorrowedFromGroup.getColorOrBlack(
                            R.color.amount_red
                        )
                    )
                    DetailBox(
                        title = "Payments",
                        value = groupMember?.amountOwedFromGroup.toString(),
                        valueColorResourceId = groupMember?.amountOwedFromGroup.getColorOrBlack(
                            R.color.amount_green
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    DetailBox(
                        title = "Returned",
                        value = groupMember?.amountReturnedToOwner.toString(),
                        valueColorResourceId = groupMember?.amountReturnedToOwner.getColorOrBlack(
                            R.color.amount_green
                        )
                    )
                    DetailBox(
                        title = "Received",
                        value = groupMember?.amountReceivedFromBorrower.toString(),
                        valueColorResourceId = groupMember?.amountReceivedFromBorrower.getColorOrBlack(
                            R.color.amount_red
                        )
                    )
                }
                CommonOutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    text = groupMember?.name ?: "",
                    hint = "Group member name",
                    updateText = {
                        updateGroupMemberName(it)
                    }
                )
            }
        }
    }

}

@Composable
fun Double?.getColorOrBlack(color: Int): Color {
    return if (this != 0.0) {
        colorResource(id = color)
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
}

@Composable
fun DetailBox(
    modifier: Modifier = Modifier,
    title: String = "Balance",
    value: String = "123",
    valueColorResourceId: Color
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .size(width = 160.dp, height = 120.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
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
            CommonText(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonText(
                text = "₹$value",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textColor = valueColorResourceId
            )
        }
    }
}


fun createImageUri(context: Context): Uri {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GroupPay/ProfileImages")
    }

    // Insert into the MediaStore to create the URI
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: Uri.EMPTY
}
package com.example.myclinic.screens.HomeScreenChilds

import android.net.Uri
import android.util.Log
import coil.compose.rememberImagePainter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myclinic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

val appointments = mutableListOf<Appointment>()

data class Appointment(
    val date: String,
    val time: String,
    val doctorId: String,
    val doctorSpecialty: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileScreen() {

    var patientName by remember { mutableStateOf<String?>(null) }
    var patientEmail by remember { mutableStateOf<String?>(null) }
    var patientDate by remember { mutableStateOf<String?>(null) }
    var patientProfileImageUrl by remember { mutableStateOf<Uri?>(null) }
    var changeFields by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var appointments by remember { mutableStateOf<List<Appointment>>(emptyList()) }
    val firestore = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val userRef = firestore.collection("Patients").document(userId!!)
    var isLoadCards by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                patientName = document.getString("name")
                patientDate = document.getString("birthDate")
                patientEmail = document.getString("email")
                val profileImageUrl = document.getString("profileImageUrl")
                Log.d("ProfileScreen", "profileImageUrl from Firestore: $profileImageUrl")
                patientProfileImageUrl = profileImageUrl?.let { Uri.parse(it) }
                Log.d(
                    "ProfileScreen",
                    "User data loaded successfully: $patientName, $patientDate, $patientEmail, $patientProfileImageUrl"
                )

            } else {
                Log.d("ProfileScreen", "User data not found, initializing new user data")

                val userProfile = hashMapOf(
                    "name" to "",
                    "birthDate" to "",
                    "email" to "",
                    "profileImageUrl" to ""
                )
                userRef.set(userProfile)
            }

        }
        val fetchedAppointments = fetchAppointments(userId)
        val appointmentsWithSpecialties = fetchedAppointments.map { appointment ->
            val specialty = fetchDoctorInfo(appointment.doctorId)
            appointment.copy(doctorSpecialty = specialty)
        }
        loadAppointments(userId)
        appointments = appointmentsWithSpecialties
        isLoadCards = false
    }
    LaunchedEffect(appointments) {
        if (appointments.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(32.dp)
            )
            Text(
                text = "Ваш профиль",
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                fontSize = 34.sp,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings Icon",
                tint = colorResource(id = R.color.authorization_mark),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.LightLightGray))
        )
        Spacer(modifier = Modifier.height(30.dp))
        SelectImageFromGallery(
            selectedImageUri = patientProfileImageUrl,
            onImageSelected = { uri ->
                patientProfileImageUrl = uri
                Log.d("SelectImageFromGallery Call", "Uri is null: uri = $uri")
                patientProfileImageUrl?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (uploadImageToFirebaseStorage(it, userRef)) {
                            Log.d("uploadImageToFirebaseStorage", "Succesful")
                        } else {
                            Log.d("uploadImageToFirebaseStorage", "Failure")
                        }
                    }
                }
            }
        )


        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            enabled = changeFields,
            value = if (patientName != null) patientName!! else "",
            onValueChange = { patientName = it },
            label = { Text("Имя", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            placeholder = { Text(text = patientName!!, color = Color.Black) },
            colors = TextFieldDefaults.colors
                (
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray
            )
        )
        TextField(
            enabled = changeFields,
            value = if (patientEmail != null) patientEmail!! else "",
            onValueChange = { patientEmail = it },
            label = { Text("Почта", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            placeholder = { Text(text = patientEmail!!, color = Color.Black) },
            colors = TextFieldDefaults.colors
                (
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray
            )
        )
        TextField(
            enabled = changeFields,
            value = if (patientDate != null) patientDate!! else "",
            onValueChange = { patientDate = it },
            label = { Text("Дата рождения", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            placeholder = { Text(text = patientDate!!, color = Color.Black) },
            colors = TextFieldDefaults.colors
                (
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (changeFields) {
                androidx.compose.material3.Button(
                    onClick = {
                        changeFields = false
                        updateInfo(userRef, patientName!!, patientDate!!, patientEmail!!)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.authorization_mark),
                            RoundedCornerShape(15.dp)
                        ),
                    shape = RoundedCornerShape(15.dp),


                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.authorization_mark),
                        contentColor = Color.White,

                        )
                ) {
                    Text("Сохранить")
                }
                androidx.compose.material3.Button(
                    onClick = {
                        changeFields = false
                    },
                    modifier = Modifier
                        .width(210.dp)
                        .height(55.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.dp,
                            Color.LightGray,
                            RoundedCornerShape(15.dp)
                        ),
                    shape = RoundedCornerShape(15.dp),


                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.LightGray
                    )
                ) {
                    Text("Отменить")
                }
            } else {
                androidx.compose.material3.Button(
                    onClick = {
                        changeFields = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.authorization_mark),
                            RoundedCornerShape(15.dp)
                        ),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = colorResource(id = R.color.authorization_mark)
                    )
                ) {
                    Text(
                        text = "Внести изменения",
                        color = colorResource(id = R.color.authorization_mark),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(22.dp))
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ваши записи:",
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 16.sp
            )
            if (isLoadCards) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Загрузка...",
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = colorResource(id = R.color.authorization_mark)
                )
            } else {
                Log.d("ProfileScreen", "Displaying appointments, total: ${appointments.size}")

                appointments.forEach { appointment ->
                    Log.d(
                        "ProfileScreen",
                        "Displaying appointment: ${appointment.date}, ${appointment.time}, ${appointment.doctorSpecialty}"
                    )

                    AppointmentCard(appointment)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

    }
}

fun updateInfo(
    userRef: DocumentReference,
    patientName: String,
    patientDate: String,
    patientEmail: String
): Boolean {
    return try {
        userRef.get().addOnSuccessListener { document ->
            val userProfile = hashMapOf(
                "name" to patientName,
                "birthDate" to patientDate,
                "email" to patientEmail,
                "profileImageUrl" to ""
            )
            userRef.set(userProfile)
            Log.d("updateInfo", "User info updated successfully")

        }

        true
    } catch (e: Exception) {
        Log.d("updateInfo", "$e")
        false
    }
}

suspend fun loadAppointments(patientID: String) {
    Log.d("loadAppointments", "Loading appointments for patientID: $patientID")

    val fetchedAppointments = fetchAppointments(patientID)
    Log.d("loadAppointments", "Fetched ${fetchedAppointments.size} appointments")


    fetchedAppointments.forEach { appointment ->
        val specialty = fetchDoctorInfo(appointment.doctorId)
        appointments.add(appointment.copy(doctorSpecialty = specialty))
        Log.d(
            "loadAppointments",
            "Appointment added: ${appointment.date}, ${appointment.time}, specialty: $specialty"
        )


    }
    Log.d("LoadAppointments", "finished")
}

suspend fun fetchAppointments(patientID: String): List<Appointment> {
    val db = FirebaseFirestore.getInstance()
    val appointments = mutableListOf<Appointment>()

    try {
        Log.d("fetchAppointments", "Fetching appointments from Firestore... patientID = $patientID")

        val snapshot = db.collectionGroup("DoctorAppointmentCalendar").get().await()

        for (document in snapshot.documents) {
            for (timeSlot in document.data?.entries ?: emptySet()) {
                val time = timeSlot.key
                val appointmentMap = timeSlot.value as? Map<*, *>

                if (appointmentMap != null) {
                    val appointmentPatientID = appointmentMap["patientID"] as? String
                    val isBooked = appointmentMap["booked"] as? Boolean

                    if (appointmentPatientID == patientID && isBooked == true) {
                        val date = document.id
                        val doctorId = document.reference.parent.parent?.id
                        Log.d(
                            "fetchAppointments",
                            "Found matching appointment: $date, $time, $doctorId"
                        )
                        appointments.add(Appointment(date, time, doctorId!!))
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("fetchAppointments", "Error fetching documents: ", e)
    }

    return appointments
}


suspend fun fetchDoctorInfo(doctorId: String): String? {
    val db = FirebaseFirestore.getInstance()
    return try {
        val doctorDoc = db.collection("Doctors").document(doctorId).get().await()
        val specialty = doctorDoc.getString("Профессия")
        Log.d("fetchDoctorInfo", "Fetched doctor info: $doctorId, specialty: $specialty")
        specialty
    } catch (e: Exception) {
        Log.e("fetchDoctorInfo", "Error fetching doctor info: $e")
        null
    }
}

enum class DeletionState {
    Idle, Loading, Deleted, Error
}

@Composable
fun AppointmentCard(appointment: Appointment) {
    var deletionState by remember { mutableStateOf(DeletionState.Idle) }

    Log.d(
        "AppointmentCard",
        "Creating card for appointment: ${appointment.date}, ${appointment.time}, ${appointment.doctorSpecialty}"
    )

    suspend fun deleteAppointment(doctorId: String) {
        val db = FirebaseFirestore.getInstance()
        val dateRef = db.collection("Doctors")
            .document(doctorId)
            .collection("DoctorAppointmentCalendar")
            .document(appointment.date)
        try {
            dateRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    dateRef.update(
                        mapOf(
                            "${appointment.time}.patientID" to null,
                            "${appointment.time}.booked" to false
                        )
                    )
                    Log.d(
                        "deleteAppointment",
                        "Appointment updated successfully: ${appointment.date}, time: ${appointment.time}, doctorID= ${appointment.doctorId}"
                    )

                    deletionState = DeletionState.Deleted
                } else {
                    Log.d(
                        "deleteAppointment",
                        "Document not exist: ${appointment.date}, time: ${appointment.time}"
                    )
                }
            }.await()
        } catch (e: Exception) {
            Log.d("deleteAppointment", "Error deleting appointment: $e")
            deletionState = DeletionState.Error
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .wrapContentHeight()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
    ) {
        when (deletionState) {
            DeletionState.Idle -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(16.dp)
                            .background(Color.White)
                    ) {
                        Text(
                            text = "Дата: ${appointment.date}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Время: ${appointment.time}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Врач: ${appointment.doctorSpecialty ?: "Неизвестно"}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(25.dp))
                    Column(
                        modifier = Modifier
                            .height(105.dp)
                            .wrapContentWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            Modifier
                                .clickable {
                                    deletionState = DeletionState.Loading
                                    CoroutineScope(Dispatchers.IO).launch {
                                        deleteAppointment(appointment.doctorId)
                                    }
                                }
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(
                                    2.dp,
                                    colorResource(id = R.color.authorization_mark),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp),
                                painter = painterResource(id = R.drawable.closeicon),
                                contentDescription = "Close Icon",
                                tint = colorResource(id = R.color.authorization_mark),
                            )
                        }
                    }
                }
            }

            DeletionState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Загрузка...",
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator(
                        modifier = Modifier.size(54.dp),
                        color = colorResource(id = R.color.authorization_mark)
                    )
                }
            }

            DeletionState.Deleted -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ваша запись удалена",
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 20.sp
                    )
                }
            }

            DeletionState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ошибка при удалении записи",
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Red,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
    fun SelectImageFromGallery(onImageSelected: (Uri?) -> Unit, selectedImageUri: Uri?) {
    Log.d("SelectImageFromGallery", "Started, uri = $selectedImageUri")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageSelected(uri)
        })
    Box(
        modifier = Modifier
            .size(196.dp)
            .clip(CircleShape)
            .padding(top = 16.dp)
            .clickable {
                launcher.launch("image/")
            }
    ) {
        if (selectedImageUri != null){
        Image(
            painter = rememberImagePainter(
                data = selectedImageUri,
            ),
            contentDescription = "PatientPhoto",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = 1.3f,
                    scaleY = 1.3f
                )
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    }
        else {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                text = "Нажмите чтобы загрузить",
                color = colorResource(id = R.color.LightLightGray)
                )
        }
        }
    Log.d("SelectImageFromGallery", "Finished")

}

suspend fun uploadImageToFirebaseStorage(
    uri: Uri?,
    userRef: DocumentReference
): Boolean {
    return try {
        if (uri != null) {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("images/${uri.lastPathSegment}")
            imageRef.putFile(uri).await()
            val downloadUrl = getDownloadUrlForImage("images/${uri.lastPathSegment}")
            if (downloadUrl != null) {
                userRef.update("profileImageUrl", downloadUrl).await()
                Log.d("FirebaseStorage", "Image URL updated successfully: $downloadUrl")
            }
            true
        } else {
            false
        }
    } catch (e: Exception) {
        Log.e("FirebaseUpload", "Upload failed", e)
        false
    }
}
suspend fun getDownloadUrlForImage(imagePath: String): String? {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val imageRef = storageRef.child(imagePath)

    return try {
        val downloadUrl = imageRef.downloadUrl.await()
        downloadUrl.toString()
    } catch (e: Exception) {
        Log.e("FirebaseStorage", "Failed to get download URL", e)
        null
    }
}

package com.example.myclinic.screens.HomeScreenChilds

import android.os.Build
import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myclinic.R
import com.google.android.play.integrity.internal.f
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(doctorName: String = "", navController: NavHostController) {
    var grayText by remember { mutableStateOf(true) }
    val today = LocalDate.now()
    var isLoadingTimesZone by remember { mutableStateOf(false)  }
    var isUnSuccessSending by remember { mutableStateOf( false)  }
    var appointment by remember { mutableStateOf(false) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var confirmAppointment by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var successfulAppointment by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val patientId = currentUser?.uid



    val daysInMonth = generateDaysInMonthWithPadding(currentMonth)
    val firestore = Firebase.firestore
    var timeSlots by remember { mutableStateOf(emptyMap<String, Boolean>()) }
    val scope = rememberCoroutineScope()
    if (!successfulAppointment && !isUnSuccessSending) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp, bottom = 20.dp, top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(

                    painter = painterResource(id = R.drawable.arrowleft),
                    contentDescription = "Arrow_left",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.authorization_mark),
                            RoundedCornerShape(13.dp)
                        )
                        .clickable {
                            Log.d("CalendarScreen", "Current month before: $currentMonth")
                            currentMonth = currentMonth.minusMonths(1)
                            Log.d("CalendarScreen", "Current month after: $currentMonth")
                        }
                )
                Column(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        ),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = currentMonth.year.toString(),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.arrowright),
                    contentDescription = "Arrow_right",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.authorization_mark),
                            RoundedCornerShape(13.dp)
                        )
                        .clickable {
                            Log.d("CalendarScreen", "Current month before: $currentMonth")
                            currentMonth = currentMonth.plusMonths(1)
                            Log.d("CalendarScreen", "Current month after: $currentMonth") }
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
            Row(modifier = Modifier.fillMaxWidth()) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.LightGray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Column {
                for (week in daysInMonth.chunked(7)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { day ->
                            if ((day.dayOfMonth?.toString() ?: "") == "1") {
                                grayText = !grayText
                            }
                            val currentDate = day.dayOfMonth?.let { currentMonth.atDay(it) }
                            val isBeforeToday = currentDate?.isBefore(today) ?: false
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            day.dayOfMonth == selectedDay -> colorResource(id = R.color.authorization_mark_very_low_opacity)
                                            day.isCurrentDay -> colorResource(id = R.color.authorization_mark_low_opacity)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable(
                                        enabled = !isBeforeToday
                                    )
                                    {
                                        if (doctorName != "" && !isBeforeToday) {
                                            selectedDay = day.dayOfMonth
                                            appointment = true
                                            scope.launch {
                                                isLoadingTimesZone = true
                                                val doctorId =
                                                    getDoctorIdByName(doctorName, firestore)
                                                if (doctorId != null) {
                                                    val selectedDate =
                                                        currentMonth.atDay(day.dayOfMonth ?: 1)
                                                    createAppointmentDocumentIfNeeded(
                                                        doctorId,
                                                        selectedDate
                                                    )

                                                    val appointmentDocRef = firestore
                                                        .collection("Doctors")
                                                        .document(doctorId)
                                                        .collection("DoctorAppointmentCalendar")
                                                        .document(selectedDate.toString())

                                                    val documentSnapshot =
                                                        appointmentDocRef
                                                            .get()
                                                            .await()
                                                    if (documentSnapshot.exists()) {

                                                        val slots =
                                                            documentSnapshot.data?.mapValues { entry ->
                                                                (entry.value as Map<String, Any>)["booked"] as Boolean
                                                            } ?: emptyMap()
                                                        timeSlots = slots
                                                    }
                                                    isLoadingTimesZone = false
                                                }
                                            }

                                        }
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.dayOfMonth?.toString() ?: "",
                                    fontSize = 16.sp,
                                    color = if (grayText) Color.LightGray else Color.Black
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
                if (appointment) {
                    if (!isLoadingTimesZone) {
                        Text(
                            text = "Выберите подходящее время:",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp, bottom = 24.dp)
                                .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                        ) {
                            for (hour in 10..15) {
                                val time = String.format("%02d:00", hour)
                                val isBooked = timeSlots[time] == true
                                val isSelected = selectedTime?.let { it == time } == true

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(CircleShape)
                                        .clickable(enabled = !isBooked) {
                                            confirmAppointment = true
                                            selectedTime = if (selectedTime == time) {
                                                null
                                            } else {
                                                time
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = time,
                                        fontSize = 16.sp,
                                        color = when {
                                            isBooked -> Color.LightGray
                                            isSelected -> colorResource(id = R.color.authorization_mark)
                                            else -> Color.Black
                                        }
                                    )
                                }
                            }

                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    isUnSuccessSending = true
                                    if (getDoctorIdByName(
                                            doctorName,
                                            firestore
                                        ) != null && selectedTime != null && selectedDay != null && patientId != null
                                    ) {
                                        val selectedDate =
                                            selectedDay?.let { currentMonth.atDay(it) }
                                        try {
                                            updateAppointmentSlot(
                                                doctorId = "${
                                                    getDoctorIdByName(
                                                        doctorName,
                                                        firestore
                                                    )
                                                }",
                                                patientId = "$patientId",
                                                selectedDate = selectedDate,
                                                selectedTime = "$selectedTime"
                                            )
                                            isUnSuccessSending = false
                                            successfulAppointment = true
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 100.dp, end = 100.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = colorResource(id = R.color.authorization_mark)
                            )
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = if (confirmAppointment) "Подтвердить запись" else "",
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(64.dp),
                                color = colorResource(id = R.color.authorization_mark)
                            )
                        }

                    }
                } else if (doctorName != "") {
                    Text(
                        text = "Выберите дату:",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    } else if (isUnSuccessSending && !successfulAppointment){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
        }
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            Text(
                text = "Запись сохранена за вами!",
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "Check",
                modifier = Modifier.size(64.dp),
                tint = colorResource(id = R.color.authorization_mark)
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            Text(
                text = "Обратно в каталог",
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate("CatalogScreen") }
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun generateDaysInMonthWithPadding(currentMonth: YearMonth): List<Day> {
    val days = mutableListOf<Day>()

    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val daysInMonth = currentMonth.lengthOfMonth()

    val dayOfWeekOffset =
        firstDayOfMonth.dayOfWeek.value - 1
    val previousMonth = currentMonth.minusMonths(1)
    val daysInPreviousMonth = previousMonth.lengthOfMonth()

    for (i in 1..dayOfWeekOffset) {
        val day = daysInPreviousMonth - dayOfWeekOffset + i
        days.add(Day(day, isCurrentDay = false))
    }

    val today = LocalDate.now()
    for (i in 1..daysInMonth) {
        val date = currentMonth.atDay(i)
        val isCurrentDay = date == today
        days.add(Day(i, isCurrentDay))
    }

    val remainingDays = (7 - lastDayOfMonth.dayOfWeek.value % 7) % 7
    for (i in 1..remainingDays) {
        days.add(Day(i, isCurrentDay = false))
    }

    return days
}

data class Day(val dayOfMonth: Int?, val isCurrentDay: Boolean)

suspend fun createAppointmentDocumentIfNeeded(DoctorID: String, selectedDate: LocalDate) {
    val firestore = Firebase.firestore
    val appointmentDocRef = firestore.collection("Doctors")
        .document(DoctorID)
        .collection("DoctorAppointmentCalendar")
        .document(selectedDate.toString())

    val documentSnapshot = appointmentDocRef.get().await()
    if (!documentSnapshot.exists()) {
        val timeSlots = generateTimeSlots()
        appointmentDocRef.set(timeSlots)
    }
}


fun generateTimeSlots(): Map<String, Map<String, Any>> {
    val timeSlots = mutableMapOf<String, Map<String, Any>>()
    for (hour in 10..15) {
        val time = String.format("%02d:00", hour)
        timeSlots[time] = mapOf("booked" to false, "patientID" to "")
    }
    return timeSlots
}


suspend fun getDoctorIdByName(doctorName: String, firestore: FirebaseFirestore): String? {
    return try {
        val querySnapshot = firestore.collection("Doctors")
            .whereEqualTo("Имя", doctorName)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            querySnapshot.documents.first().id
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun updateAppointmentSlot(
    doctorId: String,
    selectedDate: LocalDate?,
    selectedTime: String,
    patientId: String,
) {
    val firestore = Firebase.firestore
    val appointmentDocRef = firestore.collection("Doctors")
        .document(doctorId)
        .collection("DoctorAppointmentCalendar")
        .document(selectedDate.toString())

    try {
        appointmentDocRef.update(
            mapOf(
                "$selectedTime.booked" to true,
                "$selectedTime.patientID" to patientId
            )
        ).await()

    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }

}
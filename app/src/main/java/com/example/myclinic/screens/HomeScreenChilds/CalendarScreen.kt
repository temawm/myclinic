package com.example.myclinic.screens.HomeScreenChilds

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myclinic.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.w3c.dom.Text
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(doctorName: String) {
    var grayText by remember { mutableStateOf(true) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    fun previousMonth() {
        currentMonth = currentMonth.minusMonths(1)
    }

    fun nextMonth() {
        currentMonth = currentMonth.plusMonths(1)
    }

    val daysInMonth = generateDaysInMonthWithPadding(currentMonth)
    val firestore = Firebase.firestore
    var timeSlots by remember { mutableStateOf(emptyMap<String, Boolean>()) }
    val scope = rememberCoroutineScope()
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
                    .clickable { previousMonth() }
            )
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
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
                    .clickable { nextMonth() }
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
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(
                                    if (day.isCurrentDay) colorResource(id = R.color.authorization_mark_low_opacity) else Color.Transparent
                                )
                                .clickable {
                                    Log.d("Box","button_is_clicked")
                                    scope.launch {
                                        val doctorId = getDoctorIdByName(doctorName, firestore)
                                        Log.d("getDoctorID", "$doctorId")
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

                                            appointmentDocRef
                                                .get()
                                                .addOnSuccessListener { document ->
                                                    if (document.exists()) {
                                                        val slots =
                                                            document.data?.mapValues { entry ->
                                                                (entry.value as Map<String, Any>)["booked"] as Boolean
                                                            } ?: emptyMap()
                                                        timeSlots = slots
                                                    }
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
    Log.d("suspend", selectedDate.toString())
    Log.d("suspend", DoctorID)
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
            // Предположим, что имя уникально, поэтому берем первый найденный документ
            querySnapshot.documents.first().id
        } else {
            null // Врач с таким именем не найден
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
package com.example.myclinic.screens.HomeScreenChilds

import Doctor
import com.example.myclinic.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DoctorScreen(specialization: String, navHostController: NavHostController) {
    val firestore = Firebase.firestore
    var doctors by remember { mutableStateOf(emptyList<Doctor>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(specialization) {
        isLoading = true
        try {
            Log.d("DoctorScreen", "Запрос врачей по специализации: $specialization")
            val querySnapshot = firestore.collection("Doctors")
                .whereEqualTo("Профессия", specialization)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.d("DoctorScreen", "Врачи не найдены для специализации: $specialization")
            } else {
                Log.d("DoctorScreen", "Найдено врачей: ${querySnapshot.size()} для специализации: $specialization")
            }

            doctors = querySnapshot.toObjects(Doctor::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("DoctorScreen", "Ошибка выполнения запроса: ${e.message}")
        } finally {
            isLoading = false
        }
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (isLoading) {
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
            } else {
            for (doctor in doctors) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(14.dp))
                        Image(
                            painter = painterResource(id = R.drawable.mark),
                            contentDescription = "Doctor Photo",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        ) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = doctor.Имя,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Опыт: ${doctor.Опыт} лет",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Рейтинг: ${doctor.Рейтинг}",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.starforrating),
                                    contentDescription = "Star",
                                    modifier = Modifier.size(22.dp),
                                    tint = Color.Yellow
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(start = 44.dp, end = 44.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.authorization_mark_low_opacity),
                                RoundedCornerShape(25.dp)
                            ),
                        onClick = {
                            navHostController.navigate("CalendarScreen/${doctor.Имя}")

                        },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = colorResource(id = R.color.authorization_mark)

                        )
                    ){
                        Text(
                            text = "Запись к врачу",
                            color = colorResource(id = R.color.authorization_mark),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp

                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

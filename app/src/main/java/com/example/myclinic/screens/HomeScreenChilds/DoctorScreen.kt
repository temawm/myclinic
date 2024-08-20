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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun DoctorScreen(specialization: String) {
    val firestore = Firebase.firestore
    var doctors by remember { mutableStateOf(emptyList<Doctor>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(specialization) {
        isLoading = true
        try {
            val querySnapshot = firestore.collection("Doctors")
                .whereEqualTo("Профессия", specialization)
                .get()
                .await()

            doctors = querySnapshot.toObjects(Doctor::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("DoctorScreen","querySnapshot is error.")
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
            Text(text = "Загрузка...", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center, color = Color.Gray)
            CircularProgressIndicator( modifier = Modifier.size(64.dp), color = colorResource(id = R.color.authorization_mark))
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
                            Text(
                                text = doctor.Имя,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Опыт: ${doctor.Опыт} лет",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
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
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Yellow
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

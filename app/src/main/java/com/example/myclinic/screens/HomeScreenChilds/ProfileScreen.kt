package com.example.myclinic.screens.HomeScreenChilds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myclinic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileScreen() {
    var patientName by remember { mutableStateOf<String?>(null) }
    var patientEmail by remember { mutableStateOf<String?>(null) }
    var patientDate by remember { mutableStateOf<String?>(null) }
    var patientProfileImageUrl by remember { mutableStateOf<String?>(null) }
    var changeFields by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val firestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val userRef = firestore.collection("Patients").document(userId!!)

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                patientName = document.getString("name")
                patientDate = document.getString("birthDate")
                patientEmail = document.getString("email")
                patientProfileImageUrl = document.getString("profileImageUrl")
            } else {
                val userProfile = hashMapOf(
                    "name" to "",
                    "birthDate" to "",
                    "email" to "",
                    "profileImageUrl" to ""
                )
                userRef.set(userProfile)
            }
        }.addOnFailureListener { exception ->

        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.LightLightGray))
        )

        Image(
            painter = painterResource(id = R.drawable.mark),
            contentDescription = "PatientPhoto",
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            enabled = false,
            value = if (patientName != null) patientName!! else "",
            onValueChange = { patientName = it },
            label = { Text("Имя", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            placeholder = { Text(text = patientName!!, color = Color.Black) },
            colors = TextFieldDefaults.textFieldColors
                (
                containerColor = Color.Transparent,
            )
        )
        TextField(
            enabled = false,
            value = if (patientEmail != null) patientEmail!! else "",
            onValueChange = { patientEmail = it },
            label = { Text("Почта", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            placeholder = { Text(text = patientEmail!!, color = Color.Black) },
            colors = TextFieldDefaults.textFieldColors
                (
                containerColor = Color.Transparent,
            )
        )
        TextField(
            enabled = false,
            value = if (patientDate != null) patientDate!! else "",
            onValueChange = { patientDate = it },
            label = { Text("Дата рождения", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp),
            placeholder = { Text(text = patientDate!!, color = Color.Black) },
            colors = TextFieldDefaults.textFieldColors
                (
                containerColor = Color.Transparent,
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
            }
            else {
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
                        text = "Продолжить" ,
                        color = colorResource(id = R.color.authorization_mark),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

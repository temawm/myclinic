package com.example.myclinic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myclinic.ui.theme.MyclinicTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyclinicTheme {
                MyApp()
                }
            }
        }
    }
@Composable
fun MyApp() {
    LoginScreen { email, password ->
        // Обработка данных (например, проверка данных или переход на другой экран)
        println("Email: $email, Password: $password")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.mark),
            contentDescription = "authorization_logo",
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(text = "Iclinic",
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            fontSize = 64.sp,
            color = colorResource(id = R.color.authorization_mark))
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false},
            label = { Text("Email", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
                .border(1.dp,if (emailError) colorResource(R.color.authorization_mark) else Color.Gray, RoundedCornerShape(15.dp)
                )
            ,
            placeholder = { Text(text = "example@gmail.com", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = colorResource(id = R.color.authorization_mark),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,

                focusedTextColor = if (emailError) colorResource(id = R.color.authorization_mark) else Color.Black,
                unfocusedTextColor = if (emailError) colorResource(id = R.color.authorization_mark) else Color.Black
            )

        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp)),
            placeholder = { Text(text = "*********", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = colorResource(id = R.color.authorization_mark),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
        Text(
            text = "Забыли пароль?",
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(12.dp)
                .clickable {

                },
            color = colorResource(id = R.color.authorization_mark),
            style = TextStyle(
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )


        )
        Spacer(modifier = Modifier.height(24.dp)) // Отступ между полем и кнопкой
        Button(
            onClick = {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailError = true;
                }
                else {
                    emailError = false
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(15.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.authorization_mark),
                contentColor = Color.White
            )
        ) {
            Text("Продолжить")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { onLoginClick(email, password) },
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
            Text("Регистрация")
        }
        Text(
            text = "Privacy policy",
            fontSize = 16.sp,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(top = 185.dp)
                .clickable {

                },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
            ),
            color = Color.LightGray

        )

    }
}

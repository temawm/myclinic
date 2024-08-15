package com.example.myclinic.screens
import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.myclinic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val auth = Firebase.auth
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false)}
    var passwordError by remember { mutableStateOf(false)}
    var showPopup by remember { mutableStateOf( false)}
    var signInorUp by remember { mutableStateOf(true) }
    var connectionInternet by remember { mutableStateOf(true) }
    var enabledContinueButton by remember { mutableStateOf(true) }
    var timer by remember { mutableStateOf(0)    }
    var failedSignIn by remember { mutableStateOf(false) }
    LaunchedEffect(connectionInternet) {
        if (!connectionInternet) {
            for (i in 5 downTo 0) {
                timer = i
                delay(1000)
            }
            enabledContinueButton = connectionInternet
        }
        if (timer == 0){
            connectionInternet = true
            enabledContinueButton = true
        }
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
                emailError = !validateEmail(email)
            },
            label = { Text("Email", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
                .border(
                    1.dp,
                    if (emailError) colorResource(R.color.authorization_mark) else Color.Gray,
                    RoundedCornerShape(15.dp)
                ),
            placeholder = { Text(text = "example@gmail.com", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors
                (containerColor = Color.White,
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
            onValueChange = {
                password = it
                passwordError = !validatePassword(password)
            },
            label = { Text("Password", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
                .border(
                    1.dp,
                    if (passwordError) colorResource(R.color.authorization_mark) else Color.Gray,
                    RoundedCornerShape(15.dp)
                ),
            placeholder = { Text(text = "********", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors
                (containerColor = Color.White,
                cursorColor = colorResource(id = R.color.authorization_mark),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,

                focusedTextColor = if (passwordError) colorResource(id = R.color.authorization_mark) else Color.Black,
                unfocusedTextColor = if (passwordError) colorResource(id = R.color.authorization_mark) else Color.Black
            )

        )
        Text(
            text = "Забыли пароль?",
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(12.dp)
                .clickable {
                    showPopup = true
                },
            color = colorResource(id = R.color.authorization_mark),
            style = TextStyle(
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )


        )
        Spacer(modifier = Modifier.height(24.dp))
        Row ( modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {
            Button(
                onClick = {
                    signInorUp = true
                },
                modifier = Modifier
                    .width(175.dp)
                    .height(55.dp)
                    .padding(start = 12.dp, end = 12.dp)
                    .border(
                        1.dp,
                        if (signInorUp) colorResource(id = R.color.authorization_mark) else Color.LightGray,
                        RoundedCornerShape(15.dp)
                    ),
                shape = RoundedCornerShape(15.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = if (signInorUp) colorResource(id = R.color.authorization_mark) else Color.White,
                    contentColor = if (signInorUp) Color.White else Color.LightGray
                )
            ) {
                Text("Вход")
            }
            Button(
                onClick = {
                    signInorUp = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(start = 12.dp, end = 12.dp)
                    .border(
                        1.dp,
                        if (signInorUp) Color.LightGray else colorResource(id = R.color.authorization_mark),
                        RoundedCornerShape(15.dp)
                    ),
                shape = RoundedCornerShape(15.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = if (signInorUp) Color.White else colorResource(id = R.color.authorization_mark),
                    contentColor = if (signInorUp) Color.LightGray else Color.White
                )
            ) {
                Text("Регистрация")
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if (validateEmail(email)) {
                    if (validatePassword(password)) {
                        if (isNetworkAvailable(context)) {
                            connectionInternet = true
                            enabledContinueButton = true
                            if (!signInorUp) {
                                signUp(auth, email, password, navController)
                            } else {
                                signIn(auth, email, password, navController)
                            }
                        } else {
                            connectionInternet = false
                            enabledContinueButton = false
                        }
                    }
                    else {
                        passwordError = true
                    }
                }
                else {
                    emailError = true
                }
            },
            enabled = enabledContinueButton,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(start = 12.dp, end = 12.dp)
                .border(
                    1.dp, colorResource(id = R.color.authorization_mark), RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.authorization_mark)
            )
        ) {
            Text(
                text = if (enabledContinueButton) "Продолжить" else "Нет подключения к сети.\nПовторное подключение через $timer секунд.",
                color = colorResource(id = R.color.authorization_mark),
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = "Privacy policy",
            fontSize = 16.sp,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(top = 185.dp)
                .clickable {
                    showPopup = true
                },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
            ),
            color = Color.LightGray

        )
        if (showPopup) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { showPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(150.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .border(3.dp, Color.Black, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {

                        Text(text = "read readme.txt to continue", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicText(
                            text = "Visit my GitHub",
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://github.com/temawm")
                                    }
                                    context.startActivity(intent)
                                }
                                .padding(8.dp),
                            style = TextStyle(
                                color = Color.Blue,
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }
            }
        }
    }
}
private fun signUp(auth: FirebaseAuth, email: String, password: String, navController: NavController) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MyAuthLog", "SignUp is successful!")
            } else {
                Log.d("MyAuthLog", "SignUp is failure!")
            }
        }
}

private fun signIn(auth: FirebaseAuth, email: String, password: String, navController: NavController) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MyAuthLog", "SignIn is successful!")
                navController.navigate("HomeScreen")
            } else {
                Log.d("MyAuthLog", "SignIn is failure!")
            }
        }
}

private fun validateEmail(email: String): Boolean{
    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return true
    }
    else {
        return false
    }
}
private fun validatePassword(password: String): Boolean{
    if (password.length >= 8){
        return true
    }
    else {
        return false
    }
}
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}
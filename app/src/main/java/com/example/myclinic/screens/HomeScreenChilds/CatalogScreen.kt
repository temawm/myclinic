import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myclinic.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen() {
    var search by remember { mutableStateOf("") }
    val doctors = mapOf(
        "Gynecologist" to "Гинеколог",
        "Allergist" to "Аллерголог",
        "Anesthesiologist" to "Анестезиолог",
        "Gastroenterologist" to "Гастроэнтеролог",
        "Dermatologist" to "Дерматолог",
        "Nutritionist" to "Диетолог",
        "Infectious Disease Specialist" to "Инфекционист",
        "Cardiologist" to "Кардиолог",
        "Mammologist" to "Маммолог",
        "Addiction Specialist" to "Нарколог",
        "Neurologist" to "Невролог",
        "Neurophysiologist" to "Нейрофизиолог",
        "Neurosurgeon" to "Нейрохирург",
        "Nutritionist" to "Нутрициолог",
        "Oncologist" to "Онколог",
        "Ophthalmologist" to "Офтальмолог",
        "Pediatrician" to "Педиатр",
        "Psychiatrist" to "Психиатр",
        "Psychotherapist" to "Психотерапевт",
        "Pulmonologist" to "Пульмонолог",
        "Rheumatologist" to "Ревматолог",
        "Radiologist" to "Рентгенолог",
        "Sleep Specialist" to "Сомнолог",
        "Therapist" to "Терапевт",
        "Traumatologist" to "Травматолог",
        "Ultrasound Specialist" to "УЗИ",
        "Urologist" to "Уролог",
        "Physiotherapist" to "Физиотерапевт",
        "Phlebologist" to "Флеболог",
        "Chemotherapist" to "Химиотерапевт",
        "Surgeon" to "Хирург",
        "Endocrinologist" to "Эндокринолог",
        "Endoscopist" to "Эндоскопист"
    )

    val filteredDoctors = doctors.filter { (_, value) ->
        value.contains(search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Выбор специалиста",
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            fontSize = 34.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Поиск...", color = Color.Gray) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .border(
                    2.dp,
                    colorResource(R.color.authorization_mark),
                    RoundedCornerShape(15.dp)
                ),
            placeholder = { Text(text = "Поиск...", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                cursorColor = colorResource(id = R.color.authorization_mark),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(colorResource(id = R.color.LightLightGray))
        )

        Spacer(modifier = Modifier.height(30.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(filteredDoctors.size) { index ->
                val (key, value) = filteredDoctors.entries.elementAt(index)
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            colorResource(id = R.color.authorization_mark_low_opacity),
                            RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = value,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

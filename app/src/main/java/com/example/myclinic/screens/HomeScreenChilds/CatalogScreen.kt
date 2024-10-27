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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myclinic.R
import com.example.myclinic.screens.HomeScreenChilds.CatalogViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navHostController: NavHostController,
    CatalogViewModel: CatalogViewModel
) {
    val search by CatalogViewModel.search
    val filteredDoctors by CatalogViewModel.filteredDoctors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 30.dp, bottom = 60.dp),
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
            onValueChange = { CatalogViewModel.onSearchChanged(it) },
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
            placeholder = { Text(text = "Гинеколог", color = Color.Gray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
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
                val (_, value) = filteredDoctors[index]
                Button(
                    onClick = {
                        navHostController.navigate("DoctorScreen/$value")
                    },
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
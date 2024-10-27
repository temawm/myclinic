package com.example.myclinic.screens.HomeScreenChilds

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor() : ViewModel() {
    private val _search = mutableStateOf("")
    val search: State<String> = _search

    private val doctorsNames = mapOf(
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
        "Ultrasound Specialist" to "УЗИ-специалист",
        "Urologist" to "Уролог",
        "Physiotherapist" to "Физиотерапевт",
        "Phlebologist" to "Флеболог",
        "Chemotherapist" to "Химиотерапевт",
        "Surgeon" to "Хирург",
        "Endocrinologist" to "Эндокринолог",
        "Endoscopist" to "Эндоскопист"
    )

    private val _filteredDoctors = mutableStateOf(doctorsNames.toList())
    val filteredDoctors: State<List<Pair<String, String>>> = _filteredDoctors

    fun onSearchChanged(newSearch: String) {
        _search.value = newSearch
        _filteredDoctors.value = doctorsNames.filter { (_, value) ->
            value.contains(newSearch, ignoreCase = true)
        }.toList()
    }
}
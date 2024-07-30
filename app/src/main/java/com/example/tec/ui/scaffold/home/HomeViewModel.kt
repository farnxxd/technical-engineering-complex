package com.example.tec.ui.scaffold.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tec.data.TecRepository
import com.example.tec.data.local.LocalComplexData
import com.example.tec.data.local.LocalMembersData
import com.example.tec.data.member.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A view model to hold logic behind home screen.
 */
class HomeViewModel(private val tecRepository: TecRepository): ViewModel() {

    // Holds values for dean of complex and some statistics shown in home screen.
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        // Initiates HomeScreen with provided information for dean and numbers.
        updateHomeUiState()
    }

    private fun updateHomeUiState() {
        viewModelScope.launch {
            // Retrieves dean of complex profile. How many academics, authorities and interests
            // are there and restores the data from database.
            val homeUiState = tecRepository.let {
                val deanOfFaculty = it.getDeanOfComplex().first()
                val numberOfAcademics = it.getAcademicsNumber().first()
                val numberOfAuthorities = it.getAuthoritiesNumber().first()
                val numberOfInterests = it.getInterestsNumber().first()

                // Some data are static, so it had to be hardcoded. For example, number of students
                // is not provided online or number of fields will stay unchanged.
                val numbers = listOf(
                    numberOfAcademics,
                    4006,
                    numberOfAuthorities,
                    numberOfInterests,
                    5
                )

                HomeUiState(
                    deanOfFaculty = deanOfFaculty ?: LocalMembersData.emptyMember,
                    numbers = numbers
                )
            }

            _homeUiState.update { homeUiState }
        }
    }

    // Titles are needed for statistics shown in HomeScreen second card. The titles are stored in
    // a local object which can be found inside the data package. For accessibility reasons, I used
    // zip method to glue both of this lists and show access the map once from composable.
    fun getComplexStatistics() = LocalComplexData.inAGlimpseTitles.zip(_homeUiState.value.numbers)
}

/**
 * Ui state for [HomeScreen]
 */
data class HomeUiState(
    val deanOfFaculty: Member = LocalMembersData.emptyMember,
    val numbers: List<Int> = emptyList()
)
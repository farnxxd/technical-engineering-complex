package com.example.tec.ui.scaffold.groups

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tec.R
import com.example.tec.data.TecRepository
import com.example.tec.data.interest.Interest
import com.example.tec.data.local.LocalMembersData
import com.example.tec.data.member.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A view model to hold logic behind group screen.
 */
class GroupViewModel(private val tecRepository: TecRepository) : ViewModel() {

    // Defines which group is selected in header.
    var groupField by mutableStateOf(Group.Computer)
    // Defines which stage is selected in slider.
    var interestValue by mutableFloatStateOf(0f)

    // Holds values for academics, authorities and available study fields and interests.
    private val _groupUiState = MutableStateFlow(GroupUiState())
    val groupUiState: StateFlow<GroupUiState> = _groupUiState.asStateFlow()

    init {
        // Initiates GroupScreen with computer field selected
        updateField(Group.Computer)
    }

    private fun updateGroupUiState() {
        viewModelScope.launch {
            // Retrieves all necessary data for the selected field. Be aware of two fields provided
            // to search. The reason behind this is because Electrical and Mechanical groups are
            // combined and share the same dean and authorities.
            val groupUiState = combine(
                tecRepository.getDeanOfFaculty(groupField.field[0], groupField.field[1]),
                tecRepository.getAcademics(groupField.field[0], groupField.field[1]),
                tecRepository.getAuthorities(groupField.field[0], groupField.field[1]),
                tecRepository.getInterests(groupField.field[0],groupField.field[1])
            ) { dean, academics, authorities, interests ->
                GroupUiState(
                    deanOfFaculty = dean ?: LocalMembersData.emptyMember,
                    academics = academics,
                    authorities = authorities,
                    interests = interests
                )
            }.first()

            _groupUiState.update { groupUiState }
        }
    }

    fun updateField(field: Group) {
        groupField = field
        // Updated the field with new value
        updateGroupUiState()
        // Resets the value of stage whenever group changes
        updateInterestValue(0f)
    }

    fun updateInterestValue(value: Float) {
        // Updates stage value whenever slider changes
        interestValue = value
    }
}

/**
 * There are four groups total and the screen needs to be updated after any change made to selected
 * field. So there two parameter for each value:
 *      * title: to show in group screen
 *      * filed: in order to retrieve data from database
 */
enum class Group(val field: List<String>, @StringRes val title: Int) {
    Computer(field = listOf("کامپیوتر", ""), title = R.string.computer),
    ElectricalMechanical(field = listOf("برق", "مکانیک"), title = R.string.electrical_and_mechanical),
    Civil(field = listOf("عمران", ""), title = R.string.civil),
    Architectural(field = listOf("معماری", ""), title = R.string.architectural)
}

/**
 * Ui state for [GroupsScreen]
 */
data class GroupUiState(
    val deanOfFaculty: Member = LocalMembersData.emptyMember,
    val academics: List<Member> = emptyList(),
    val authorities: List<Member> = emptyList(),
    val interests: List<Interest> = emptyList()
)
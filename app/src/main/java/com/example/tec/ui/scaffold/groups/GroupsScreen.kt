package com.example.tec.ui.scaffold.groups

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tec.R
import com.example.tec.data.callUnit
import com.example.tec.data.convertByteArrayToBitmap
import com.example.tec.data.fullName
import com.example.tec.data.interest.Interest
import com.example.tec.data.member.Member
import com.example.tec.data.sendEmail
import com.example.tec.ui.AppViewModelProvider
import com.example.tec.ui.ConfirmationDialog
import com.example.tec.ui.theme.TECTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GroupsScreen(
    modifier: Modifier = Modifier,
    viewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory),
    listState: LazyListState = rememberLazyListState()
) {
    // UiState to be shown from viewModel
    val uiState = viewModel.groupUiState.collectAsState().value

    // Interest titles need to be shown at the bottom of the screen
    val interestValue = viewModel.interestValue

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Holds the value for cards visibility during changing groups by selecting field from top.
    var visible by remember { mutableStateOf(true) }

    // Animation state for cards transformation
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioNoBouncy),
        label = "alpha"
    ) {
        visible = true
    }

    // If user clicks either call or email button, a dialog box will ask for confirmation.
    var dialogBoxVisibility by remember { mutableStateOf(false) }

    // If this value is true, the dialog box will be shown with email confirmation content.
    // Otherwise, it will show calling confirmation message.
    var dialogBoxAction by remember { mutableStateOf(true) }

    // Necessary for determining the destination's number or email, based on user's choice.
    var contactField by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = visible,        // If cards are not shown, scrolling is disabled.
            state = listState,              // Necessary state holding for FAB (go to top action)
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item(key = 0) {
                GroupFieldPicker(
                    selectedGroup = viewModel.groupField,
                    onItemClick = {
                        coroutineScope.launch {
                            visible = false
                            delay(400)
                            viewModel.updateField(it)
                        }
                    }
                )
            }
            item(key = 1) {
                GroupListTitle(
                    titleRes = R.string.dean_of_faculty,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item(key = 2) {
                uiState.deanOfFaculty.let {
                    MemberCard(
                        topText = it.title.orEmpty(),
                        middleText = it.fullName(),
                        bottomText = stringResource(
                            id = R.string.field_and_interest,
                            uiState.deanOfFaculty.field.orEmpty(),
                            uiState.deanOfFaculty.interest.orEmpty()
                        ),
                        profile = convertByteArrayToBitmap(it.imageBytes),
                        sendEmail = { 
                            dialogBoxAction = true
                            contactField = it.contactInfo
                            dialogBoxVisibility = true
                        },
                        isDoctor = true,
                        modifier = Modifier.graphicsLayer { alpha = animatedAlpha }
                    )
                }
            }
            item(key = 3) {
                GroupListTitle(
                    titleRes = R.string.academics,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            items(items = uiState.academics, key = { it.hashCode()*10 }) {
                MemberCard(
                    topText = it.title.orEmpty(),
                    middleText = it.fullName(),
                    bottomText = it.contactInfo,
                    profile = convertByteArrayToBitmap(it.imageBytes),
                    modifier = Modifier.graphicsLayer { alpha = animatedAlpha }
                )
            }
            item(key = 4) {
                GroupListTitle(
                    titleRes = R.string.authorities,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            items(items = uiState.authorities, key = { it.hashCode()*10 }) {
                AuthorityCard(
                    member = it,
                    callUnit = {
                        dialogBoxAction = false
                        contactField = context.getString(R.string.education_unit)
                        dialogBoxVisibility = true
                    }
                )
            }
            item(key = 5) {
                GroupListTitle(
                    titleRes = R.string.fields,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            item(key = 6) {
                val interestMap = uiState.interests.groupBy { it.stage }
                InterestPicker(
                    stageList = interestMap.entries.map { it.key },
                    interestList = interestMap.entries.map { it.value }[interestValue.toInt()],
                    interestValue = interestValue,
                    onInterestValueChange = { viewModel.updateInterestValue(it.roundToInt().toFloat()) },
                    onInterestClick = { viewModel.updateInterestValue(it) }
                )
            }
            item ( key = 7 ) {
                Spacer(modifier = Modifier.height(44.dp))
            }
            item ( key = 8 ) { 
                Text(
                    text = stringResource(id = R.string.choice_hint),
                    style = typography.labelSmall,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }

        // Dialog box confirmation if user clicks email button.
        if (dialogBoxVisibility && dialogBoxAction) {
            ConfirmationDialog(
                title = stringResource(id = R.string.send_email),
                text = stringResource(id = R.string.email_confirm),
                onConfirm = {
                    sendEmail(context, contactField)
                    dialogBoxVisibility = false
                },
                onDismissRequest = { dialogBoxVisibility = false }
            )
        }

        // Dialog box confirmation if user clicks call button.
        if (dialogBoxVisibility && !dialogBoxAction) {
            ConfirmationDialog(
                title = stringResource(id = R.string.call_phone),
                text = stringResource(id = R.string.call_confirm),
                onConfirm = {
                    callUnit(context, contactField)
                    dialogBoxVisibility = false
                },
                onDismissRequest = { dialogBoxVisibility = false })
        }
    }
}

@Composable
fun GroupFieldPicker(
    selectedGroup: Group,
    onItemClick: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    // Holds the value to the button if it's expanded or not
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            // The field picker button itself
            GroupDropDownButton(
                title = stringResource(id = selectedGroup.title),
                onClick = { expanded = !expanded }
            )
            // This row contains spacer. These spacers adjust the expanded list just below the center
            // of the field picker button.
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                // Dropdown menu. Children are implemented in the composable itself.
                GroupDropDownMenu(
                    expanded = expanded,
                    selectedGroup = selectedGroup,
                    onDismissRequest = { expanded = false },
                    onItemClick = {
                        onItemClick(it)
                        expanded = !expanded
                    }
                )
                Spacer(modifier = Modifier.widthIn(min = 130.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GroupDropDownMenu(
    expanded: Boolean,
    selectedGroup: Group,
    onDismissRequest: () -> Unit,
    onItemClick: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.background(colorScheme.surfaceContainerHighest)
        ) {
            for (group in Group.entries) {
                if (group != selectedGroup) {
                    // All items of dropdown menu are listed here using iteration. Pay attention
                    // the selected group will not be shown in the list checking with if statement
                    // right above.
                    GroupDropDownMenuItem(
                        title = stringResource(id = group.title),
                        onClick = { onItemClick(group) },
                    )
                }
            }
        }
    }
}

@Composable
fun GroupDropDownButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        // Spacers are used for more aesthetic purpose.
        Spacer(modifier = Modifier.width(4.dp))
        // Group or field name to be shown on button. remember the text on button will change.
        Text(text = stringResource(id = R.string.group_field, title))
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down),
            contentDescription = ""
        )
    }
}

@Composable
fun GroupDropDownMenuItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        text = { Text(
            text = stringResource(id = R.string.group_field, title),
            style = typography.bodySmall
        ) },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun GroupListTitle(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = titleRes),
        style = typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun MemberCard(
    topText: String,
    middleText: String,
    bottomText: String,
    profile: Bitmap?,
    modifier: Modifier = Modifier,
    sendEmail: () -> Unit = {},
    isDoctor: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(colorScheme.surfaceContainer),
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            ProfilePicture(profile = profile)
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Member's rank
                Text(
                    text = topText,
                    style = typography.labelSmall,
                    modifier = Modifier.alpha(0.35f)
                )
                // Full name of member
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // If member is dean, adds doctor prefix
                    if (isDoctor) {
                        Text(
                            text = "${stringResource(id = R.string.doctor)} ",
                            style = typography.labelSmall,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Text(
                        text = middleText,
                        style = typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                // If member is dean, shows field and interest of study, otherwise email address.
                Text(
                    text = bottomText,
                    style = typography.labelSmall,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.alpha(0.7f)
                )
            }

            // If member is dean, adds a send email button. On user click, opens dialog box and
            // ask for confirmation.
            if (isDoctor) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    IconButton(onClick = sendEmail) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_email),
                            contentDescription = stringResource(id = R.string.send_email)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthorityCard(
    member: Member,
    callUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        ListItem(
            colors = ListItemDefaults.colors(colorScheme.surfaceContainer),
            // Full name of authority
            headlineContent = { Text(
                text = member.fullName(),
                style = typography.bodyMedium,
                fontWeight = FontWeight.Medium
            ) },
            // Interior department number
            supportingContent = { Text(
                text = stringResource(id = R.string.phone_number, member.contactInfo),
                style = typography.labelSmall,
                modifier = Modifier.alpha(0.7f)
            ) },
            leadingContent = {
                Box(modifier = Modifier.width(60.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(30.dp)
                    )}},
            // Redirects the screen to Phone app in order to make a call. But first opens a dialog
            // box and asks for confirmation.
            trailingContent = {
                IconButton(onClick = callUnit) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_phone),
                        contentDescription = stringResource(id = R.string.call_phone)
                    )
                }
            }
        )
    }
}

@Composable
fun InterestPicker(
    stageList: List<String>,
    interestList: List<Interest>,
    interestValue: Float,
    onInterestValueChange: (Float) -> Unit,
    onInterestClick: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        InterestHeader(
            stageList = stageList,
            interestValue = interestValue,
            onInterestClick = onInterestClick
        )
        InterestSlider(
            interestValue = interestValue,
            onInterestValueChange = onInterestValueChange,
            numberOfStages = stageList.size
        )
        InterestCard(interestList = interestList)
    }
}

@Composable
fun InterestHeader(
    stageList: List<String>,
    interestValue: Float,
    onInterestClick: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        // Iterates through the list of stages available for selected field. The list of stages
        // may vary, so that's why a dynamic and responsive method is implemented instead of
        // hardcoding the titles. It restores data from database.
        stageList.withIndex().forEach {
            val isActive = it.index.toFloat() == interestValue
            // Selectable headers
            TextButton(
                onClick = { onInterestClick(it.index.toFloat()) },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                // Labels of buttons
                Text(
                    text = it.value.replaceFirst(" ", "\n"),
                    style = typography.labelMedium,
                    textAlign = TextAlign.Center,
                    color = if (isActive) colorScheme.primary else colorScheme.onSurface,
                    modifier = Modifier
                        .widthIn(min = 64.dp)
                        .alpha(if (isActive) 1f else 0.3f)
                )
            }
        }
    }
}

@Composable
fun InterestSlider(
    interestValue: Float,
    onInterestValueChange: (Float) -> Unit,
    numberOfStages: Int,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        // Interest slider with interaction support
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(numberOfStages) {
                Card(
                    colors = CardDefaults.cardColors(colorScheme.surfaceContainer),
                    modifier = Modifier.size(28.dp)
                ) {

                }
            }
        }
        Slider(
            value = interestValue,
            onValueChange = onInterestValueChange,
            valueRange = 0f..(numberOfStages - 1).toFloat(),
            steps = (numberOfStages - 2),
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                inactiveTrackColor = colorScheme.surfaceContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun InterestCard(
    interestList: List<Interest>,
    modifier: Modifier = Modifier
) {
    // Interest card shown at the bottom of the group screen
    Column {
        Card(
            colors = CardDefaults.cardColors(colorScheme.surfaceContainerLow),
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                        start = 24.dp,
                        bottom = 20.dp
                    )
            ) {
                // Printing all available interests according to selected stage
                interestList.forEach {
                    Text(
                        text = if (!it.name.contains(it.field)) {
                            stringResource(
                                id = R.string.interest,
                                it.field,
                                it.name
                            )
                        } else {
                            stringResource(id = R.string.group_field, it.name)
                        },
                        style = typography.bodyMedium
                    )
                }
            }
        }
        // This iteration creates space equal to single interest while printed inside the
        // InterestCard, so it maintains the remaining height below the card if the items are less
        // than 4.
        repeat(4 - interestList.size) {
            Text(text = "", style = typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun ProfilePicture(
    profile: Bitmap?,
    modifier: Modifier = Modifier,
    profileSize: Int = 60
) {
    Box(modifier = modifier) {
        if (profile != null) {
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Image(
                    bitmap = profile.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(profileSize.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupsScreenPreview() {
    TECTheme {
        GroupsScreen()
    }
}
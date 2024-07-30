package com.example.tec.ui.scaffold.home

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tec.R
import com.example.tec.data.convertByteArrayToBitmap
import com.example.tec.data.fullName
import com.example.tec.data.local.LocalComplexData
import com.example.tec.data.local.LocalMembersData
import com.example.tec.data.member.Member
import com.example.tec.data.sendEmail
import com.example.tec.ui.AppViewModelProvider
import com.example.tec.ui.ConfirmationDialog
import com.example.tec.ui.scaffold.groups.ProfilePicture
import com.example.tec.ui.theme.TECTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    listState: LazyStaggeredGridState = rememberLazyStaggeredGridState()
) {
    // UiState to be shown from viewModel
    val uiState = viewModel.homeUiState.collectAsState().value

    val context = LocalContext.current

    // If user clicks email button, a dialog box will ask for confirmation.
    var dialogBoxVisibility by remember { mutableStateOf(false) }

    // Necessary for determining the destination's email.
    var contactField by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),      // Two columns
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
            state = listState,          // Necessary state holding for FAB (go to top action)
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            // Header logo
            item(key = 0, span = StaggeredGridItemSpan.FullLine) { Logo() }
            item(key = 1, span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.fillMaxWidth())
            }

            // Complex history
            LocalComplexData.complexHistory.let {
                item(key = 2) { HomeListTitle(text = stringResource(id = it.first))}
                item(key = 3) {
                    ListImage(imageRes = R.drawable.complex, modifier = Modifier.weight(1f))
                }
                item(key = 4) {
                    ListDescription(
                        text = stringResource(id = it.second),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item(key = 5) { Spacer(modifier = Modifier.fillMaxWidth()) }
            item(key = 6) { RoadMap(modifier = Modifier.height(220.dp)) }
            item(key = 7, span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.fillMaxWidth())
            }

            // In a glimpse statistics
            item(key = 8) {
                ListImage(imageRes = R.drawable.statistics, modifier = Modifier.weight(1f))
            }
            item(key = 9) { HomeListTitle(text = stringResource(id = R.string.in_a_glimpse)) }
            item(key = 10) { StatisticContent(
                title = stringResource(R.string.person), list = viewModel.getComplexStatistics()
            ) }
            item(key = 11) { RoadMap(modifier = Modifier.height(320.dp)) }
            item(key = 12, span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.fillMaxWidth())
            }

            // Dean of complex
            item(key = 13) { HomeListTitle(text = stringResource(id = R.string.dean_of_complex)) }
            item(key = 14) {
                uiState.deanOfFaculty.let {
                    DeanOfFacultyCard(
                        member = it,
                        // Profile picture needs to be converted
                        profile = convertByteArrayToBitmap(it.imageBytes),
                        sendEmail = {
                            contactField = it.contactInfo
                            dialogBoxVisibility = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item(key = 15) { FormerDeansCard(members = LocalMembersData.formerDeans) }
            item(key = 16) { RoadMap(modifier = Modifier.height(200.dp)) }
            item(key = 17, span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.fillMaxWidth())
            }

            // Facilities
            item(key = 18) {
                ListImage(imageRes = R.drawable.facilities, modifier = Modifier.weight(1f))
            }
            item(key = 19) { HomeListTitle(text = stringResource(id = R.string.facilities)) }
            item { StatisticContent(
                title = stringResource(R.string.classes), list = LocalComplexData.facilitiesList
            ) }

            item(key = 20, span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Dialog box confirmation if user clicks email button.
        if (dialogBoxVisibility) {
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
    }
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.logo_iau),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun HomeListTitle(text: String, modifier: Modifier = Modifier) {
    Column {
        RoadMap(modifier = Modifier.height(24.dp))
        Text(
            text = text,
            style = typography.headlineMedium,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun RoadMap(
    modifier: Modifier = Modifier,
    color: Color = colorScheme.surfaceContainerHighest
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        // Vertical line drawn between different items in home screen
        VerticalDivider(
            color = color,
            thickness = 3.dp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        content()
    }
}

@Composable
fun ListImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    ListCard(modifier = modifier) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ListDescription(
    text: String,
    modifier: Modifier = Modifier
) {
    // First card in the screen
    ListCard(modifier = modifier) {
        Text(
            text = text,
            style = typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun DeanOfFacultyCard(
    member: Member,
    profile: Bitmap?,
    sendEmail: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Third card in the screen
    ListCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(
                    start = 16.dp, end = 16.dp,
                    top = 20.dp, bottom = 8.dp
                )
        ) {
            ProfilePicture(profile = profile, profileSize = 100, modifier = Modifier.padding(bottom = 20.dp))
            DeanOfFacultyContent(member = member)
            EmailButton(sendEmail = sendEmail)
        }
    }
}

@Composable
fun DeanOfFacultyContent(
    member: Member,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        // Dean's full name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${stringResource(id = R.string.doctor)} ",
                style = typography.labelSmall,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = member.fullName(),
                style = typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        // Dean's field of interest
        Text(
            text = stringResource(
                id = R.string.field_and_interest,
                member.field ?: "",
                member.interest ?: ""
            ),
            style = typography.labelSmall
        )
        Text(
            text = stringResource(id = R.string.degree),
            style = typography.labelSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Dean's rank
        Text(
            text = member.title ?: "",
            style = typography.bodySmall,
            modifier = Modifier.alpha(0.35f)
        )
    }
}

@Composable
fun FormerDeansCard(
    members: List<Member>,
    modifier: Modifier = Modifier
) {
    // Third card in home screen
    ListCard(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Card's title
            Text(
                text = stringResource(R.string.former_deans),
                style = typography.bodyMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            // Former deans
            members.forEach {
                Column {
                    Text(
                        text = "● ${it.title ?: ""}",
                        style = typography.labelSmall,
                        modifier = Modifier.alpha(0.7f)
                    )
                    Text(
                        text = stringResource(id = R.string.doctor) + " " + it.fullName(),
                        style = typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticContent(
    title: String,
    list: List<Pair<Int,Int>>,
    modifier: Modifier = Modifier
) {
    // Second and fourth card in home screen
    ListCard(modifier = modifier) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Card's title
            Text(
                text = stringResource(id = R.string.number, title),
                style = typography.bodyMedium,
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(bottom = 12.dp)
            )
            // Table drawn inside the card with information in it
            list.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = it.first),
                        style = typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = it.second.toString(),
                        fontSize = 28.sp,
                        lineHeight = 52.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun EmailButton(
    sendEmail: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = colorScheme.primary
) {
    TextButton(
        onClick = sendEmail,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.send_email),
            style = typography.labelSmall,
            color = color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.baseline_email),
            contentDescription = stringResource(id = R.string.send_email),
            tint = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MembersPreview() {
    TECTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            HomeScreen()
        }
    }
}
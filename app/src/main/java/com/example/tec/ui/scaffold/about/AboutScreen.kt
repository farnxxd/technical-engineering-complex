package com.example.tec.ui.scaffold.about

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.tec.R
import com.example.tec.data.callUnit
import com.example.tec.data.sendEmail
import com.example.tec.ui.ConfirmationDialog
import com.example.tec.ui.scaffold.groups.GroupListTitle
import com.example.tec.ui.theme.TECTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // If user clicks either call or email button, a dialog box will ask for confirmation.
    var dialogBoxVisibility by remember { mutableStateOf(false) }

    // If this value is true, the dialog box will be shown with email confirmation content.
    // Otherwise, it will show calling confirmation message.
    var dialogBoxAction by remember { mutableStateOf(true) }

    // Necessary for determining the destination's number or email, based on user's choice.
    var contactField by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Whole Google map card
        UniversityMap(modifier = Modifier.height(IntrinsicSize.Min))

        // University address information
        AddressCard()

        // Call and email button at the bottom of the screen
        ButtonRow(
            sendEmail = {
                dialogBoxAction = true
                contactField = context.getString(R.string.university_email)
                dialogBoxVisibility = true
            },
            callUnit = {
                dialogBoxAction = false
                contactField = context.getString(R.string.university_unit)
                dialogBoxVisibility = true
            }
        )

        // Dialog box confirmation if user clicks email button.
        if (dialogBoxVisibility && dialogBoxAction) {
            ConfirmationDialog(
                title = stringResource(id = R.string.send_email),
                text = stringResource(id = R.string.email_confirm),
                onConfirm = {
                    sendEmail(context, contactField)
                    dialogBoxVisibility = false
                },
                onDismissRequest = { dialogBoxVisibility = false })
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
fun UniversityMap(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
    ) {
        GoogleMap(
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(37.2500,49.5800), 13.5f
                )
            },
            mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM
        ) {
            Marker(
                state = rememberMarkerState(position = LatLng(37.2435,49.5776)),
                title = stringResource(id = R.string.location),
                snippet = stringResource(id = R.string.app_name_fa)
            )
        }
    }
}

@Composable
fun AddressCard() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GroupListTitle(titleRes = R.string.address)
        Card(
            colors = CardDefaults.cardColors(colorScheme.surfaceContainer)
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.address_detail),
                        style = typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                },
                colors = ListItemDefaults.colors(Color.Transparent),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ButtonRow(
    sendEmail: () -> Unit,
    callUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(modifier = modifier) {
            ContactButton(
                titleRes = R.string.send_email,
                iconRes = R.drawable.baseline_email,
                onClick = sendEmail,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ContactButton(
                titleRes = R.string.call_phone,
                iconRes = R.drawable.baseline_phone,
                onClick = callUnit,
                modifier = Modifier.weight(1f)
            )
        }
        OpeningHours(modifier = Modifier.padding(top = 8.dp, start = 8.dp))
    }
}

@Composable
fun ContactButton(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = titleRes))
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(id = titleRes)
            )
        }
    }
}

@Composable
fun OpeningHours(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.opening_hours))
                append(": ")
                append(stringResource(id = R.string.opening_hours_detail))
            },
            style = typography.labelSmall,
            modifier = Modifier.alpha(0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    TECTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AboutScreen()
        }
    }
}
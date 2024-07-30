package com.example.tec.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tec.R
import com.example.tec.ui.navigation.NavigationItemContent
import com.example.tec.ui.navigation.NavigationList
import com.example.tec.ui.navigation.Screen
import com.example.tec.ui.scaffold.about.AboutScreen
import com.example.tec.ui.scaffold.groups.GroupViewModel
import com.example.tec.ui.scaffold.groups.GroupsScreen
import com.example.tec.ui.scaffold.home.HomeScreen
import com.example.tec.ui.scaffold.home.HomeViewModel
import com.example.tec.ui.theme.TECTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TecApp() {

    // Instantiating view models to be ready before navigating to the screen
    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val groupViewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory)

    // Holds the selected screen value, listens to user choice, instantiate by Home value.
    var screen by rememberSaveable { mutableStateOf(Screen.Home) }

    val coroutineScope = rememberCoroutineScope()

    // Scrolling state of home and group screen
    val homeListState = rememberLazyStaggeredGridState()
    val groupListState = rememberLazyListState()

    // Boolean value to either show the FAB button or not. Because of different type of list
    // implementation in home and group screen, there were two different logic needed.
    val showHomeFAB by remember { derivedStateOf { homeListState.firstVisibleItemIndex > 0 } }
    val showGroupFAB by remember { derivedStateOf { groupListState.firstVisibleItemIndex > 0 } }


    Box {
        Scaffold(
            topBar = { TecTopAppBar() },
            bottomBar = {
                TecNavigationBar(
                    currentScreen = screen,
                    onTabPressed = { screen = it },
                    navigationItemContentList = NavigationList.navigationItemContentList
                )
            },
            floatingActionButton = {
                when (screen) {
                    Screen.Home ->
                        TecFAB(
                            showButton = showHomeFAB,
                            onClick = { coroutineScope.launch { homeListState.animateScrollToItem(0) } }
                        )

                    Screen.Groups ->
                        TecFAB(
                            showButton = showGroupFAB,
                            onClick = { coroutineScope.launch { groupListState.animateScrollToItem(0) } }
                        )

                    else -> Unit
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                when (screen) {
                    Screen.Home -> HomeScreen(
                        viewModel = homeViewModel,
                        listState = homeListState,
                        modifier = Modifier.padding(it)
                    )

                    Screen.Groups -> GroupsScreen(
                        viewModel = groupViewModel,
                        listState = groupListState,
                        modifier = Modifier.padding(it)
                    )

                    Screen.About -> AboutScreen(modifier = Modifier.padding(it))

                    Screen.Splash -> Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LaunchedEffect(Unit) {
                            delay(500)
                            screen = Screen.Home
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecTopAppBar(modifier: Modifier = Modifier) {
    // TopBar shown in every screen of application
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name_fa),
                fontWeight = FontWeight.Bold
            ) },
        modifier = modifier
    )
}

@Composable
fun TecFAB(
    showButton: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Floating action button which appears after scrolling a little. On clicking, it resets scroll
    // state and returns to top of screen.
    if (showButton) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_top),
                contentDescription = stringResource(R.string.go_to_top)
            )
        }
    }
}

@Composable
fun TecNavigationBar(
    currentScreen: Screen,
    onTabPressed: (Screen) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    // NavigationBar at the bottom of the screen to choose the screen
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = modifier
        ) {
            navigationItemContentList.forEach {
                NavigationBarItem(
                    selected = currentScreen == it.screen,
                    onClick = { onTabPressed(it.screen) },
                    icon = {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = stringResource(id = it.label)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = it.label),
                            fontWeight =
                            if (currentScreen == it.screen) FontWeight.ExtraBold
                            else FontWeight.Light
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dialog box for call and email confirmation
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(text = stringResource(id = R.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(text = stringResource(id = R.string.cancel)) }
        },
        text = { Text(text = text) },
        title = { Text(text = title, fontWeight = FontWeight.Medium) },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TecAppPreview() {
    TECTheme {
        TecApp()
    }
}
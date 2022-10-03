package com.example.jetpackcomposetest.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.jetpackcomposetest.common.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "OTPScreen"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPScreen(navController: NavController) {

    val isOtpCorrect = remember {
        mutableStateOf(true)
    }

    val timer = remember {
        mutableStateOf(120000)
    }

    val isOtpResent = remember {
        mutableStateOf(false)
    }

    if (timer.value > 0) {
        LaunchedEffect(isOtpResent.value) {
            while (timer.value > 0) {
                delay(1000L)
                timer.value -= 1000
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verification code has been sent to: \n ******1234",
            textAlign = TextAlign.Center
        )
        Text(
            text = "please enter the code you received below",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        if (!isOtpCorrect.value)
            Text(text = "the OTP entered is not correct!", color = Color.Red)

        OTPTextFiled(modifier = Modifier, isOTPCorrect = isOtpCorrect) { otpEntered ->
            Log.d(TAG, "OTPScreen: $otpEntered")
            if (otpEntered == "1111")
                navController.navigate(Screen.Home.route)
            else
                isOtpCorrect.value = false
        }
        if (timer.value > 0) {
            Text(text = "Activation code will expire after:")
            Text(
                text = "${timer.value / 1000 / 60}:${
                    String.format(
                        "%02d",
                        timer.value / 1000 % 60
                    )
                }"
            )
        } else {
            Text(
                text = "resend OTP",
                modifier = Modifier.clickable(true) {
                    timer.value = 120000
                    isOtpResent.value = !isOtpResent.value
                },
                color = Color.Blue,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPTextFiled(
    modifier: Modifier,
    OTPLength: Int = 4,
    isOTPCorrect: MutableState<Boolean>,
    whenFull: (smsCode: String) -> Unit
) {
    val enteredNumbers = remember {
        mutableStateListOf(
            *((0 until OTPLength).map { "" }.toTypedArray())
        )
    }
    val focusRequesters: List<FocusRequester> = remember {
        (0 until OTPLength).map { FocusRequester() }
    }

    val isEnabled = remember {
        mutableStateListOf(*(0 until OTPLength).map { it == 0 }.toTypedArray())
    }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        focusRequesters[0].requestFocus()
        onDispose { }
    }

    Row(modifier = modifier.padding(start = 60.dp, end = 60.dp)) {
        (0 until OTPLength).forEach { index ->
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .size(120.dp, 80.dp)
                    .onKeyEvent { event ->
                        val cellValue = enteredNumbers[index]
                        if (event.type == KeyEventType.KeyUp) {
                            if (event.key == Key.Backspace && cellValue == "" && index != 0) {
                                isEnabled[index] = false
                                focusRequesters
                                    .getOrNull(index - 1)
                                    ?.requestFocus()
                                enteredNumbers[index - 1] = ""

                            } else if (cellValue != "") {
                                coroutineScope.launch {
                                   if (index != OTPLength - 1) {
                                        isEnabled[index + 1] = true
                                    }
//                                    delay(10)
                                    focusRequesters
                                        .getOrNull(index + 1)
                                        ?.requestFocus()
                                }
                            }
                        }
                        false
                    }
                    .padding(vertical = 2.dp)
                    .focusRequester(focusRequesters[index])
                    .focusOrder(focusRequesters[index]),
                colors = TextFieldDefaults.textFieldColors(
                    errorLabelColor = Color.Red
                ),

                isError = !isOTPCorrect.value,
                singleLine = true,
                value = enteredNumbers[index],
                onValueChange = { value: String ->
                    if (value.isDigitsOnly()) {
                        if (value.length > 1) {
                            enteredNumbers[index] = value.last().toString()
                            return@OutlinedTextField
                        }
                        if (focusRequesters[index].freeFocus()) {
                            enteredNumbers[index] = value
                            if (enteredNumbers[index].isBlank() && index > 0 && index < OTPLength) {
                                isEnabled[index] = false
                                focusRequesters[index - 1].requestFocus()
                            } else if (index < OTPLength - 1) {
                                coroutineScope.launch{
                                    isEnabled[index + 1] = true
                                    delay(50)
                                    focusRequesters[index + 1].requestFocus()
                                }
                            } else if (enteredNumbers.size == OTPLength) {
                                whenFull(enteredNumbers.joinToString(separator = ""))
                                if (!isOTPCorrect.value){
                                    coroutineScope.launch{
                                        enteredNumbers.replaceAll { "" }
                                        isEnabled.replaceAll { false }.also { isEnabled[0] = true }
                                        delay(50)
                                        focusRequesters[0].requestFocus()
                                    }
                                }
                            }
                        }
                    }
                },
                enabled = isEnabled[index],
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}
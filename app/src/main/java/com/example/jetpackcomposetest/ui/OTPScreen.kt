package com.example.jetpackcomposetest.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.example.jetpackcomposetest.common.Screen

private const val TAG = "OTPScreen"

@Composable
fun OTPScreen(navController: NavController) {

    Log.d(TAG, "OTPScreen: hello from otp screen")
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
        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OTPTextFiled(modifier = Modifier) { otpEntered ->
                Log.d(TAG, "OTPScreen: $otpEntered")
                if (otpEntered == "1111")
                    navController.navigate(Screen.Home.route)
            }
//            CommonOtpTextField(otp = otpOne)
//            CommonOtpTextField(otp = otpTwo)
//            CommonOtpTextField(otp = otpThree)
//            CommonOtpTextField(otp = otpFour)
//            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.size(36.dp))
//            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.size(36.dp))
//            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.size(36.dp))
//            OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.size(36.dp))
        }
        Text(text = "Activation code will expire after:")
        Text(text = "Timer here")
    }
}

@Composable
fun CommonOtpTextField(otp: MutableState<String>) {
    val max = 1
    OutlinedTextField(
        value = otp.value,
        singleLine = true,
        onValueChange = {
            if (it.length <= max) {
                otp.value = it
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier
            .width(60.dp)
            .height(60.dp),
        maxLines = 1,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPTextFiled(
    modifier: Modifier,
    OTPLength: Int = 4,
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
    Row(modifier = modifier.padding(start = 60.dp, end = 60.dp)) {
        (0 until OTPLength).forEach { index ->
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .size(120.dp, 80.dp)
                    .onKeyEvent { event ->
                        val cellValue = enteredNumbers[index]
                        if (event.type == KeyEventType.KeyUp) {
                            if (event.key == Key.Backspace && cellValue == "") {
                                focusRequesters
                                    .getOrNull(index - 1)
                                    ?.requestFocus()
                                enteredNumbers[index - 1] = ""
                            } else if (cellValue != "") {
                                focusRequesters
                                    .getOrNull(index + 1)
                                    ?.requestFocus()
                            }
                        }
                        false
                    }
                    .padding(vertical = 2.dp)
                    .focusOrder(focusRequesters[index])
                    .focusRequester(focusRequesters[index]),
                colors = TextFieldDefaults.textFieldColors(
                ),

                singleLine = true,
                value = enteredNumbers[index],
                onValueChange = { value: String ->
                    if (value.isDigitsOnly()) {
                        if (value.length > 1) {
                            enteredNumbers[index] = value.last().toString()
                            return@TextField
                        }
                        if (focusRequesters[index].freeFocus()) {
                            enteredNumbers[index] = value
                            if (enteredNumbers[index].isBlank() && index > 0 && index < OTPLength) {
                                focusRequesters[index - 1].requestFocus()
                            } else if (index < OTPLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            } else if (enteredNumbers.size == OTPLength) {
                                whenFull(enteredNumbers.joinToString(separator = ""))
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}
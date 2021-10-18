package com.reflectednetwork.tanukitimer

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.reflectednetwork.tanukitimer.ui.theme.TanukiTimerTheme
import kotlinx.coroutines.delay

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.view.WindowManager
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.PopupProperties
import com.reflectednetwork.tanukitimer.ui.theme.TanukiTimerThemeSquare
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent

import android.media.AudioManager
import android.provider.Settings
import android.widget.PopupMenu
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.window.Popup


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        setContent {
            var timeName by remember { mutableStateOf("         ") }
            var maxTime by remember { mutableStateOf(0L) }
            var currentTime by remember { mutableStateOf(maxTime) }
            var isTimerRunning by remember { mutableStateOf(false) }
            var expanded by remember { mutableStateOf(false) }

            var mp by remember { mutableStateOf<MediaPlayer?>( null ) }

            var tryNotifs by remember { mutableStateOf(false) }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            TanukiTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (currentTime % 60 < 10) {
                                "${currentTime / 60}:0${currentTime % 60}"
                            } else {
                                "${currentTime / 60}:${currentTime % 60}"
                            },
                            fontSize = 30.em,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )

                        if (currentTime == 0L || currentTime == maxTime) {
                            Button(
                                onClick = { expanded = true },
                                colors = buttonColors(Color.Gray, Color.White, Color.Gray, Color.White)
                            ) {
                                Text(
                                    text = timeName
                                )

                                if (expanded) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        "Close Dropdown",
                                        modifier = Modifier.rotate(270f)
                                    )
                                } else {
                                    Icon(Icons.Default.ArrowDropDown, "Open Dropdown")
                                }

                                TanukiTimerThemeSquare {
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(onClick = {
                                            maxTime = 1500L
                                            currentTime = maxTime
                                            expanded = false
                                            timeName = "Working"
                                            mp?.stop()
                                        }) {
                                            Text(text = "Working")
                                        }

                                        DropdownMenuItem(onClick = {
                                            maxTime = 300L
                                            currentTime = maxTime
                                            expanded = false
                                            timeName = "Short Break"
                                            mp?.stop()
                                        }) {
                                            Text(text = "Short Break")
                                        }

                                        DropdownMenuItem(onClick = {
                                            maxTime = 900L
                                            currentTime = maxTime
                                            expanded = false
                                            timeName = "Long Break"
                                            mp?.stop()
                                        }) {
                                            Text(text = "Long Break")
                                        }
                                    }
                                }
                            }
                        }

                        if (currentTime > 0) {
                            Button(
                                onClick = {
                                    if (!isTimerRunning && currentTime > 0) {
                                        currentTime -= 1L
                                    }

                                    if (isTimerRunning) {
                                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                                    } else if (timeName == "Working") {
                                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                                    }

                                    isTimerRunning = !isTimerRunning
                                },
                                modifier = Modifier.padding(PaddingValues(0.dp, 10.dp, 0.dp, 0.dp))
                            ) {
                                Text(
                                    text = if (isTimerRunning) {
                                        "Pause"
                                    } else {
                                        "Start"
                                    }
                                )
                            }

                            if (currentTime != maxTime) {
                                Button(
                                    onClick = {
                                        isTimerRunning = false
                                        currentTime = maxTime
                                        mp?.stop()
                                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                                    },
                                    modifier = Modifier.padding(
                                        PaddingValues(
                                            0.dp,
                                            10.dp,
                                            0.dp,
                                            0.dp
                                        )
                                    )
                                ) {
                                    Text(
                                        text = "Reset"
                                    )
                                }
                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.tanukilarge),
                        contentDescription = "Tanuki",
                        modifier = Modifier.absoluteOffset(80.dp, 150.dp)
                    )
                }
            }

            if (!notificationManager.isNotificationPolicyAccessGranted && !tryNotifs) {
                Popup {
                    TanukiTimerTheme {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxHeight().background(MaterialTheme.colors.background)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(30.dp)
                                    .shadow(5.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text("Hey love,")
                                    Text("This app needs Do Not Disturb access so that it can save you from yourself when you're trying to get work done.")

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Button(
                                            onClick = {
                                                tryNotifs = true
                                                val intent = Intent(
                                                    Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                                                )
                                                startActivity(intent)
                                            },
                                            modifier = Modifier.padding(10.dp).fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Gotcha",
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
                if (notificationManager.isNotificationPolicyAccessGranted) {
                    if (isTimerRunning) {
                        if (currentTime > 0) {
                            delay(1000L)
                            currentTime -= 1L
                        } else {
                            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                            mp = MediaPlayer.create(
                                applicationContext,
                                RingtoneManager.getActualDefaultRingtoneUri(
                                    window.context,
                                    RingtoneManager.TYPE_ALARM
                                )
                            )
                            mp?.start()
                            isTimerRunning = false
                        }
                    }
                }
            }
        }
    }
}

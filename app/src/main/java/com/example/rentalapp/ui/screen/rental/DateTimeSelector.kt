package com.example.rentalapp.ui.screen.rental

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rentalapp.ui.designsystem.AppTheme
import com.example.rentalapp.ui.designsystem.components.Button
import com.example.rentalapp.ui.designsystem.components.ButtonVariant
import com.example.rentalapp.ui.designsystem.components.Icon
import com.example.rentalapp.ui.designsystem.components.Text
import com.example.rentalapp.ui.designsystem.components.card.Card
import com.example.rentalapp.ui.designsystem.components.card.OutlinedCard
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateTimeSelector(
    modifier: Modifier = Modifier,
    startTime: Instant?,
    endTime: Instant?,
    onStartTimeSelected: (Instant) -> Unit,
    onEndTimeSelected: (Instant) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 開始時刻
        DateTimePickerField(
            label = "開始時刻",
            selectedTime = startTime,
            onTimeSelected = onStartTimeSelected
        )

        // 終了時刻
        DateTimePickerField(
            label = "終了時刻",
            selectedTime = endTime,
            onTimeSelected = onEndTimeSelected,
            enabled = startTime != null // 開始時刻が選択されてから有効化
        )
    }
}

@Composable
private fun DateTimePickerField(
    label: String,
    selectedTime: Instant?,
    onTimeSelected: (Instant) -> Unit,
    enabled: Boolean = true
) {
    var showDialog by remember { mutableStateOf(false) }

    val formatter = remember {
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
            .withZone(ZoneId.systemDefault())
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { showDialog = true },
        border = BorderStroke(1.dp, if (enabled) Color.Gray else Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = AppTheme.typography.label1,
                color = if (enabled) Color.Gray else Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = selectedTime?.let { formatter.format(it) } ?: "未選択",
                style = AppTheme.typography.body2,
                color = if (selectedTime != null) {
                    Color.Black
                } else {
                    Color.Gray
                }
            )
        }
    }

    if (showDialog) {
        DateTimePickerDialog(
            initialTime = selectedTime,
            onDismiss = { showDialog = false },
            onConfirm = { instant ->
                onTimeSelected(instant)
                showDialog = false
            }
        )
    }
}

@Composable
private fun DateTimePickerDialog(
    initialTime: Instant?,
    onDismiss: () -> Unit,
    onConfirm: (Instant) -> Unit
) {
    val context = LocalContext.current
    val calendar = remember {
        Calendar.getInstance().apply {
            if (initialTime != null) {
                time = Date.from(initialTime)
            }
            // 分と秒を0にする（1時間単位）
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    var selectedDate by remember {
        mutableStateOf(calendar.time)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "日時を選択",
                    style = AppTheme.typography.h2
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 日付選択ボタン
                Button(
                    onClick = {
                        showDatePicker(
                            context = context,
                            initialDate = calendar,
                            onDateSelected = { year, month, day ->
                                calendar.set(year, month, day)
                                selectedDate = calendar.time
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(Icons.Default.DateRange, contentDescription = null)
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                .format(selectedDate)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 時刻選択ボタン
                Button(
                    onClick = {
                        showTimePicker(
                            context = context,
                            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                            onTimeSelected = { hour ->
                                calendar.set(Calendar.HOUR_OF_DAY, hour)
                                calendar.set(Calendar.MINUTE, 0)
                                selectedDate = calendar.time
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null)
                        Spacer(Modifier.width(20.dp))
                        Text(
                            text = SimpleDateFormat("HH:00", Locale.getDefault())
                                .format(selectedDate)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        variant = ButtonVariant.Ghost,
                        onClick = onDismiss
                    ) {
                        Text("キャンセル")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(calendar.time.toInstant())
                        }
                    ) {
                        Text("決定")
                    }
                }
            }
        }
    }
}

private fun showDatePicker(
    context: Context,
    initialDate: Calendar,
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit
) {
    DatePickerDialog(
        context,
        { _, year, month, day ->
            onDateSelected(year, month, day)
        },
        initialDate.get(Calendar.YEAR),
        initialDate.get(Calendar.MONTH),
        initialDate.get(Calendar.DAY_OF_MONTH)
    ).show()
}

private fun showTimePicker(
    context: Context,
    initialHour: Int,
    onTimeSelected: (hour: Int) -> Unit
) {
    TimePickerDialog(
        context,
        { _, hour, _ ->
            // 分は常に0（1時間単位）
            onTimeSelected(hour)
        },
        initialHour,
        0, // 初期値は0分
        true // 24時間表示
    ).show()
}
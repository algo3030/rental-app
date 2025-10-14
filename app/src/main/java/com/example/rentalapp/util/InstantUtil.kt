package com.example.rentalapp.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.asDeadlineDisplay(): String{
    return this.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM/dd HH:mm"))
}
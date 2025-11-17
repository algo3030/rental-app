package com.example.rentalapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class RentalRequestDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("pc_id")
    val pcId: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    val status: String,
    @SerialName("requested_at")
    val requestedAt: String,
    @SerialName("checked_out_at")
    val checkedOutAt: String? = null,
    @SerialName("returned_at")
    val returnedAt: String? = null,
    @SerialName("cancelled_at")
    val cancelledAt: String? = null
)

@Serializable
data class PcDto(
    val id: String,
    @SerialName("model_id")
    val modelId: String,
    @SerialName("pc_number")
    val pcNumber: String,
    @SerialName("serial_number")
    val serialNumber: String? = null,
    val status: String
)

@Serializable
data class PcModelDto(
    val id: String,
    @SerialName("model_name")
    val modelName: String,
    val manufacturer: String? = null,
    val specs: JsonObject? = null,
    @SerialName("image_path")
    val imagePath: String? = null
)
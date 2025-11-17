package com.example.rentalapp.data

import com.example.rentalapp.model.Pc
import com.example.rentalapp.model.PcModel
import com.example.rentalapp.model.PcStatus
import com.example.rentalapp.model.RentalRequest
import com.example.rentalapp.model.RentalRequestStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.time.Instant
import javax.inject.Inject

class RentalRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getActiveRental(userId: String): RentalRequest? {
        val dto = supabase.from("rental_requests")
            .select {
                filter {
                    eq("user_id", userId)
                    isIn("status", listOf("pending", "checked_out", "overdue"))
                }
            }.decodeList<RentalRequestDto>().firstOrNull()
        return dto?.toDomainModel()
    }

    suspend fun cancelRequest(requestId: String): Result<Unit> {
        return runCatching {
            supabase.from("rental_requests")
                .update({
                    set("status", "cancelled")
                }) {
                    filter {
                        eq("id", requestId)
                    }
                }
        }
    }

    private suspend fun RentalRequestDto.toDomainModel(): RentalRequest {
        val pc = fetchPc(pcId)

        return RentalRequest(
            id = id,
            userId = userId,
            pc = pc,
            startTime = Instant.parse(startTime),
            endTime = Instant.parse(endTime),
            status = status.toRentalRequestStatus(),
            requestedAt = Instant.parse(requestedAt),
            checkedOutAt = checkedOutAt?.let { Instant.parse(it) },
            returnedAt = returnedAt?.let { Instant.parse(it) },
            cancelledAt = cancelledAt?.let { Instant.parse(it) }
        )
    }

    private suspend fun fetchPc(pcId: String): Pc {
        val pcDto = supabase.from("pcs")
            .select {
                filter {
                    eq("id", pcId)
                }
            }
            .decodeSingle<PcDto>()

        val model = fetchPcModel(pcDto.modelId)

        return Pc(
            id = pcDto.id,
            model = model,
            pcNumber = pcDto.pcNumber,
            serialNumber = pcDto.serialNumber,
            status = pcDto.status.toPcStatus()
        )
    }

    private suspend fun fetchPcModel(modelId: String): PcModel {
        val modelDto = supabase.from("pc_models")
            .select {
                filter {
                    eq("id", modelId)
                }
            }
            .decodeSingle<PcModelDto>()

        return PcModel(
            id = modelDto.id,
            modelName = modelDto.modelName,
            manufacturer = modelDto.manufacturer,
            specs = modelDto.specs?.toMap(),
            imagePath = modelDto.imagePath
        )
    }

    private fun String.toRentalRequestStatus(): RentalRequestStatus {
        return when (this) {
            "pending" -> RentalRequestStatus.PENDING
            "checked_out" -> RentalRequestStatus.CHECKED_OUT
            "overdue" -> RentalRequestStatus.OVERDUE
            "returned" -> RentalRequestStatus.RETURNED
            "cancelled" -> RentalRequestStatus.CANCELLED
            else -> RentalRequestStatus.PENDING
        }
    }

    private fun String.toPcStatus(): PcStatus {
        return when (this) {
            "available" -> PcStatus.AVAILABLE
            "maintenance" -> PcStatus.MAINTENANCE
            "retired" -> PcStatus.RETIRED
            else -> PcStatus.AVAILABLE
        }
    }
}
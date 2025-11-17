package com.example.rentalapp.data

import com.example.rentalapp.model.Pc
import com.example.rentalapp.model.PcModel
import com.example.rentalapp.model.PcStatus
import com.example.rentalapp.model.RentalRequest
import com.example.rentalapp.model.RentalRequestStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
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

    // 新規追加: 利用可能なPC一覧を取得
    suspend fun getAvailablePcs(): List<Pc> {
        val pcDtos = supabase.from("pcs")
            .select {
                filter {
                    eq("status", "available")
                }
            }
            .decodeList<PcDto>()

        return pcDtos.map { it.toDomainModel() }
    }

    // 新規追加: レンタル申請を作成
    suspend fun createRentalRequest(
        userId: String,
        pcId: String,
        startTime: Instant,
        endTime: Instant
    ): Result<RentalRequest> {
        return runCatching {
            val dto = supabase.from("rental_requests")
                .insert(
                    mapOf(
                        "user_id" to userId,
                        "pc_id" to pcId,
                        "start_time" to startTime.toString(),
                        "end_time" to endTime.toString(),
                        "status" to "pending"
                    )
                ) {
                    select()
                }
                .decodeSingle<RentalRequestDto>()

            dto.toDomainModel()
        }
    }

    // 新規追加: 画像URLを取得
    fun getPcImageUrl(imagePath: String?): String? {
        return imagePath?.let { path ->
            supabase.storage.from("pc-images").publicUrl(path)
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

    private suspend fun PcDto.toDomainModel(): Pc {
        val model = fetchPcModel(modelId)

        return Pc(
            id = id,
            model = model,
            pcNumber = pcNumber,
            serialNumber = serialNumber,
            status = status.toPcStatus()
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
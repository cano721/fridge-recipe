package com.fridgerecipe.shared.domain.repository

import com.fridgerecipe.shared.data.remote.dto.ScanStatusResponse

interface ScanRepository {
    suspend fun scanReceipt(imageBase64: String): Result<ScanStatusResponse>
    suspend fun getReceiptResult(scanId: Long): Result<ScanStatusResponse>
    suspend fun scanPhoto(imageBase64: String): Result<ScanStatusResponse>
    suspend fun getPhotoResult(scanId: Long): Result<ScanStatusResponse>
}

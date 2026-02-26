package com.fridgerecipe.domain.service

import com.fridgerecipe.core.client.AiServiceClient
import com.fridgerecipe.core.client.toDomain
import com.fridgerecipe.domain.model.ScanResult
import com.fridgerecipe.domain.repository.ScanRepository

class ScanService(
    private val scanRepository: ScanRepository,
    private val aiServiceClient: AiServiceClient
) {
    suspend fun receiptScan(userId: Long, imageUrl: String): Long {
        val scanId = scanRepository.create(userId, "receipt", imageUrl)
        runCatching {
            aiServiceClient.requestReceiptOcr(imageUrl)
        }.onFailure {
            scanRepository.updateStatus(scanId, "failed", null, it.message)
        }
        return scanId
    }

    suspend fun photoScan(userId: Long, imageUrl: String): Long {
        val scanId = scanRepository.create(userId, "photo", imageUrl)
        runCatching {
            aiServiceClient.requestVisionIngredients(imageUrl)
        }.onFailure {
            scanRepository.updateStatus(scanId, "failed", null, it.message)
        }
        return scanId
    }

    suspend fun getScanResult(scanId: Long, userId: Long): ScanResult? {
        val scan = scanRepository.findById(scanId) ?: return null
        if (scan.userId != userId) return null

        if (scan.status != "processing") return scan

        val updated = when (scan.scanType) {
            "receipt" -> pollReceiptResult(scanId, scan.imageUrl ?: "")
            "photo" -> pollPhotoResult(scanId, scan.imageUrl ?: "")
            else -> null
        }
        return updated ?: scanRepository.findById(scanId)
    }

    private suspend fun pollReceiptResult(scanId: Long, imageUrl: String): ScanResult? {
        val taskId = runCatching {
            aiServiceClient.requestReceiptOcr(imageUrl)
        }.getOrNull() ?: run {
            scanRepository.updateStatus(scanId, "failed", null, "AI 서비스 요청 실패")
            return scanRepository.findById(scanId)
        }

        val result = runCatching {
            aiServiceClient.getReceiptOcrResult(taskId)
        }.getOrNull() ?: return null

        return when (result.status) {
            "done" -> {
                val items = result.items?.map { it.toDomain() }
                scanRepository.updateStatus(scanId, "done", items, null)
                scanRepository.findById(scanId)
            }
            "failed" -> {
                scanRepository.updateStatus(scanId, "failed", null, result.errorMessage)
                scanRepository.findById(scanId)
            }
            else -> scanRepository.findById(scanId)
        }
    }

    private suspend fun pollPhotoResult(scanId: Long, imageUrl: String): ScanResult? {
        val taskId = runCatching {
            aiServiceClient.requestVisionIngredients(imageUrl)
        }.getOrNull() ?: run {
            scanRepository.updateStatus(scanId, "failed", null, "AI 서비스 요청 실패")
            return scanRepository.findById(scanId)
        }

        val result = runCatching {
            aiServiceClient.getVisionIngredientsResult(taskId)
        }.getOrNull() ?: return null

        return when (result.status) {
            "done" -> {
                val items = result.items?.map { it.toDomain() }
                scanRepository.updateStatus(scanId, "done", items, null)
                scanRepository.findById(scanId)
            }
            "failed" -> {
                scanRepository.updateStatus(scanId, "failed", null, result.errorMessage)
                scanRepository.findById(scanId)
            }
            else -> scanRepository.findById(scanId)
        }
    }
}

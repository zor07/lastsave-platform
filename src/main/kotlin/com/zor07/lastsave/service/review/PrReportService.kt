package com.zor07.lastsave.service.review

import com.zor07.lastsave.dto.review.PrReportRequest

interface PrReportService {
    fun handlePrReport(request: PrReportRequest)
}

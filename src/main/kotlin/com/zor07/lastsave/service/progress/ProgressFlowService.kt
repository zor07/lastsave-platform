package com.zor07.lastsave.service.progress

import com.zor07.lastsave.model.Student

interface ProgressFlowService {
    fun process(student: Student)
}

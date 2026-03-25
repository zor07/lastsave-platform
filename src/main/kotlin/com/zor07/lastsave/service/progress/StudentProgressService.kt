package com.zor07.lastsave.service.progress

import com.zor07.lastsave.model.Block
import com.zor07.lastsave.model.Student

interface StudentProgressService {
    fun startProgress(student: Student)
    fun startBlock(student: Student, block: Block)
}

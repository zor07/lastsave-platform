package com.zor07.lastsave.service.messaging

import com.zor07.lastsave.model.Student

interface MessageDispatchService {
    fun advanceIfPossible(student: Student)
}

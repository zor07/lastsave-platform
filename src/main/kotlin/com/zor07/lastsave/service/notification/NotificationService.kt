package com.zor07.lastsave.service.notification

import com.zor07.lastsave.model.Message
import com.zor07.lastsave.model.Student

interface NotificationService {
    fun sendText(student: Student, text: String)
    fun sendMessage(student: Student, message: Message)
}

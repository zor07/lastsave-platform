package com.zor07.lastsave.scheduler

import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.messaging.MessageDispatchService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProgressScheduler(
    private val studentRepository: StudentRepository,
    private val messageDispatchService: MessageDispatchService,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 60_000)
    fun tick() {
        val students = studentRepository.findAllWithActiveProgress()
        logger.info("=== Scheduler tick: {} active student(s) ===", students.size)
        if (students.isEmpty()) return
        students.forEach { student ->
            logger.info("Processing student {} (chatId={})", student.id, student.telegramChatId)
            messageDispatchService.advanceIfPossible(student)
        }
        logger.info("=== Scheduler tick complete ===")
    }
}

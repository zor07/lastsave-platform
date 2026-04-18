package com.zor07.lastsave.scheduler

import com.zor07.lastsave.repository.StudentRepository
import com.zor07.lastsave.service.progress.ProgressFlowService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProgressScheduler(
    private val studentRepository: StudentRepository,
    private val progressFlowService: ProgressFlowService,
    @Value("\${scheduler.enabled}") private val enabled: Boolean,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    fun tick() {
        if (!enabled) {
            logger.debug("Scheduler is disabled, skipping tick")
            return
        }
        doTick()
    }

    @Transactional
    fun doTick() {
        val students = studentRepository.findAllWithActiveProgress()
        logger.info("=== Scheduler tick: {} active student(s) ===", students.size)
        if (students.isEmpty()) return
        students.forEach { student ->
            logger.info("Processing student {} (chatId={})", student.id, student.telegramChatId)
            progressFlowService.process(student)
        }
        logger.info("=== Scheduler tick complete ===")
    }
}

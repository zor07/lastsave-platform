package com.zor07.lastsave.service.ci

import com.zor07.lastsave.repository.StudentProgressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CiServiceImpl(
    private val studentProgressRepository: StudentProgressRepository,
) : CiService {

    @Transactional(readOnly = true)
    override fun getTestTag(githubUsername: String): String? =
        studentProgressRepository.findActivePosition(githubUsername)
            ?.let { "${it.blockCode}.${it.topicCode}" }
}

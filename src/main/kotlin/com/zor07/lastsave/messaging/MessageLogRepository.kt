package com.zor07.lastsave.messaging

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageLogRepository : JpaRepository<MessageLog, Long>

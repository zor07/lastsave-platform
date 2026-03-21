package com.zor07.lastsave.student

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentProgressRepository : JpaRepository<StudentProgress, Long>

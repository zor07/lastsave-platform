package com.zor07.lastsave.repository

import com.zor07.lastsave.entity.StudentProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentProgressRepository : JpaRepository<StudentProgress, Long>

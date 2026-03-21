package com.zor07.lastsave.course

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialRepository : JpaRepository<Material, Long>

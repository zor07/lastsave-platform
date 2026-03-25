package com.zor07.lastsave.repository

import com.zor07.lastsave.enums.UnlockCondition
import com.zor07.lastsave.model.Section
import com.zor07.lastsave.table.SectionsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class SectionRepository {

    fun findById(id: Long): Section? =
        SectionsTable.selectAll()
            .where { SectionsTable.id eq id }
            .singleOrNull()
            ?.toSection()

    fun findNextInTopic(topicId: Long, afterOrder: Int): Section? =
        SectionsTable.selectAll()
            .where { (SectionsTable.topicId eq topicId) and (SectionsTable.order greater afterOrder) }
            .orderBy(SectionsTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toSection()

    fun findFirstInTopic(topicId: Long): Section? =
        SectionsTable.selectAll()
            .where { SectionsTable.topicId eq topicId }
            .orderBy(SectionsTable.order to SortOrder.ASC)
            .limit(1)
            .singleOrNull()
            ?.toSection()

    private fun ResultRow.toSection() = Section(
        id = this[SectionsTable.id],
        topicId = this[SectionsTable.topicId],
        title = this[SectionsTable.title],
        order = this[SectionsTable.order],
        unlockCondition = UnlockCondition.valueOf(this[SectionsTable.unlockCondition]),
    )
}

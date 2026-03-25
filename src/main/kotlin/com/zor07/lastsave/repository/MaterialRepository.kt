package com.zor07.lastsave.repository

import com.zor07.lastsave.enums.MaterialType
import com.zor07.lastsave.model.Material
import com.zor07.lastsave.table.MaterialsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class MaterialRepository {

    fun findBySectionId(sectionId: Long): List<Material> =
        MaterialsTable.selectAll()
            .where { MaterialsTable.sectionId eq sectionId }
            .orderBy(MaterialsTable.order to SortOrder.ASC)
            .map { it.toMaterial() }

    private fun ResultRow.toMaterial() = Material(
        id = this[MaterialsTable.id],
        sectionId = this[MaterialsTable.sectionId],
        type = MaterialType.valueOf(this[MaterialsTable.type]),
        title = this[MaterialsTable.title],
        url = this[MaterialsTable.url],
        order = this[MaterialsTable.order],
    )
}

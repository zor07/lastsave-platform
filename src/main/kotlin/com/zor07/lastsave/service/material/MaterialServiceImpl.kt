package com.zor07.lastsave.service.material

import com.zor07.lastsave.enums.MaterialType
import com.zor07.lastsave.model.Material
import com.zor07.lastsave.repository.MaterialRepository
import org.springframework.stereotype.Service

@Service
class MaterialServiceImpl(
    private val materialRepository: MaterialRepository,
) : MaterialService {

    override fun getSectionMaterials(sectionId: Long): List<Material> =
        materialRepository.findBySectionId(sectionId)

    override fun formatMessage(materials: List<Material>): String {
        val sb = StringBuilder()
        val byType = materials.groupBy { it.type }

        byType[MaterialType.THEORY]?.let { items ->
            sb.appendLine("Теория по текущему разделу:")
            sb.appendLine()
            items.forEach { sb.appendLine("${it.title}: ${it.url}") }
        }

        byType[MaterialType.PRACTICE]?.let { items ->
            if (sb.isNotEmpty()) sb.appendLine()
            sb.appendLine("Техническое задание:")
            sb.appendLine()
            items.forEach { sb.appendLine("${it.title}: ${it.url}") }
        }

        byType[MaterialType.VIDEO]?.let { items ->
            if (sb.isNotEmpty()) sb.appendLine()
            sb.appendLine("Посмотри видео:")
            sb.appendLine()
            items.forEach { sb.appendLine("${it.title}: ${it.url}") }
        }

        return sb.toString().trimEnd()
    }
}

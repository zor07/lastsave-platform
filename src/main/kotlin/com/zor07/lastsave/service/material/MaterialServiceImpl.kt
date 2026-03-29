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

        byType[MaterialType.TEXT]?.let { texts ->
            sb.appendLine("Почитай документацию:")
            sb.appendLine()
            texts.forEach { sb.appendLine("${it.title}: ${it.url}") }
        }

        byType[MaterialType.VIDEO]?.let { videos ->
            if (sb.isNotEmpty()) sb.appendLine()
            sb.appendLine("Посмотри видео:")
            sb.appendLine()
            videos.forEach { sb.appendLine("${it.title}: ${it.url}") }
        }

        return sb.toString().trimEnd()
    }
}

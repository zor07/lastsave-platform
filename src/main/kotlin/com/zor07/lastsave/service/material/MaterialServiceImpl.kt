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
        val sb = StringBuilder("Материалы к разделу:\n\n")
        materials.forEach { material ->
            val prefix = when (material.type) {
                MaterialType.VIDEO -> "Посмотри видео"
                MaterialType.TEXT  -> "Почитай документацию"
            }
            sb.appendLine("$prefix: [${material.title}](${material.url})")
        }
        return sb.toString().trimEnd()
    }
}

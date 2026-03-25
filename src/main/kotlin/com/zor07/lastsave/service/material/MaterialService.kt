package com.zor07.lastsave.service.material

import com.zor07.lastsave.model.Material

interface MaterialService {
    fun getSectionMaterials(sectionId: Long): List<Material>
    fun formatMessage(materials: List<Material>): String
}

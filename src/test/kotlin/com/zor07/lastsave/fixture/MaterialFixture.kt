package com.zor07.lastsave.fixture

import com.zor07.lastsave.enums.MaterialType
import com.zor07.lastsave.model.Material

object MaterialFixture {
    fun material(
        id: Long = 1L,
        sectionId: Long = 10L,
        type: MaterialType = MaterialType.TEXT,
        title: String = "title",
        url: String = "https://example.com",
        order: Int = 1,
    ) = Material(
        id = id,
        sectionId = sectionId,
        type = type,
        title = title,
        url = url,
        order = order,
    )
}

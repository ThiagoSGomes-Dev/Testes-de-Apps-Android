package com.rocketseat.rocketia.data

import com.rocketseat.rocketia.data.local.database.AIChatTextEntity
import com.rocketseat.rocketia.domain.model.AIChatTextType

fun createAIChatTextEntityStub(from: AIChatTextType?, text: String = "text"): AIChatTextEntity =
    AIChatTextEntity(
        from = from?.name.orEmpty(),
        stack = "stack",
        datetime = 0L,
        text = text
    )
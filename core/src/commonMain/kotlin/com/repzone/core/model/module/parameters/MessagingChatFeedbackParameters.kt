package com.repzone.core.model.module.parameters

import com.repzone.core.model.module.base.IModuleParametersBase

data class MessagingChatFeedbackParameters(
    override val isActive: Boolean,
    val messaging: Boolean,
    val feedback: Boolean
): IModuleParametersBase

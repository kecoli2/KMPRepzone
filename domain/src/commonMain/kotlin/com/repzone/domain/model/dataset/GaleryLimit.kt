package com.repzone.domain.model.dataset

data class GaleryLimit(
    var maxCount: Int = 0,
    var maxLimit: Int = 0,
    var useOnlyCamera: Boolean = false,
    var saveToAlbum: Boolean = false
)
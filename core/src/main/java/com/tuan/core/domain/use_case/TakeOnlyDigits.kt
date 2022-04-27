package com.tuan.core.domain.use_case

/**
 * This class is injected in app module
 */
class TakeOnlyDigits {

    /**
     * Filter a text so that it only contains digits
     *
     * @param text the text that needs to be filtered
     * @return the text only contains digits
     */
    operator fun invoke(text: String): String {
        return text.filter { character ->
            character.isDigit()
        }
    }
}
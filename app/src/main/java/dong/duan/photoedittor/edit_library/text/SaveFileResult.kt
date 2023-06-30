package dong.duan.photoedittor.edit_library.text

import java.io.IOException

sealed interface SaveFileResult {

    object Success : SaveFileResult
    class Failure(val exception: IOException) : SaveFileResult

}
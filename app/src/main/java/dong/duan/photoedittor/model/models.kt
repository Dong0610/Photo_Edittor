package dong.duan.photoedittor.model

import android.graphics.Bitmap
import java.io.Serializable

data class ImageData(
    val id: Long,
    val name: String,
    val path: String,
    val folderName: String,
    val size: Long, var bitmap: Bitmap?
):Serializable

data class EditData(var id:Int,var name:String,var icon:Int)

package dong.duan.photoedittor.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ImageData(
    val id: Long,
    val name: String,
    val path: String,
    val folderName: String,
    val size: Long, var bitmap: Bitmap?
):Serializable


data class ImageEdit(
    val id: Long,
    val name: String,
    val bitmap: Bitmap?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",

        parcel.readParcelable(Bitmap::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeParcelable(bitmap, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageEdit> {
        override fun createFromParcel(parcel: Parcel): ImageEdit {
            return ImageEdit(parcel)
        }

        override fun newArray(size: Int): Array<ImageEdit?> {
            return arrayOfNulls(size)
        }
    }
}
data class EditData(var id:Int,var name:String,var icon:Int)

object ImageDataHolder {
    var imageData: ImageData? = null
}
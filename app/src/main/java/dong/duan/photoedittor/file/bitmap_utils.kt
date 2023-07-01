package dong.duan.photoedittor.file

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

fun bitmap_from_uri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
 fun bitmap_to_file(bitmap: Bitmap,activity: Activity): File {
    val file = File(activity.getExternalFilesDir(null), "image.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    return file
}
fun uri_from_bitmap(context: Context, bitmap: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val resolver = context.contentResolver
        uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            val outputStream = resolver.openOutputStream(uri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return uri
}
fun uri_from_bitmap(context: Context, bitmap: Bitmap, urical: (Uri?) -> Unit) {
    var uri: Uri? = null
    try {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val resolver = context.contentResolver
        uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Invoke the callback with the generated Uri
        urical(uri)

        val outputStream = resolver.openOutputStream(uri!!)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream?.close()

        // Delete the file associated with the Uri
        resolver.delete(uri, null, null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}







fun uri_from_id(id: Long): Uri?{
    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    return ContentUris.withAppendedId(contentUri, id.toLong())
}

fun bitmap_from_id(id:Long,context: Context):Bitmap {
    val contentResolver: ContentResolver = context.contentResolver
    val uri: Uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
    val inputStream = contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}


fun bitmap_from_id(id:Int,context: Context):Bitmap {
    return BitmapFactory.decodeResource(context.resources,id)
}
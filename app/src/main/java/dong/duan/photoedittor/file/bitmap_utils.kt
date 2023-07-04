package dong.duan.photoedittor.file

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun bitmap_from_uri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun bitmap_to_file(bitmap: Bitmap, activity: Activity): File {
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

private fun file_extension(uri: Uri,context: Context): String? {
    val cr: ContentResolver =context.getContentResolver()
    val mime = MimeTypeMap.getSingleton()
    return mime.getExtensionFromMimeType(cr.getType(uri))
}

private fun current_time(): String {
    val dateFormat = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
    val currentTime = Calendar.getInstance().time
    return dateFormat.format(currentTime)
}

 fun save_bitmap_image(bitmap: Bitmap, applicationContext: Context) {
    val fileName = "${current_time()}.jpg" // Specify the desired file name and extension
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    try {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val imageFile = File(storageDir, fileName)
        val fileOutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()
        show_toast(applicationContext, "Image saved successfully!")
    } catch (e: Exception) {
        e.printStackTrace()
        show_toast(applicationContext, "Failed to save image.")
    }
}


 fun save_image(context: Context, Final_bitmap: Bitmap): File? {
    val pictureFileDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "Photo Edit"
    )
    if (!pictureFileDir.exists()) {
        val isDirectoryCreated = pictureFileDir.mkdirs()
        if (!isDirectoryCreated) Log.i("TAG", "Can't create directory to save the image")
        return null
    }
    val filename = pictureFileDir.path + File.separator + System.currentTimeMillis() + ".jpg"
    val pictureFile = File(filename)
    try {
        pictureFile.createNewFile()
        val oStream = FileOutputStream(pictureFile)
        Final_bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream)
        oStream.flush()
        oStream.close()
        Toast.makeText(context, "Save Image Successfully..", Toast.LENGTH_SHORT)
            .show()
    } catch (e: IOException) {
        e.printStackTrace()
        Log.i("TAG", "There was an issue saving the image.")
    }
    scanGallery(context, pictureFile.absolutePath)
    return pictureFile
}
fun save_image_photo(bitmap: Bitmap,context: Context) {
    //Generating a file name
    val filename = "${System.currentTimeMillis()}.jpg"

    //Output stream
    var fos: OutputStream? = null

    //For devices running android >= Q
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //getting the contentResolver
        context?.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }

    fos?.use {
        //Finally writing the bitmap to the output stream that we opened
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        show_toast(context,"Saved to Photos")
    }
}
private fun scanGallery(cntx: Context, path: String) {
    try {
        MediaScannerConnection.scanFile(
            cntx, arrayOf<String>(path), null
        ) { path, uri ->
            Toast.makeText(
                cntx,
                "Save Image Successfully..",
                Toast.LENGTH_SHORT
            ).show()
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        Log.i("TAG", "There was an issue scanning gallery.")
    }
}

fun uri_from_id(id: Long): Uri? {
    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    return ContentUris.withAppendedId(contentUri, id.toLong())
}

fun bitmap_from_id(id: Long, context: Context): Bitmap {
    val contentResolver: ContentResolver = context.contentResolver
    val uri: Uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
    val inputStream = contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}


fun bitmap_from_id(id: Int, context: Context): Bitmap {
    return BitmapFactory.decodeResource(context.resources, id)
}
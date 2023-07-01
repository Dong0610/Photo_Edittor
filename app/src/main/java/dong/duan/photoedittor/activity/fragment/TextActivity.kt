package dong.duan.photoedittor.activity.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import dong.duan.photoedittor.activity.BaseActivity
import dong.duan.photoedittor.databinding.ActivityTextBinding
import dong.duan.photoedittor.edit_library.text.FileSaveHelper
import dong.duan.photoedittor.edit_library.text.OnPhotoEditorListener
import dong.duan.photoedittor.edit_library.text.OverlayTextFragment
import dong.duan.photoedittor.edit_library.text.PhotoEditor
import dong.duan.photoedittor.edit_library.text.SaveSettings
import dong.duan.photoedittor.edit_library.text.TextStyleBuilder
import dong.duan.photoedittor.edit_library.text.ViewType
import dong.duan.photoedittor.file.bitmap_from_uri
import dong.duan.photoedittor.file.bitmap_to_file
import dong.duan.photoedittor.file.log
import dong.duan.photoedittor.file.show_toast
import dong.duan.photoedittor.file.uri_from_bitmap
import dong.duan.photoedittor.model.ImageEdit
import kotlinx.coroutines.launch
import java.io.File

@Suppress("DEPRECATION")
class TextActivity : BaseActivity(), OnPhotoEditorListener {
    val binding by lazy {
        ActivityTextBinding.inflate(layoutInflater)
    }
    lateinit var mPhotoEditor: PhotoEditor
    lateinit var image_edit: ImageEdit
    lateinit var bitmap_result: Bitmap
    private lateinit var mSaveFileHelper: FileSaveHelper

    @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
      val filepath=  intent.getStringExtra("bimap")
         bitmap_result = BitmapFactory.decodeFile(filepath)
        val pinchTextScalable = intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)
        mPhotoEditor = PhotoEditor.Builder(this, binding.drawview)
            .setPinchTextScalable(pinchTextScalable)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)
        mSaveFileHelper= FileSaveHelper(this)
        binding.drawview.source.setImageBitmap(bitmap_result)

        binding.icAddtext.setOnClickListener {
            val text_edtfg = OverlayTextFragment.show(this)
            text_edtfg.setOntextEdittorEvent(object : OverlayTextFragment.TextEditorEvent {
                override fun onDone(input: String, color: Int) {
                    val textStyle = TextStyleBuilder()
                    textStyle.withTextColor(color)
                    mPhotoEditor.addText(input, color)
                }

            })
        }

        binding.txtSave.setOnClickListener {
            saveImage { bitmap ->
                val resultIntent = Intent()
                var file= bitmap_to_file(bitmap,this)
                if(file==null){
                    show_toast(this,"File is null",)
                }

                resultIntent.putExtra("image",file.absolutePath)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()

            }

            deleteAllJpgFilesInPicturesFolder()
        }



    //    openDirectory()
    }

    private fun deleteFilesInDirectory(directoryUri: Uri) {
        val resolver = contentResolver
        resolver.takePersistableUriPermission(
            directoryUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )

        val directory = DocumentFile.fromTreeUri(this, directoryUri)
        deleteRecursive(directory)
    }

    private fun deleteRecursive(documentFile: DocumentFile?) {
        if (documentFile != null) {
            if (documentFile.isDirectory) {
                val files = documentFile.listFiles()
                if (files != null) {
                    for (file in files) {
                        deleteRecursive(file)
                    }
                }
            }
            documentFile.delete()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file deletion
                deleteAllFilesInPath("/storage/emulated/0/Pictures/")
            } else {
                // Permission denied
                // Handle the case where the user denied the permission
            }
        }
    }


    @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    private fun saveImage(bitmapcal: (Bitmap) -> Unit) {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val hasStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (hasStoragePermission || FileSaveHelper.isSdkHigherThan28()) {
            mSaveFileHelper.createFile(fileName, object : FileSaveHelper.OnFileCreateResult {

                @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
                override fun onFileCreateResult(
                    created: Boolean,
                    filePath: String?,
                    error: String?,
                    uri: Uri?
                ) {
                    lifecycleScope.launch {
                        if (created && filePath != null) {
                            val saveSettings = SaveSettings.Builder()
                                .setClearViewsEnabled(true)
                                .setTransparencyEnabled(true)
                                .build()
                            var bitmap = mPhotoEditor.saveAsBitmap(saveSettings)
                            bitmapcal(bitmap)
                            log("a","pathImg:"+ filePath.toString())
                            deleteAllJpgFilesInPicturesFolder()
                            val path = "/storage/emulated/0/Pictures/"
                            deleteAllFilesInPath(path)
                        } else {
                            Toast.makeText(applicationContext, "Cant not edit", Toast.LENGTH_LONG)
                                .show()
                        }
                    }



                }
            })


        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
    fun deleteAllFilesInPath(path: String) {
        val directory = File(path)
        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        file.delete()
                    }
                }
            }
        }
    }

        private fun deleteAllJpgFilesInPicturesFolder() {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val jpgFiles = picturesDir.listFiles { dir, name ->
                name.endsWith(".jpg", true)
            }

            if (jpgFiles != null) {
                for (file in jpgFiles) {
                    if (file.isFile && file.exists()) {
                        file.delete()
                    }
                }
            }
        }

    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
        val textEditorDialogFragment =
            OverlayTextFragment.show(this, text.toString(), colorCode)
        textEditorDialogFragment.setOntextEdittorEvent(object :
            OverlayTextFragment.TextEditorEvent {
            override fun onDone(inputText: String, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                mPhotoEditor.editText(rootView, inputText, styleBuilder)
                //mTxtCurrentTool.setText(R.string.label_text)
            }
        })
    }

    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {

    }

    override fun onStartViewChangeListener(viewType: ViewType) {

    }

    override fun onStopViewChangeListener(viewType: ViewType) {

    }

    override fun onTouchSourceImage(event: MotionEvent) {

    }

    companion object {

        private const val TAG = "EditImageActivity"

        const val FILE_PROVIDER_AUTHORITY = "com.burhanrashid52.photoediting.fileprovider"
        private const val CAMERA_REQUEST = 52
        private const val PICK_REQUEST = 53
        const val ACTION_NEXTGEN_EDIT = "action_nextgen_edit"
        const val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    }
}
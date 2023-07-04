package dong.duan.photoedittor.activity.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dong.duan.photoedittor.databinding.ActivityTextBinding
import dong.duan.photoedittor.edit_library.text.FileSaveHelper
import dong.duan.photoedittor.edit_library.text.OnPhotoEditorListener
import dong.duan.photoedittor.edit_library.text.OverlayTextFragment
import dong.duan.photoedittor.edit_library.text.PhotoEditor
import dong.duan.photoedittor.edit_library.text.SaveSettings
import dong.duan.photoedittor.edit_library.text.TextStyleBuilder
import dong.duan.photoedittor.edit_library.text.ViewType
import dong.duan.photoedittor.file.BaseActivity
import dong.duan.photoedittor.file.bitmap_to_file
import dong.duan.photoedittor.file.log
import dong.duan.photoedittor.file.show_toast
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
        val filepath = intent.getStringExtra("bitmap")
        bitmap_result = BitmapFactory.decodeFile(filepath)
        val pinchTextScalable = intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)
        mPhotoEditor = PhotoEditor.Builder(this, binding.drawview)
            .setPinchTextScalable(pinchTextScalable)
            .build()
        mPhotoEditor.setOnPhotoEditorListener(this)
        mSaveFileHelper = FileSaveHelper(this)
        binding.drawview.source.setImageBitmap(bitmap_result)

        binding.icAddtext.setOnClickListener {
            val text_edtfg = OverlayTextFragment.show(this)
            text_edtfg.setOntextEdittorEvent(object : OverlayTextFragment.TextEditorEvent {
                @RequiresApi(Build.VERSION_CODES.O)
                @SuppressLint("WrongConstant")
                override fun onDone(input: String, color: Int, value: Int?) {
                    val textStyle = TextStyleBuilder()

                    textStyle.withTextColor(color)
                    if (value != null) {
                        val typeface = resources.getFont(value)
                        textStyle.withTextStyle(typeface)
                        mPhotoEditor.addText(typeface, input, color)
                    } else {
                        mPhotoEditor.addText(input, color)
                    }

                }

            })
        }

        binding.txtSave.setOnClickListener {
            saveImage { bitmap ->
                val resultIntent = Intent()
                var file = bitmap_to_file(bitmap, this)
                if (file == null) {
                    show_toast(this, "File is null")
                }

                resultIntent.putExtra("bitmap", file.absolutePath)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

            deleteAllJpgFilesInPicturesFolder()
        }
        binding.icUndo.setOnClickListener {
            mPhotoEditor.undo()
        }
        binding.icRedo.setOnClickListener {
            mPhotoEditor.redo()
        }

        binding.txtCancel2.setOnClickListener {
            onBackPressed()
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
                            log("a", "pathImg:" + filePath.toString())
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
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
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

// Region
    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
        val textEditorDialogFragment =
            OverlayTextFragment.show(this, text.toString(), colorCode)
        textEditorDialogFragment.setOntextEdittorEvent(object :
            OverlayTextFragment.TextEditorEvent {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("WrongConstant")
            override fun onDone(inputText: String, colorCode: Int, value: Int?) {
                               val textStyle = TextStyleBuilder()
                textStyle.withTextColor(colorCode)
                if (value != null) {
                    val typeface = resources.getFont(value)
                    textStyle.withTextStyle(typeface)
                    mPhotoEditor.editText(rootView,typeface, inputText, colorCode)
                }
                else{
                    mPhotoEditor.editText(rootView,inputText,textStyle)
                }

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

        const val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    }
    // End Region
}
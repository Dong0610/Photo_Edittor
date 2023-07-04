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
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.burhanrashid52.photoediting.StickerBSFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dong.duan.photoedittor.databinding.ActivityImageBinding

import dong.duan.photoedittor.edit_library.text.FileSaveHelper
import dong.duan.photoedittor.edit_library.text.OnPhotoEditorListener
import dong.duan.photoedittor.edit_library.text.OverlayTextFragment
import dong.duan.photoedittor.edit_library.text.PhotoEditor
import dong.duan.photoedittor.edit_library.text.SaveSettings
import dong.duan.photoedittor.edit_library.text.TextStyleBuilder
import dong.duan.photoedittor.edit_library.text.ViewType
import dong.duan.photoedittor.file.BaseActivity
import dong.duan.photoedittor.file.bitmap_to_file
import dong.duan.photoedittor.file.show_toast
import dong.duan.photoedittor.model.ImageEdit
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ImageActivity : BaseActivity(), OnPhotoEditorListener, StickerBSFragment.StickerListener {

    val binding by lazy {
        ActivityImageBinding.inflate(layoutInflater)
    }
    lateinit var mPhotoEditor: PhotoEditor
    lateinit var image_edit: ImageEdit
    lateinit var bitmap_result: Bitmap
    private lateinit var mEmojiBSFragment: StickerBSFragment
    private lateinit var mSaveFileHelper: FileSaveHelper
    private var mIsFilterVisible = false
    @SuppressLint("MissingPermission")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(binding.root)
        val filepath = intent.getStringExtra("bitmap")
        bitmap_result = BitmapFactory.decodeFile(filepath)
        mEmojiBSFragment = StickerBSFragment(applicationContext)
        mEmojiBSFragment.setStickerListener(this)
        val pinchTextScalable = intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)
        mPhotoEditor = PhotoEditor.Builder(this, binding.drawview)
            .setPinchTextScalable(pinchTextScalable)
            .build()

        mPhotoEditor.setOnPhotoEditorListener(this)
        mSaveFileHelper = FileSaveHelper(this)
        binding.drawview.source.setImageBitmap(bitmap_result)

        binding.txtSave.setOnClickListener {
            saveImage{
                    bitmap ->
                val resultIntent = Intent()
                var file = bitmap_to_file(bitmap, this)
                if (file == null) {
                    show_toast(this, "File is null")
                }

                resultIntent.putExtra("bitmap", file.absolutePath)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
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

        binding.icAddtext.setOnClickListener {
            showBottomSheetDialogFragment(mEmojiBSFragment)
        }
    }


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
                    mPhotoEditor.editText(rootView, typeface, inputText, colorCode)
                } else {
                    mPhotoEditor.editText(rootView, inputText, textStyle)
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


    @RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    private fun saveImage(calback:(Bitmap)->Unit) {
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

                            if (bitmap != null) {
                                binding.drawview.source.setImageBitmap(bitmap)
                                calback(bitmap)

                            } else {
                                Toast.makeText(applicationContext, "Image null", Toast.LENGTH_LONG)
                                    .show()
                            }

                        }
                    }
                }
            })
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @SuppressLint("MissingPermission")
    override fun isPermissionGranted(isGranted: Boolean, permission: String?) {
        if (isGranted) {
            saveImage{
                    bitmap ->

            }
        }
    }


    private fun showBottomSheetDialogFragment(fragment: BottomSheetDialogFragment?) {
        if (fragment == null || fragment.isAdded) {
            return
        }
        fragment.show(supportFragmentManager, fragment.tag)
    }


    companion object {

        private const val TAG = "EditImageActivity"

        const val FILE_PROVIDER_AUTHORITY = "com.burhanrashid52.photoediting.fileprovider"
        private const val CAMERA_REQUEST = 52
        private const val PICK_REQUEST = 53
        const val ACTION_NEXTGEN_EDIT = "action_nextgen_edit"
        const val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    }

    override fun onStickerClick(bitmap: Bitmap) {
        mPhotoEditor.addImage(bitmap)
    }
}
package dong.duan.photoedittor.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import dong.duan.photoedittor.R
import dong.duan.photoedittor.databinding.ActivityTestBinding
import dong.duan.photoedittor.edit_library.text.OnPhotoEditorListener

import dong.duan.photoedittor.edit_library.text.OverlayTextFragment
import dong.duan.photoedittor.edit_library.text.PhotoEditor
import dong.duan.photoedittor.edit_library.text.PhotoEditorView
import dong.duan.photoedittor.edit_library.text.TextStyleBuilder
import dong.duan.photoedittor.edit_library.text.ViewType


class TestActivity : AppCompatActivity(), OnPhotoEditorListener {
    lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView
    private val binding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val pinchTextScalable = intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)
        mPhotoEditorView=binding.drawview
        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this)


        binding.drawview.source.setImageResource(R.drawable.img)

        binding.btnShow.setOnClickListener {
            val text_edtfg=OverlayTextFragment.show(this)
            text_edtfg.setOntextEdittorEvent(object : OverlayTextFragment.TextEditorEvent{
                override fun onDone(input: String, color: Int) {
                    val  textStyle= TextStyleBuilder()
                    textStyle.withTextColor(color)
                  mPhotoEditor.addText(input,color)
                }

            })
        }
    }

    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {

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
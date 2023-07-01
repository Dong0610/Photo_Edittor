package dong.duan.photoedittor.edit_library.text

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import dong.duan.photoedittor.R
import dong.duan.photoedittor.file.show_toast

internal class Text(
    private val mPhotoEditorView: PhotoEditorView,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    private val mDefaultTextTypeface: Typeface?,
    private val mGraphicManager: GraphicManager
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = mGraphicManager,
    viewType = ViewType.TEXT,
    layoutId = R.layout.view_photo_editor_text
) {

    private var mTextView: TextView? = null

    fun buildView(text: String?, styleBuilder: TextStyleBuilder?) {
        mTextView?.apply {
            this.text = text
            styleBuilder?.applyStyle(this)
        }
    }

    private fun setupGesture() {
        val onGestureControl = buildGestureController(mPhotoEditorView, mViewState)
        mMultiTouchListener.setOnGestureControl(onGestureControl)
        val rootView = rootView

        rootView.setOnTouchListener(mMultiTouchListener)
    }


    override fun setupView(rootView: View) {
        mTextView = rootView.findViewById(R.id.tvPhotoEditorText)
        mTextView?.run {
            gravity = Gravity.CENTER
            typeface = mDefaultTextTypeface
        }
    }

    override fun updateView(view: View) {
        super.updateView(view)
        val textInput = mTextView?.text.toString()
        val currentTextColor = mTextView?.currentTextColor ?: 0
        val photoEditorListener = graphicManager?.onPhotoEditorListener
        photoEditorListener?.onEditTextChangeListener(view, textInput, currentTextColor)
    }

    init {
        setupGesture()
    }
}

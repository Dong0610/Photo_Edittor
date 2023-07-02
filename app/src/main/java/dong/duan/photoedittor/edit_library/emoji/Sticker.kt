package dong.duan.photoedittor.edit_library.emoji

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import dong.duan.photoedittor.R
import dong.duan.photoedittor.edit_library.text.Graphic
import dong.duan.photoedittor.edit_library.text.GraphicManager
import dong.duan.photoedittor.edit_library.text.MultiTouchListener
import dong.duan.photoedittor.edit_library.text.PhotoEditorView
import dong.duan.photoedittor.edit_library.text.PhotoEditorViewState
import dong.duan.photoedittor.edit_library.text.ViewType

internal class Sticker(
    private val mPhotoEditorView: PhotoEditorView,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    graphicManager: GraphicManager?
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = graphicManager,
    viewType = ViewType.IMAGE,
    layoutId = R.layout.view_photo_editor_image
) {
    private var imageView: ImageView? = null
    fun buildView(desiredImage: Bitmap?) {
        imageView?.setImageBitmap(desiredImage)
    }

    private fun setupGesture() {
        val onGestureControl = buildGestureController(mPhotoEditorView, mViewState)
        mMultiTouchListener.setOnGestureControl(onGestureControl)
        val rootView = rootView
        rootView.setOnTouchListener(mMultiTouchListener)
    }

    override fun setupView(rootView: View) {
        imageView = rootView.findViewById(R.id.imgPhotoEditorImage)
    }

    init {
        setupGesture()
    }
}
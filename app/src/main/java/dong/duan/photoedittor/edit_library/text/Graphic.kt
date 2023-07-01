package dong.duan.photoedittor.edit_library.text

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import dong.duan.photoedittor.R
import dong.duan.photoedittor.edit_library.text.MultiTouchListener.OnGestureControl
import dong.duan.photoedittor.file.show_toast
internal abstract class Graphic(
    val context: Context,
    val layoutId: Int,
    val viewType: ViewType,
    val graphicManager: GraphicManager?) {

    val rootView: View

    open fun updateView(view: View) {
        //Optional for subclass to override
    }

    init {
        if (layoutId == 0) {
            throw UnsupportedOperationException("Layout id cannot be zero. Please define a layout")
        }
        rootView = LayoutInflater.from(context).inflate(layoutId, null)
        this.setupView(rootView)
        this.setupEdit(rootView)
        setupRemoveView(rootView)
    }

    private fun setupEdit(rootView: View){
        rootView.tag=viewType
        val imgEdit=rootView.findViewById<ImageView>(R.id.imgPhotoEditChange)
        imgEdit.setOnClickListener {
            updateView(rootView)
        }
    }
    private fun setupRemoveView(rootView: View) {
        rootView.tag = viewType
        val imgClose = rootView.findViewById<ImageView>(R.id.imgPhotoEditorClose)
        imgClose?.setOnClickListener {
            graphicManager?.removeView(this@Graphic)
        }
    }

    protected fun toggleSelection() {
        val frmBorder = rootView.findViewById<View>(R.id.frmBorder)
        val imgClose = rootView.findViewById<View>(R.id.imgPhotoEditorClose)
        val imgEdit=rootView.findViewById<ImageView>(R.id.imgPhotoEditChange)
        if (frmBorder != null) {
            frmBorder.setBackgroundResource(R.drawable.rounded_border_tv)
            frmBorder.tag = true
        }
        if (imgClose != null) {
            imgClose.visibility = View.VISIBLE
        }
        if (imgEdit != null) {
            imgEdit.visibility = View.VISIBLE
        }
    }

    protected fun buildGestureController(
        photoEditorView: PhotoEditorView,
        viewState: PhotoEditorViewState
    ): OnGestureControl {
        val boxHelper = BoxHelper(photoEditorView, viewState)
        return object : OnGestureControl {
            override fun onClick() {
                boxHelper.clearHelperBox()
                toggleSelection()

                viewState.currentSelectedView = rootView
            }

        }
    }

    open fun setupView(rootView: View) {}
}
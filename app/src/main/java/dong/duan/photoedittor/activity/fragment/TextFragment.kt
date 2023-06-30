package dong.duan.photoedittor.activity.fragment

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dong.duan.photoedittor.R
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.FragmentTextBinding
import dong.duan.photoedittor.databinding.ItemColorsViewBinding
import dong.duan.photoedittor.edit_library.text.OnPhotoEditorListener

import dong.duan.photoedittor.edit_library.text.OverlayTextFragment
import dong.duan.photoedittor.edit_library.text.PhotoEditor
import dong.duan.photoedittor.edit_library.text.TextStyleBuilder
import dong.duan.photoedittor.edit_library.text.ViewType
import dong.duan.photoedittor.file.get_all_colorsxml
import dong.duan.photoedittor.model.ImageData
import dong.duan.photoedittor.test.TestActivity

class TextFragment : Fragment() , OnPhotoEditorListener {
    val binding by lazy {
        FragmentTextBinding.inflate(layoutInflater)
    }
    lateinit var mPhotoEditor: PhotoEditor
    lateinit var image_edit: ImageData
    lateinit var bitmap_result: Bitmap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return  binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        image_edit = requireArguments().getSerializable("image") as ImageData
        bitmap_result =image_edit.bitmap!!
        val pinchTextScalable = requireActivity().intent.getBooleanExtra(TestActivity.PINCH_TEXT_SCALABLE_INTENT_KEY, true)
        mPhotoEditor = PhotoEditor.Builder(this.requireContext(), binding.drawview)
            .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk

        binding.drawview.source.setImageBitmap(bitmap_result)

        even_click()



    }

    private fun even_click() {
        binding.icAddtext.setOnClickListener {
            val text_edtfg= OverlayTextFragment.show(requireActivity() as AppCompatActivity)
            text_edtfg.setOntextEdittorEvent(object : OverlayTextFragment.TextEditorEvent{
                override fun onDone(input: String, color: Int) {
                    val  textStyle= TextStyleBuilder()
                    textStyle.withTextColor(color)
                    mPhotoEditor.addText(input,color)
                }

            })
        }
        binding.icUndo.setOnClickListener {
            mPhotoEditor.undo()
        }
        binding.icRedo.setOnClickListener {
            mPhotoEditor.redo()
        }
    }


    companion object {
        

    }
    private lateinit var mTxtCurrentTool: TextView
    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
        val textEditorDialogFragment =
            OverlayTextFragment.show(this.requireActivity() as AppCompatActivity, text.toString(), colorCode)
        textEditorDialogFragment.setOntextEdittorEvent(object :
            OverlayTextFragment.TextEditorEvent {
            override fun onDone(inputText: String, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                mPhotoEditor.editText(rootView, inputText, styleBuilder)
                mTxtCurrentTool.setText("Text")
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
}
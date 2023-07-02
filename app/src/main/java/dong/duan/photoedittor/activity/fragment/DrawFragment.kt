package dong.duan.photoedittor.activity.fragment

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import dong.duan.photoedittor.R
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.FragmentDrawBinding
import dong.duan.photoedittor.databinding.ItemColorsViewBinding
import dong.duan.photoedittor.edit_library.drawboard.view.DrawBoardView.DrawMode
import dong.duan.photoedittor.file.Colors
import dong.duan.photoedittor.file.get_all_colorsxml
import dong.duan.photoedittor.file.show_popup_seekbar
import dong.duan.photoedittor.model.ImageData

class DrawFragment : Fragment() {
    lateinit var binding:FragmentDrawBinding
    lateinit var bitmap_result: Bitmap

    private var color=Colors.APP_COLOR
    var pain_size=5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentDrawBinding.inflate(layoutInflater)
        val filepath = requireArguments().getString("image")
        bitmap_result = BitmapFactory.decodeFile(filepath)
        binding.drawLayout.setImageBitmap(bitmap_result)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.drawLayout.imageBitmap=bitmap_result

        binding.layoutColor.adapter=GenericAdapter(get_all_colorsxml(requireContext()),ItemColorsViewBinding::inflate){
            itemColorsViewBinding, colorFilter, i ->
            itemColorsViewBinding.itemColors.backgroundTintList= ColorStateList.valueOf(colorFilter.value)
            itemColorsViewBinding.root.setOnClickListener {
                binding.drawLayout.drawTextColor=colorFilter.value
            }
        }

        binding.icSize.setOnClickListener {
            show_popup_seekbar(it,this@DrawFragment,pain_size,3,10,-160){value->
                binding.textEdit.text=value.toString()
                pain_size=value
                binding.drawLayout.penSize(pain_size.toFloat())
            }
        }
        even_click()
      return binding.root
    }

    fun bitmap_result():Bitmap{
        return binding.drawLayout.imageBitmap
    }
    private fun even_click() {
        binding.icRedo.setOnClickListener {
            binding.drawLayout.redo()
        }
        binding.icUndo.setOnClickListener {
            binding.drawLayout.undo()
        }

        binding.icPath.setOnClickListener {
            binding.drawLayout.drawMode=DrawMode.DRAW_PATH
        }
        binding.icLine.setOnClickListener {
            binding.drawLayout.drawMode=DrawMode.DRAW_LINE
        }
        binding.icArrow.setOnClickListener {
            binding.drawLayout.drawMode=DrawMode.DRAW_ARROW
        }
        binding.icReatangle.setOnClickListener {
            binding.drawLayout.drawMode=DrawMode.DRAW_RECT
        }
        binding.icOval.setOnClickListener {
            binding.drawLayout.drawMode=DrawMode.DRAW_OVAL
        }
        binding.icErase.setOnClickListener {
            binding.drawLayout.drawMode=DrawMode.ERASER
        }

    }
}
package dong.duan.photoedittor.activity.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import dong.duan.photoedittor.R
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.FragmentPixelBinding
import dong.duan.photoedittor.databinding.ItemEditListBinding

import dong.duan.photoedittor.model.ImageData
import java.io.Serializable

class PixelFragment : Fragment() {
  lateinit var binding:FragmentPixelBinding
    lateinit var image_edit: ImageData
    lateinit var bitmap_result: Bitmap
  //  lateinit var pixsel_filter:PixselateFilter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= FragmentPixelBinding.inflate(layoutInflater)
        image_edit = requireArguments().getSerializable("image") as ImageData
        bitmap_result = image_edit.bitmap!!
        binding.imageView.setImageBitmap(bitmap_result)
        //pixsel_filter= PixselateFilter(bitmap_result)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.rcvlistEdit.adapter = GenericAdapter(
            list_Pixelate().toList(),
            ItemEditListBinding::inflate
        ) { itembinding, pixelate:  Pixelate, position: Int ->
            itembinding.textEdit.text = pixelate.name
            itembinding.imageEdit.scaleType= ImageView.ScaleType.CENTER_CROP
            itembinding.imageEdit.setPadding(0,0,0,0)
            itembinding.imageEdit.setImageResource(pixelate.demo)
            itembinding.root.setOnClickListener {
               set_pixselate(pixelate)
            }

        }

        return binding.root
    }

    private fun set_pixselate(pixelate: Pixelate) {
        var bitmap_rs: Bitmap? = null
//        when (pixelate.id) {
//            1 -> {
//                bitmap_rs = pixsel_filter.first_filter
//            }
//            2 -> {
//                bitmap_rs =pixsel_filter.second_filter
//            }
//
//            3 -> {
//                bitmap_rs = pixsel_filter.three_filter
//            }
//
//            4 -> {
//                bitmap_rs = pixsel_filter.four_filter
//            }
//
//            5 -> {
//                bitmap_rs = pixsel_filter.five_filter
//            }
//
//            6 -> {
//
//                bitmap_rs = pixsel_filter.six_filter
//
//            }
//            7 -> {
//                bitmap_rs = pixsel_filter.seven_filter
//            }
//
//            else -> bitmap_rs = bitmap_result
//        }

        binding.imageView.setImageBitmap(bitmap_rs)
    }

    data class Pixelate(var id: Int, var name: String,var demo:Int) : Serializable

    private fun list_Pixelate(): MutableList<Pixelate> {
        var list_string = mutableListOf<Pixelate>()
        list_string.add(Pixelate(0, "None",R.drawable.img))
        list_string.add(Pixelate(1, "One",R.drawable.pix_one))
        list_string.add(Pixelate(2, "Tow",R.drawable.pix_two))
        list_string.add(Pixelate(3, "Three",R.drawable.pix_three))
        list_string.add(Pixelate(4, "Four",R.drawable.pix_four))
        list_string.add(Pixelate(5, "Five",R.drawable.pix_five))
        list_string.add(Pixelate(6, "Six",R.drawable.pix_seven))
        list_string.add(Pixelate(7, "Seven",R.drawable.pix_six))
        return list_string
    }
}
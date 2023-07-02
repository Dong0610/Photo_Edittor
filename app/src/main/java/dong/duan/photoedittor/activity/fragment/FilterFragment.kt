package dong.duan.photoedittor.activity.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dong.duan.photoedittor.R
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.FragmentFilterBinding
import dong.duan.photoedittor.databinding.ItemEditListBinding
import dong.duan.photoedittor.edit_library.effect.FilterFactory
import dong.duan.photoedittor.file.run_blocking
import dong.duan.photoedittor.model.ImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class FilterFragment : Fragment() {
    lateinit var binding: FragmentFilterBinding
    lateinit var effectView: GLSurfaceView
    lateinit var bitmap_result: Bitmap
    lateinit var filter: FilterFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFilterBinding.inflate(layoutInflater)
        effectView = GLSurfaceView(this.requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val filepath = requireArguments().getString("image")
        bitmap_result = BitmapFactory.decodeFile(filepath)
        binding.imageView.setImageBitmap(bitmap_result)
        filter = FilterFactory(bitmap_result)

        binding.rcvlistEdit.adapter = GenericAdapter(
            list_filter().toList(),
            ItemEditListBinding::inflate
        ) { itembinding, filter: Filter, position: Int ->
            itembinding.textEdit.text = filter.name
            itembinding.imageEdit.scaleType=ImageView.ScaleType.CENTER_CROP
            itembinding.imageEdit.setPadding(0,0,0,0)
            itembinding.imageEdit.setImageResource(filter.demo)
            itembinding.root.setOnClickListener {
                this.set_filter(filter)
            }

        }

        return binding.root
    }

    private fun set_filter(filters: Filter) {
        binding.progressLoad.visibility = View.VISIBLE
        var bitmap_rs: Bitmap? = null
        when (filters.id) {
            1 -> {

                bitmap_rs = filter.apply_fresh()

            }

            2 -> {
                bitmap_rs = filter.apply_transparent()
            }

            3 -> {
                bitmap_rs = filter.apply_warm()
            }

            4 -> {
                bitmap_rs = filter.apply_blackwhite(0.5f)
            }

            5 -> {
                bitmap_rs = filter.apply_brightness(80)
            }

            6 -> {

                bitmap_rs = filter.apply_ice()

            }

            7 -> {
                bitmap_rs = filter.apply_special()
            }

            8 -> {
                bitmap_rs = filter.apply_classic()
            }

            9 -> {
                bitmap_rs = filter.apply_fade()
            }

            10 -> {
                bitmap_rs = filter.apply_gray()
            }

            11 -> {
                bitmap_rs = filter.apply_blind()
            }

            12 -> {
                bitmap_rs = filter.apply_orange()
            }

            13 -> {
                bitmap_rs = filter.apply_brilliantpink()
            }

            14 -> {
                bitmap_rs = filter.apply_neural()
            }

            else -> bitmap_rs = bitmap_result
        }

        binding.imageView.setImageBitmap(bitmap_rs)
        binding.progressLoad.visibility = View.GONE
    }

    data class Filter(var id: Int, var name: String,var demo:Int) : Serializable

    private fun list_filter(): MutableList<Filter> {
        var list_string = mutableListOf<Filter>()
        list_string.add(Filter(0, "None",R.drawable.img))
        list_string.add(Filter(1, "Fresh",R.drawable.img_fresh))
        list_string.add(Filter(2, "Transparent",R.drawable.img))
        list_string.add(Filter(3, "Warm",R.drawable.img_warm))
        list_string.add(Filter(4, "BlackWhite",R.drawable.img_blackwhite))
        list_string.add(Filter(5, "Brightness",R.drawable.img_brighness))
        list_string.add(Filter(6, "Ice",R.drawable.img_ice))
        list_string.add(Filter(7, "Spceial",R.drawable.img_spceial))
        list_string.add(Filter(8, "Classic",R.drawable.img_claasic))
        list_string.add(Filter(9, "Fade",R.drawable.img))
        list_string.add(Filter(10, "Gray",R.drawable.img_gray))
        list_string.add(Filter(11, "Blind",R.drawable.img_blind))
        list_string.add(Filter(12, "Orange",R.drawable.img_orange))
        list_string.add(Filter(13, "Pink",R.drawable.img_pink))
        list_string.add(Filter(14, "Neural",R.drawable.img))

        return list_string
    }

    fun bitmap_result():Bitmap{
        return bitmap_result
    }
}
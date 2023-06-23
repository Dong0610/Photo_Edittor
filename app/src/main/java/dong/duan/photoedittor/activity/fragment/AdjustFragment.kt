package dong.duan.photoedittor.activity.fragment

import android.graphics.Bitmap
import android.media.effect.Effect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import dong.duan.photoedittor.R
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.FragmentAdjustBinding
import dong.duan.photoedittor.databinding.ItemEditListBinding
import dong.duan.photoedittor.edit_library.effect.EffectFactory
import dong.duan.photoedittor.file.show_popup_seekbar
import dong.duan.photoedittor.model.EditData
import dong.duan.photoedittor.model.ImageData

class AdjustFragment : Fragment() {
    lateinit var binding: FragmentAdjustBinding
    lateinit var effect: Effect
    lateinit var image_edit:ImageData
    lateinit var bitmap_result:Bitmap
    var adapter: GenericAdapter<EditData, ItemEditListBinding>? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAdjustBinding.inflate(layoutInflater)
        image_edit = requireArguments().getSerializable("image") as ImageData
        binding.imageView.setImageBitmap(image_edit.bitmap)
        bitmap_result=image_edit.bitmap!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        load_adjust()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun load_adjust() {
        var list_filter = ArrayList<EditData>()
        list_filter.add(EditData(1, "Auto", R.drawable.ic_filter))
        adapter = GenericAdapter(
            list_filter,
            ItemEditListBinding::inflate
        ) { itembinding, editdata, position ->
            itembinding.imageEdit.setImageResource(editdata.icon)
            itembinding.textEdit.text = editdata.name
            itembinding.root.setOnClickListener {
                set_filter(editdata,it,itembinding.textEdit)

            }
        }
        binding.rcvlistEdit.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun set_filter(editdata: EditData, it: View, textEdit: TextView) {
        when(editdata.id){
            1->{
                show_popup_seekbar(it, this@AdjustFragment,75 , 25, 125, 100) { value ->
                    val valueset = (value*0.01f).coerceIn(0.35f,1.25f)
                    textEdit.text=(value-25).toString()
                    bitmap_result= EffectFactory(image_edit.bitmap!!).apply_auto_fix(valueset,1.2f)
                    binding.imageView.setImageBitmap(bitmap_result)
                }
            }
        }
    }




}
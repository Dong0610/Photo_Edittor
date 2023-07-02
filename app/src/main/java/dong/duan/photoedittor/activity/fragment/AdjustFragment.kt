package dong.duan.photoedittor.activity.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.abs

class AdjustFragment : Fragment() {
    lateinit var binding: FragmentAdjustBinding
    private lateinit var bitmap_result:Bitmap
    var adapter: GenericAdapter<EditData, ItemEditListBinding>? = null

    private var imageJob: Job? = null
    private val imageScope = CoroutineScope(Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAdjustBinding.inflate(layoutInflater)
        val filepath = requireArguments().getString("image")
        bitmap_result = BitmapFactory.decodeFile(filepath)
        binding.imageView.setImageBitmap(bitmap_result)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        load_adjust()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        imageJob?.cancel()
        imageScope.cancel()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun load_adjust() {
        var list_filter = ArrayList<EditData>()
        list_filter.add(EditData(1, "Auto", R.drawable.ic_filter))
        list_filter.add(EditData(2, "Brightness", R.drawable.ic_filter))
       list_filter.add(EditData(3,"Contrast",R.drawable.ic_filter))
        list_filter.add(EditData(4, "Saturation", R.drawable.ic_filter))
        list_filter.add(EditData(5, "Exposure", R.drawable.ic_filter))
        list_filter.add(EditData(6,"Tint",R.drawable.ic_filter))
        list_filter.add(EditData(7, "Temperature", R.drawable.ic_filter))
        list_filter.add(EditData(8, "Tone", R.drawable.ic_filter))
        list_filter.add(EditData(9,"Slossiness",R.drawable.ic_filter))
        list_filter.add(EditData(10,"Shadow",R.drawable.ic_filter))

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


    @OptIn(DelicateCoroutinesApi::class)
    fun run_blocking(function:()->Unit){
        GlobalScope.launch(Dispatchers.Default) {
            launch(Dispatchers.Main) {
               function()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun set_filter(editdata: EditData, it: View, textEdit: TextView) {
        when(editdata.id){
            1->{
                show_popup_seekbar(it, this@AdjustFragment,0 , 25, 125, -150) { value ->
                   run_blocking {
                    val valueset = (value*0.01f).coerceIn(0.35f,1.25f)
                    textEdit.text=(value-25).toString()
                    bitmap_result= EffectFactory(bitmap_result).apply_auto_fix(valueset,1.2f)
                    binding.imageView.setImageBitmap(bitmap_result)
                   }
                }
            }
            2->{
                show_popup_seekbar(it, this@AdjustFragment,0 , -100, 100, -150) { value ->
                    run_blocking {
                    imageJob?.cancel()
                    imageJob = imageScope.launch {
                        textEdit.text=(value).toString()
                        bitmap_result= EffectFactory(bitmap_result).adjust_brightness(value.toInt())
                        binding.imageView.setImageBitmap(bitmap_result)
                    }
                    }
                }
            }

            3->{
                show_popup_seekbar(it, this@AdjustFragment,0 , -10, 10, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text=(value).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_contrast(value.toFloat())
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }
            4->{
                show_popup_seekbar(it, this@AdjustFragment,0 , -100, 100, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text=(value).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_saturation(value.toFloat())
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }

            5->{
                show_popup_seekbar(it, this@AdjustFragment,0 , 0, 10, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text=(value).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_exposure(value.toFloat())
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }
            6->{
                show_popup_seekbar(it, this@AdjustFragment,10, 0, 20, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text= abs(value*5).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_tint((value-20).toFloat())
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }

            7->{
                show_popup_seekbar(it, this@AdjustFragment,0 , -100, 100, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text=(value).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_temperature(value)
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }
            8->{
            show_popup_seekbar(it, this@AdjustFragment,0 , -100, 100, -150) { value ->
                run_blocking {
                    imageJob?.cancel()
                    imageJob = imageScope.launch {
                        textEdit.text=(value).toString()
                        bitmap_result= EffectFactory(bitmap_result).adjust_tone(value.toFloat())
                        binding.imageView.setImageBitmap(bitmap_result)
                    }
                }
            }
        }

            9->{
                show_popup_seekbar(it, this@AdjustFragment,0 , -10, 10, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text=(value).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_slossiness(value.toFloat())
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }
            10->{
                show_popup_seekbar(it, this@AdjustFragment,0 , -100, 100, -150) { value ->
                    run_blocking {
                        imageJob?.cancel()
                        imageJob = imageScope.launch {
                            textEdit.text=(value).toString()
                            bitmap_result= EffectFactory(bitmap_result).adjust_shadow(value.toFloat())
                            binding.imageView.setImageBitmap(bitmap_result)
                        }
                    }
                }
            }
        }
    }

    fun bitmap_result():Bitmap{
        return  bitmap_result
    }


}
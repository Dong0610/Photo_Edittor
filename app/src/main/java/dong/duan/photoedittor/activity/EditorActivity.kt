package dong.duan.photoedittor.activity

import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import dong.duan.photoedittor.R
import dong.duan.photoedittor.activity.fragment.AdjustFragment
import dong.duan.photoedittor.activity.fragment.CropFragment
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.ActivityEditorBinding
import dong.duan.photoedittor.databinding.ItemEditListBinding
import dong.duan.photoedittor.file.FullScreenActivity
import dong.duan.photoedittor.file.bitmap_from_uri
import dong.duan.photoedittor.model.EditData
import dong.duan.photoedittor.model.ImageData


class EditorActivity : FullScreenActivity() {
    lateinit var binding: ActivityEditorBinding
    lateinit var image_edit: ImageData
    lateinit var list_edit: ArrayList<EditData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list_edittor()
        image_edit = intent.getSerializableExtra("image") as ImageData
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            image_edit.id
        )
        image_edit.bitmap = bitmap_from_uri(applicationContext, contentUri)
        binding.imageView.setImageBitmap(image_edit.bitmap)

        binding.rcvlistEdit.adapter = GenericAdapter<EditData, ItemEditListBinding>(
            list_edit,
            ItemEditListBinding::inflate
        ) { itembinding: ItemEditListBinding, data, position ->
            itembinding.imageEdit.setImageResource(data.icon)
            itembinding.textEdit.text = data.name
            itembinding.root.setOnClickListener {
                edit_image(data)
            }
        }

        binding.txtCancel.setOnClickListener {
            load_fragmen(null)
        }

        even_click()
    }

    private fun even_click() {

        binding.txtSave.setOnClickListener {
            val text = binding.txtSave.text
            when (text) {
                "Ok" -> {
                    val fragment =
                        supportFragmentManager.findFragmentById(R.id.layout_frame) as? CropFragment
                    var bitmap = fragment?.bitmap_result()
                    load_fragmen(null)
                    binding.imageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun edit_image(data: EditData) {

        when (data.id) {
            1 -> {
                val fragment = CropFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", image_edit)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }
            2->{
                val fragment = AdjustFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", image_edit)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }

        }
    }

    fun load_fragmen(fragment: Fragment?, text: String = "Ok") {
        if (fragment != null) {
            binding.imageView.visibility = View.GONE
            binding.rcvlistEdit.visibility = View.GONE
            binding.layoutFrame.visibility = View.VISIBLE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.layout_frame, fragment)
                .commit()
            binding.txtSave.text = text
        } else {
            binding.imageView.visibility = View.VISIBLE
            binding.rcvlistEdit.visibility = View.VISIBLE
            binding.layoutFrame.visibility = View.GONE
            binding.layoutFrame.removeAllViews()
            binding.txtSave.text = "Save"
        }
    }

    private fun list_edittor() {
        list_edit = ArrayList()
        list_edit.add(EditData(1, "Crop", R.drawable.ic_crop))
        list_edit.add(EditData(2, "Adjust", R.drawable.ic_adjust))
        list_edit.add(EditData(3, "Filter", R.drawable.ic_filter))
        list_edit.add(EditData(6, "Pixel", R.drawable.ic_pixel_late))
        list_edit.add(EditData(4, "Draw", R.drawable.ic_pen))
        list_edit.add(EditData(5, "Text", R.drawable.ic_text))

    }
}
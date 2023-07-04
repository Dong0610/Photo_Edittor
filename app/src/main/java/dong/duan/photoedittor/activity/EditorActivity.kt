package dong.duan.photoedittor.activity

import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import dong.duan.photoedittor.R
import dong.duan.photoedittor.activity.fragment.AdjustFragment
import dong.duan.photoedittor.activity.fragment.CropFragment
import dong.duan.photoedittor.activity.fragment.DrawFragment
import dong.duan.photoedittor.activity.fragment.EmojiActivity
import dong.duan.photoedittor.activity.fragment.FilterFragment
import dong.duan.photoedittor.activity.fragment.ImageActivity
import dong.duan.photoedittor.activity.fragment.PixelFragment
import dong.duan.photoedittor.activity.fragment.TextActivity
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.ActivityEditorBinding
import dong.duan.photoedittor.databinding.ItemEditListBinding
import dong.duan.photoedittor.file.FullScreenActivity
import dong.duan.photoedittor.file.PreferenceManager
import dong.duan.photoedittor.file.bitmap_from_uri
import dong.duan.photoedittor.file.bitmap_to_file
import dong.duan.photoedittor.file.save_bitmap_image
import dong.duan.photoedittor.file.save_image
import dong.duan.photoedittor.file.save_image_photo
import dong.duan.photoedittor.model.EditData
import dong.duan.photoedittor.model.ImageData


@Suppress("DEPRECATION")
class EditorActivity : FullScreenActivity() {
    lateinit var binding: ActivityEditorBinding
    lateinit var list_edit: ArrayList<EditData>
    
   lateinit var bitmap_end: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list_edittor()
        val filepath=intent.getStringExtra("image")
        bitmap_end=BitmapFactory.decodeFile(filepath)
        binding.imageView.setImageBitmap(bitmap_end)

        binding.rcvlistEdit.adapter = GenericAdapter(
            list_edit,
            ItemEditListBinding::inflate
        ) { itembinding: ItemEditListBinding, data, position ->
            itembinding.imageEdit.setBackgroundResource(R.drawable.item_filter_choose)
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
            if (text.equals("Ok")) {
                call_bitmap(fragentval)
            }else if(text.equals("Save")){
                checkWriteExternalStoragePermission {
                    save_image_photo(bitmap_end,applicationContext)
                }
            }
        }

    }

    private fun call_bitmap(fragentval: Int) {
        when (fragentval) {
            1 -> {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.layout_frame) as? CropFragment
                var bitmap = fragment?.bitmap_result()
                bitmap_end= bitmap!!
                load_fragmen(null)
                binding.imageView.setImageBitmap(bitmap)
            }

            2 -> {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.layout_frame) as? AdjustFragment
                var bitmap = fragment?.bitmap_result()
               bitmap_end = bitmap!!
                load_fragmen(null)
                binding.imageView.setImageBitmap(bitmap)
            }
            3 -> {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.layout_frame) as? FilterFragment
                var bitmap = fragment?.bitmap_result()
              bitmap_end = bitmap!!
                load_fragmen(null)
                binding.imageView.setImageBitmap(bitmap)
            }
            4 -> {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.layout_frame) as? PixelFragment
                var bitmap = fragment?.bitmap_result()
                bitmap_end = bitmap!!
                load_fragmen(null)
                binding.imageView.setImageBitmap(bitmap)
            }
            5 -> {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.layout_frame) as? DrawFragment
                var bitmap = fragment?.bitmap_result()
                bitmap_end = bitmap!!
                load_fragmen(null)
                binding.imageView.setImageBitmap(bitmap)
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_TEXT && resultCode == RESULT_OK) {
            val filePath = data?.getStringExtra("bitmap")
            val bitmaprs = BitmapFactory.decodeFile(filePath)
            bitmap_end=bitmaprs
            binding.imageView.setImageBitmap(bitmap_end)
        }
        if (requestCode == REQUEST_CODE_EMOJI && resultCode == RESULT_OK) {
            val filePath = data?.getStringExtra("bitmap")
            val bitmaprs = BitmapFactory.decodeFile(filePath)
            bitmap_end=bitmaprs
            binding.imageView.setImageBitmap(bitmap_end)
        }
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            val filePath = data?.getStringExtra("bitmap")
            val bitmaprs = BitmapFactory.decodeFile(filePath)
            bitmap_end=bitmaprs
            binding.imageView.setImageBitmap(bitmap_end)
        }


    }

    private var fragentval = 0


    private fun edit_image(data: EditData) {

        when (data.id) {
            1 -> {
                fragentval = 1
                val fragment = CropFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", bitmap_to_file(bitmap_end,this).absolutePath)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }

            2 -> {
                fragentval = 2
                val fragment = AdjustFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", bitmap_to_file(bitmap_end,this).absolutePath)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }

            3 -> {
                fragentval = 3
                val fragment = FilterFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", bitmap_to_file(bitmap_end,this).absolutePath)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }

            4 -> {
                fragentval=4
                val fragment = PixelFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", bitmap_to_file(bitmap_end,this).absolutePath)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }

            5 -> {
                fragentval=5
                val fragment = DrawFragment();
                val dataput = Bundle()
                dataput.putSerializable("image", bitmap_to_file(bitmap_end,this).absolutePath)
                fragment.arguments = dataput
                load_fragmen(fragment)
            }

            6 -> {
                PreferenceManager(applicationContext).PutString("edt", "text")
                val intent = Intent(this, TextActivity::class.java)
                intent.putExtra("bitmap", bitmap_to_file(bitmap_end, this).absolutePath)
                startActivityForResult(intent, REQUEST_CODE_TEXT)

            }

            7 -> {
                PreferenceManager(applicationContext).PutString("edt", "emoj")
                val intent = Intent(this, EmojiActivity::class.java)
                intent.putExtra("bitmap", bitmap_to_file(bitmap_end, this).absolutePath)
                startActivityForResult(intent, REQUEST_CODE_EMOJI)
            }

            8 -> {
                PreferenceManager(applicationContext).PutString("edt", "emoj")
                val intent = Intent(this, ImageActivity::class.java)
                intent.putExtra("bitmap", bitmap_to_file(bitmap_end, this).absolutePath)
                startActivityForResult(intent, REQUEST_CODE_IMAGE)
            }


        }
    }

    fun load_fragmen(fragment: Fragment?, text: String = "Ok") {
        if (fragment != null) {
            binding.imageView.visibility = View.GONE
            binding.rcvlistEdit.visibility = View.GONE
            binding.layoutFrame.visibility = View.VISIBLE
            binding.constraintLayout5.layoutParams.height = 0
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
            binding.constraintLayout5.layoutParams.height = com.intuit.sdp.R.dimen._80sdp
        }
    }

    private fun list_edittor() {
        list_edit = ArrayList()
        list_edit.add(EditData(1, "Crop", R.drawable.ic_crop))
        list_edit.add(EditData(2, "Adjust", R.drawable.ic_adjust))
        list_edit.add(EditData(3, "Filter", R.drawable.ic_filter))
        list_edit.add(EditData(4, "Pixel", R.drawable.ic_pixel_late))
        list_edit.add(EditData(5, "Draw", R.drawable.ic_pen))
        list_edit.add(EditData(6, "Text", R.drawable.ic_text))
        list_edit.add(EditData(7, "Icon", R.drawable.ic_emoj))
        list_edit.add(EditData(8, "Image", R.drawable.ic_image))

    }

    companion object {
        const val REQUEST_CODE_TEXT = 1
        const val REQUEST_CODE_EMOJI = 2
        const val REQUEST_CODE_IMAGE = 2
    }
}
package dong.duan.photoedittor.activity

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dong.duan.photoedittor.R
import dong.duan.photoedittor.adapter.GenericAdapter
import dong.duan.photoedittor.databinding.ActivityMainBinding
import dong.duan.photoedittor.databinding.ItemFolderListBinding
import dong.duan.photoedittor.databinding.ItemImageListBinding
import dong.duan.photoedittor.databinding.ItemImageSmallBinding
import dong.duan.photoedittor.file.BaseActivity
import dong.duan.photoedittor.file.Colors
import dong.duan.photoedittor.file.bitmap_from_id
import dong.duan.photoedittor.file.bitmap_from_uri
import dong.duan.photoedittor.file.bitmap_to_file
import dong.duan.photoedittor.file.show_toast
import dong.duan.photoedittor.model.ImageData
import java.io.FileNotFoundException


@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    private var islayout = false
    lateinit var list_image: ArrayList<ImageData>
    lateinit var list_folder: ArrayList<String>
    var selcect = RecyclerView.NO_POSITION
    var selcectfolder = RecyclerView.NO_POSITION
    var image_edit: ImageData? = null

    var bitmap_value: Bitmap? = null

    private var adapterimg: GenericAdapter<ImageData, ItemImageListBinding>? = null
    private var adapterimg_small: GenericAdapter<ImageData, ItemImageSmallBinding>? = null
    private var adapterfolder: GenericAdapter<String, ItemFolderListBinding>? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        load_image()
        even_listener()


        binding.btnContune.setOnClickListener {
            if (choose == 0) {
                show_toast(applicationContext, "Choose image")
            } else {
                val file_path = bitmap_to_file(bitmap_value!!, this).absolutePath
                val intent = Intent(applicationContext, EditorActivity::class.java)
                intent.putExtra("image", file_path)
                startActivity(intent)
            }
        }

        adapterimg = GenericAdapter(
            list_image,
            ItemImageListBinding::inflate
        ) { binding: ItemImageListBinding, imageData: ImageData, position: Int ->
            binding.check.isChecked = selcect == position
            val bitmap = bitmap_from_id(imageData.id, applicationContext)
            binding.imageView.setImageBitmap(bitmap)
            binding.root.setOnClickListener {
                choose = 2
                selcect = position
                bitmap_value = bitmap
                this.binding.btnContune.setBackgroundResource(R.drawable.btn_continue)
                adapterimg!!.notifyDataSetChanged()
                this.image_edit = imageData
                this.binding.imageView.setImageBitmap(bitmap)
            }
        }


        binding.rcvListimage.adapter = adapterimg
        adapterimg_small = GenericAdapter(
            list_image,
            ItemImageSmallBinding::inflate
        ) { binding: ItemImageSmallBinding, imageData: ImageData, position: Int ->
            binding.check.isChecked = selcect == position

            val bitmap = bitmap_from_id(imageData.id, applicationContext)
            binding.imageView.setImageBitmap(bitmap)
            binding.root.setOnClickListener {
                selcect = position
                choose = 2
                bitmap_value = bitmap
                this.binding.btnContune.setBackgroundResource(R.drawable.btn_continue)
                adapterimg!!.notifyDataSetChanged()
                this.image_edit = imageData
                this.binding.imageView.setImageBitmap(bitmap)
            }
        }


        var list_new = removeDuplicates(list_folder)
        list_new.add(0, "All")

        adapterfolder = GenericAdapter(
            list_new,
            ItemFolderListBinding::inflate
        ) { binding: ItemFolderListBinding, data: String, position: Int ->
            binding.root.setBackgroundResource(if (selcectfolder == position) R.drawable.btn_continue else R.drawable.item_folder_default)
            binding.textVal.setTextColor(if (selcectfolder == position) Colors.WHITE else Colors.APP_COLOR)
            binding.textVal.text = data

            binding.textVal.setOnClickListener {
                selcectfolder = position
                adapterfolder!!.notifyDataSetChanged()
                get_image_with_f(data)
            }
        }
        binding.rcvListFolder.adapter = adapterfolder

        binding.cameraCapture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, 1000)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1000) {
            bitmap_value = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(bitmap_value)
            this.binding.btnContune.setBackgroundResource(R.drawable.btn_continue)
        }
    }


    var choose = 0

    @SuppressLint("NotifyDataSetChanged")
    private fun get_image_with_f(data: String) {
        var listnewimg = ArrayList<ImageData>()
        if (data == "All")
            listnewimg = list_image
        else
            list_image.forEach { item ->
                if (item.folderName.equals(data)) {
                    listnewimg.add(item)
                }
            }
        adapterimg = GenericAdapter(
            listnewimg,
            ItemImageListBinding::inflate
        ) { binding: ItemImageListBinding, imageData: ImageData, position: Int ->
            binding.check.isChecked = selcect == position
            val bitmap = bitmap_from_id(imageData.id, applicationContext)
            binding.imageView.setImageBitmap(bitmap)
            binding.root.setOnClickListener {
                selcect = position
                bitmap_value = bitmap
                this.binding.btnContune.setBackgroundResource(R.drawable.btn_continue)
                adapterimg!!.notifyDataSetChanged()
                this.binding.imageView.setImageBitmap(bitmap)
            }
        }
        binding.rcvListimage.adapter = adapterimg
    }

    fun removeDuplicates(list: ArrayList<String>): ArrayList<String> {
        val uniqueElementsMap = HashMap<String, Boolean>()
        val distinctList = ArrayList<String>()

        for (element in list) {
            if (!uniqueElementsMap.containsKey(element)) {
                uniqueElementsMap[element] = true
                distinctList.add(element)
            }
        }

        return distinctList
    }

    private fun even_listener() {
        binding.icViewlayout.setOnClickListener {
            load_layout(islayout)
        }
    }

    fun load_image() {
        list_image = ArrayList<ImageData>()
        list_folder = ArrayList()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val folderColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val path = cursor.getString(pathColumn)
                val folderName = cursor.getString(folderColumn)
                val size = cursor.getLong(sizeColumn)
                list_folder.add(folderName)
                val imageData = ImageData(id, name, path, folderName, size, null)
                list_image.add(imageData)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun load_layout(islayout: Boolean) {
        this.islayout = !islayout
        if (islayout) {
            binding.icViewlayout.setImageResource(R.drawable.ic_arrow_drop_down)
            binding.rcvListimage.layoutParams.height =
                resources.getDimension(com.intuit.sdp.R.dimen._180sdp).toInt()
            binding.rcvListimage.adapter = adapterimg
        } else {
            binding.rcvListimage.setHasFixedSize(true)
            binding.rcvListimage.layoutParams.height =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._90sdp)
            binding.icViewlayout.setImageResource(R.drawable.ic_arrow_drop_up)

            binding.rcvListimage.adapter = adapterimg_small

        }

        binding.rcvListimage.requestLayout()

        val layoutManager = if (islayout) {
            GridLayoutManager(this, 3)
        } else {
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, true)
        }
        binding.rcvListimage.layoutManager = layoutManager
    }
}
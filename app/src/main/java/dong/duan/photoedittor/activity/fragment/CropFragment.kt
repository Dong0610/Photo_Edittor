package dong.duan.photoedittor.activity.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import dong.duan.photoedittor.R
import dong.duan.photoedittor.databinding.FragmentCropBinding
import dong.duan.photoedittor.edit_library.cropper.CropImageView
import dong.duan.photoedittor.edit_library.cropper.CropImageView.CropShape
import dong.duan.photoedittor.file.show_popup_seekbar
import dong.duan.photoedittor.model.ImageData

class CropFragment : Fragment() {
    lateinit var binding: FragmentCropBinding
    lateinit var image_edit: ImageData
    lateinit var bitmap_default: Bitmap
   private lateinit var bitmap_result: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCropBinding.inflate(layoutInflater)
        image_edit = requireArguments().getSerializable("image") as ImageData
        bitmap_default = image_edit.bitmap!!
    }

    var cropShape = CropShape.RECTANGLE

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.imageEdit.setImageBitmap(image_edit.bitmap)
        binding.imageEdit.cropShape = CropShape.RECTANGLE

        even_click()

        return binding.root
    }

    var flip_value = 0;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun even_click() {
        binding.icropShape.setOnClickListener {
            binding.imageEdit.cropShape = cropShape
            if (cropShape == CropShape.RECTANGLE) {
                cropShape = CropShape.OVAL
                binding.txtShape.text = "Rectangle"
            } else {
                cropShape = CropShape.RECTANGLE
                binding.txtShape.text = "Oval"
            }
            bitmap_result = binding.imageEdit.bitmapResult()
        }
        binding.icFlipImage.setOnClickListener {
            flip_value++
            if (flip_value > 4) {
                flip_value = 0;
            }
            when (flip_value) {
                1 -> binding.imageEdit.flipImageHorizontally()
                2 -> binding.imageEdit.flipImageHorizontally()
                3 -> binding.imageEdit.flipImageVertically()
                4 -> binding.imageEdit.flipImageVertically()
            }
            bitmap_result = binding.imageEdit.bitmapResult()
        }

        binding.icRotate.setOnClickListener {
            binding.imageEdit.cropShape = CropShape.OVAL
            binding.txtShape.text = "Oval"
            show_popup_seekbar(it, this@CropFragment, 0,  minval = -90, maxVal = 90,100) { it ->
                binding.txtRotate.text = it.toString()
                binding.imageEdit.rotateImage(it)
            }
            bitmap_result = binding.imageEdit.bitmapResult()
        }
        var default = 0
        binding.icRotate90.setOnClickListener {
            binding.txtShape.text = "Oval"
            binding.imageEdit.cropShape = CropShape.OVAL
            default += 90
            if (default == 360) {
                default = 0
            }
            binding.txtRotate90.text = default.toString()
            binding.imageEdit.rotateImage(90)
            bitmap_result = binding.imageEdit.bitmapResult()
        }
        binding.icPreview.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.imageEdit.setImageBitmap(bitmap_default)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    bitmap_result = binding.imageEdit.bitmapResult()
                    binding.imageEdit.setImageBitmap(bitmap_result)
                    false // Return false to allow further event handling
                }
                else -> false // Return false for other touch events
            }

        }
    }

    fun bitmap_result():Bitmap {
        return  binding.imageEdit.croppedImage
    }
}
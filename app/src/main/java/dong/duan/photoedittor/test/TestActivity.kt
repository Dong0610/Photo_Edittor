package dong.duan.photoedittor.test

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dong.duan.photoedittor.R
import dong.duan.photoedittor.databinding.ActivityTestBinding


class TestActivity : AppCompatActivity(){


    private val binding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }
    private var strokeWidth: Float = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }


}
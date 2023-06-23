package dong.duan.photoedittor.file

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import dong.duan.photoedittor.R

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UseCompatLoadingForDrawables")
fun show_popup_seekbar(
    anchorView: View,
    activity: Activity,
    seekVal: Int = 0,
    minval:Int=0,maxVal:Int=100,
    locationVal:Int =0,
    calback: (Int) -> Unit
) {
    lateinit var popupWindow: PopupWindow
    val inflater = LayoutInflater.from(activity.applicationContext)
    val popupView = inflater.inflate(R.layout.popup_seekbar_value, null)
    val seekBar = popupView.findViewById<SeekBar>(R.id.seekBar)
    seekBar.max=maxVal
    seekBar.min=minval
    seekBar.progress=seekVal
    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            calback(progress)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            seekBar!!.progress = seekVal
        }
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            calback(seekBar!!.progress)
        }
    })
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val screenWidth = displayMetrics.widthPixels
    val popupWidth = (screenWidth * 0.8).toInt() // Adjust the proportion as needed
    val popupHeight = WindowManager.LayoutParams.WRAP_CONTENT
    popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)
    popupWindow.setBackgroundDrawable(activity.applicationContext.getDrawable(R.drawable.pop_up_background))
    popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, 0, -anchorView.height)
}


@SuppressLint("UseCompatLoadingForDrawables")
@RequiresApi(Build.VERSION_CODES.O)
fun show_popup_seekbar(
    anchorView: View,
    activity: Fragment,
    seekVal: Int = 0,
    minval:Int=0,maxVal:Int=100,
    locationVal:Int =0,
    calback: (Int) -> Unit,
) {
    lateinit var popupWindow: PopupWindow
    val inflater = LayoutInflater.from(activity.requireContext())
    val popupView = inflater.inflate(R.layout.popup_seekbar_value, null)
    val seekBar = popupView.findViewById<SeekBar>(R.id.seekBar)
    seekBar.max=maxVal
    seekBar.min=minval
    seekBar.progress=seekVal
    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            calback(progress)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            seekBar!!.progress = seekVal
        }
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            calback(seekBar!!.progress)
        }
    })
    val screenWidth = screen_width(activity.requireContext())
    val popupWidth = (screenWidth * 0.9).toInt()
    val popupHeight = WindowManager.LayoutParams.WRAP_CONTENT
    popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)
    popupWindow.setBackgroundDrawable(activity.requireContext().getDrawable(R.drawable.pop_up_background))
    anchorView.post {
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val screenX = location[0]
        val screenY = location[1]
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, (screenWidth-popupWidth)/2, screenY+locationVal)

    }

}
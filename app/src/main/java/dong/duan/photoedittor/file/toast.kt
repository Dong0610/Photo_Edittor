package dong.duan.photoedittor.file

import android.content.Context
import android.widget.Toast

fun show_toast(context: Context,mess:Any){
    Toast.makeText(context,mess.toString(),Toast.LENGTH_LONG).show()
}

fun show_toast(context: Context,mess:Any,time:Int){
    Toast.makeText(context,mess.toString(),time).show()
}
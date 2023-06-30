package dong.duan.photoedittor.file

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun show_toast(context: Context,mess:Any){
    Toast.makeText(context,mess.toString(),Toast.LENGTH_LONG).show()
}

fun show_toast(context: Context,mess:Any,time:Int){
    Toast.makeText(context,mess.toString(),time).show()
}


@OptIn(DelicateCoroutinesApi::class)
fun run_blocking(function:()->Unit){
    GlobalScope.launch(Dispatchers.Default) {
        launch(Dispatchers.Main) {
            function()
        }
    }
}
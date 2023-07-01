package dong.duan.photoedittor.file

import android.util.Log

fun log(tag:Any,value:Any?){
    Log.d(tag.toString(),value.toString())
}
fun log(value:Any?){
    Log.d("tag",value.toString())
}
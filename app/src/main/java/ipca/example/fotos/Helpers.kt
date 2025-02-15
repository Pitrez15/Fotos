package ipca.example.fotos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

fun dateToString(date : Date) : String{
    val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss", Locale.getDefault())
    return formatter.format(date)
}

fun stringToDate(dateStr: String) : Date {
    val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm:ss", Locale.getDefault())
    val date = formatter.parse(dateStr)
    return date ?: Date()
}

fun saveImageToCard(context: Context, bitmap : Bitmap) : String {
    var photoDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    Log.d("photocatalog", photoDir.toString() )
    photoDir = File(photoDir, "${UUID.randomUUID()}.jpg")
    Log.d("photocatalog", photoDir.toString() )
    try {
        val stream: OutputStream = FileOutputStream(photoDir)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException){
        e.printStackTrace()
        return ""
    }
    return photoDir.toString()
}

fun loadImageFromCard(context : Context, path : String) : Bitmap? {
    try {
        var bitmap : Bitmap = BitmapFactory.decodeFile(path)
        return bitmap
    } catch (e: IOException){
        e.printStackTrace()
        return null
    }
}
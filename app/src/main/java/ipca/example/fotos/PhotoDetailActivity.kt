package ipca.example.fotos

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_photo_detail.*
import java.util.*

class PhotoDetailActivity : AppCompatActivity() {

    private var bitmap : Bitmap? = null
    private var date : Date? = null
    private var client : FusedLocationProviderClient? =null
    private var latitude : Double? = null
    private var longitude : Double? = null
    private var photoPath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        floatingActionButtonTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1001)
        }

        client  = LocationServices.getFusedLocationProviderClient(this)

        client!!.lastLocation.addOnSuccessListener {location ->
            location?.let{
                latitude = it.latitude
                longitude = it.longitude
                textViewLatitude.text =  "$latitude"
                texViewLongitude.text = "$longitude"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_photo,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add){
            val intentResult = Intent()
            intentResult.putExtra(PHOTO_PATH, photoPath )
            intentResult.putExtra(PHOTO_DATE, dateToString(date!!))
            intentResult.putExtra(PHOTO_LATITUDE, latitude)
            intentResult.putExtra(PHOTO_LONGITUDE, longitude)
            intentResult.putExtra(PHOTO_DESCRIPTION, editTextDescription.text.toString())
            setResult(Activity.RESULT_OK, intentResult)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode === Activity.RESULT_OK){
            if (requestCode == 1001){
                data?.extras?.let {
                    bitmap = it.get("data") as Bitmap
                    imageViewPhoto.setImageBitmap(bitmap)
                    date = Date()
                    textViewDate.text = dateToString(date!!)
                    photoPath = saveImageToCard(this, bitmap!!)
                }
            }
        }
    }

    companion object {
        val PHOTO_PATH = "photo_path"
        val PHOTO_DATE  = "photo_date"
        val PHOTO_LATITUDE = "photo_latitude"
        val PHOTO_LONGITUDE = "photo_longitude"
        val PHOTO_DESCRIPTION = "photo_description"
    }
}

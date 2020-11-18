package ipca.example.fotos

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import ipca.example.fotos.models.PhotoItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_photos_view.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var photos : MutableList<PhotoItem> = ArrayList<PhotoItem>()
    var photoAdapter : PhotoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photoAdapter = PhotoAdapter()
        listViewPhotos.adapter = photoAdapter

        requestPermission()
        floatingActionButtonNewPhoto.setOnClickListener {
            val intent = Intent(this, PhotoDetailActivity::class.java)
            startActivityForResult(intent, 1002)
        }
    }

    inner class PhotoAdapter : BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = layoutInflater.inflate(R.layout.row_photos_view,parent,false)
            val dateTextView = rowView.findViewById<TextView>(R.id.textViewDateRowPhotos)
            val imageView = rowView.findViewById<ImageView>(R.id.imageViewRowPhotos)
            dateTextView.text = photos[position].date.toString()
            val bm = loadImageFromCard(this@MainActivity, photos[position].filePath!!)
            imageView.setImageBitmap(bm)
            return rowView
        }
        override fun getItem(position: Int): Any {
            return photos[position]
        }
        override fun getItemId(position: Int): Long {
            return 0
        }
        override fun getCount(): Int {
            return photos.size
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode === Activity.RESULT_OK){
            if (requestCode == 1002){
                data?.extras?.let {
                    val description = it.getString(PhotoDetailActivity.PHOTO_DESCRIPTION)
                    val date = it.getString(PhotoDetailActivity.PHOTO_DATE)
                    val path = it.getString(PhotoDetailActivity.PHOTO_PATH)
                    val latitude = it.getDouble(PhotoDetailActivity.PHOTO_LATITUDE)
                    val longitude = it.getDouble(PhotoDetailActivity.PHOTO_LONGITUDE)
                    val photoItem = PhotoItem()
                    photoItem.description = description
                    photoItem.date = stringToDate(date!!)
                    photoItem.filePath = path
                    photoItem.gpsLatitude = latitude
                    photoItem.gpsLongitude = longitude
                    photos.add(photoItem)
                    photoAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }
}

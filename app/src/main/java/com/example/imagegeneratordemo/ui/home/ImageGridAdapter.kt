package com.example.imagegeneratordemo.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import coil.load
import com.example.imagegeneratordemo.R
import com.example.imagegeneratordemo.model.ImageUrl
import com.example.imagegeneratordemo.utils.ImageExtensions.base64ToBitmap

class ImageGridAdapter(
    private val imageList: List<ImageUrl>,
    private val context: Context
) : BaseAdapter(
) {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var ivImage: ImageView

    override fun getCount(): Int = imageList.size

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView1 = convertView

        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView1 == null) {
            convertView1 = layoutInflater!!.inflate(R.layout.item_gridview, null)
        }
        ivImage = convertView1!!.findViewById(R.id.ivItem)
        ivImage.load(imageList[position].url.base64ToBitmap()) {
            crossfade(true)
        }
        return convertView1
    }

}
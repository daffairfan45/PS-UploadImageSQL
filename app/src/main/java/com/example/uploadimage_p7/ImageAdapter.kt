import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uploadimage_p7.R

class ImageAdapter(private val imageList: List<ImageModel>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = imageList[position]
        holder.titleTextView.text = currentItem.title
        val bitmap = BitmapFactory.decodeByteArray(currentItem.image, 0, currentItem.image.size)
        holder.imageView.setImageBitmap(bitmap)
    }

    override fun getItemCount() = imageList.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}

data class ImageModel(val id: Int, val title: String, val image: ByteArray)

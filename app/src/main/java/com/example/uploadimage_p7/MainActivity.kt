package com.example.uploadimage_p7


import DatabaseHelper
import ImageAdapter
import ImageModel
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uploadimage_p7.R
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var saveButton: Button
    private lateinit var viewButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val PICK_IMAGE = 1
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleEditText = findViewById(R.id.titleEditText)
        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadButton)
        saveButton = findViewById(R.id.saveButton)
        viewButton = findViewById(R.id.viewButton)
        statusTextView = findViewById(R.id.statusTextView)
        recyclerView = findViewById(R.id.recyclerView)

        databaseHelper = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            val result = databaseHelper.insertImage(title, byteArray)
            if (result != -1L) {
                statusTextView.text = "Image saved successfully!"
            } else {
                statusTextView.text = "Failed to save image."
            }
        }

        viewButton.setOnClickListener {
            val cursor = databaseHelper.readableDatabase.rawQuery("SELECT * FROM images", null)
            val imageList = mutableListOf<ImageModel>()
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
                    imageList.add(ImageModel(id, title, image))
                } while (cursor.moveToNext())
            }
            cursor.close()
            val adapter = ImageAdapter(imageList)
            recyclerView.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage = data.data
            imageView.setImageURI(selectedImage)
        }
    }
}

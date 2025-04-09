package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import com.intprog.tableflow.model.SessionManager

class ProfilePictureSelectionScreen : Activity() {

    private val profilePictures = arrayOf(
        R.drawable.pfp1,
        R.drawable.pfp2,
        R.drawable.pfp3,
        R.drawable.pfp4,
        R.drawable.pfp5,
        R.drawable.pfphehe
    )

    private var selectedPictureId: Int = -1
    private var previousSelectedImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture_selection_screen)

        val backButton: LinearLayout = findViewById(R.id.backButton)
        val confirmButton: TextView = findViewById(R.id.confirmButton)
        val gridLayout: GridLayout = findViewById(R.id.profilePicturesGrid)

        val sessionManager = SessionManager(this)
        val currentProfilePicId = sessionManager.getUserDetails().profilePictureId
        selectedPictureId = currentProfilePicId

        // Dynamically add profile pictures to the grid
        setupProfilePictureGrid(gridLayout, currentProfilePicId)

        backButton.setOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {
            if (selectedPictureId != -1) {
                // Instead of saving to SessionManager here, just return the selected ID
                val resultIntent = Intent()
                resultIntent.putExtra("SELECTED_PROFILE_PICTURE_ID", selectedPictureId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupProfilePictureGrid(gridLayout: GridLayout, currentProfilePicId: Int) {
        val columnCount = 3
        gridLayout.columnCount = columnCount
        gridLayout.rowCount = (profilePictures.size + columnCount - 1) / columnCount

        for (i in profilePictures.indices) {
            val pictureId = profilePictures[i]

            // Create image view with margin
            val imageView = ImageView(this)
            val params = GridLayout.LayoutParams()
            params.width = resources.getDimensionPixelSize(R.dimen.profile_pic_grid_size)
            params.height = resources.getDimensionPixelSize(R.dimen.profile_pic_grid_size)
            params.setMargins(8, 8, 8, 8)

            // Set row and column
            params.rowSpec = GridLayout.spec(i / columnCount)
            params.columnSpec = GridLayout.spec(i % columnCount)

            imageView.layoutParams = params
            imageView.setImageResource(pictureId)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            // Make the ImageView have rounded corners
            imageView.clipToOutline = true
            imageView.outlineProvider = ViewOutlineProvider.BACKGROUND
            imageView.background = ContextCompat.getDrawable(this, R.drawable.profile_pic_border)

            // If this is the current profile picture, mark it as selected
            if (pictureId == currentProfilePicId) {
                imageView.foreground = ContextCompat.getDrawable(this, R.drawable.profile_pic_selected_border)
                previousSelectedImageView = imageView
            } else {
                imageView.foreground = ContextCompat.getDrawable(this, R.drawable.profile_pic_border)
            }

            imageView.setOnClickListener {
                // Update selection
                selectedPictureId = pictureId

                // Update visual selection state
                previousSelectedImageView?.foreground = ContextCompat.getDrawable(this, R.drawable.profile_pic_border)
                imageView.foreground = ContextCompat.getDrawable(this, R.drawable.profile_pic_selected_border)
                previousSelectedImageView = imageView
            }

            gridLayout.addView(imageView)
        }
    }
}
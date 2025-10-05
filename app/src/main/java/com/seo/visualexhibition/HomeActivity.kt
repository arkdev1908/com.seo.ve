package com.seo.visualexhibition

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seo.visualexhibition.data.model.ThemeModel
import com.seo.visualexhibition.ui.home.ThemeAdapter

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rvThemes = findViewById<RecyclerView>(R.id.rvThemes)
        rvThemes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val themes = listOf(
            ThemeModel(R.drawable.avatar_1, "1930 - 1945"),
            ThemeModel(R.drawable.avatar_2, "1945 - 1954"),
            ThemeModel(R.drawable.avatar_1, "1954 - 1975"),
            ThemeModel(R.drawable.avatar_1, "1975 - 1986"),
            ThemeModel(R.drawable.avatar_1, "1986 - Nay"),
            ThemeModel(R.drawable.avatar_1, "1986 - 1"),
            ThemeModel(R.drawable.avatar_1, "1986 - 2"),
        )

        rvThemes.adapter = ThemeAdapter(themes) { item ->
            Toast.makeText(this, "Chọn ${item.period}", Toast.LENGTH_SHORT).show()
        }
    }
}
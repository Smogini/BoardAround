package com.example.boardaround

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.boardaround.ui.theme.BoardAroundTheme

class HomepageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardAroundTheme() {
                Text("Benvenuto nella Homepage!")
            }
        }
    }
}
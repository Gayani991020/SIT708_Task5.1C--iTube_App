package com.example.task5_1_itube;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.videoplayer);

        // Retrieve the YouTube player view from the layout
        YouTubePlayerView youTubePlayerView = findViewById(R.id.videoPlayer);

        // Get the video ID passed via Intent
        String videoId = getIntent().getStringExtra("VIDEO_ID");

        // Verify if the video ID is valid
        if (videoId == null || videoId.isEmpty()) {
            // Display a toast message if the video ID is invalid or missing
            Toast.makeText(this, "Invalid video ID", Toast.LENGTH_SHORT).show();
            // Exit the method to prevent further execution
            return;
        }

        // Add a listener to the YouTube player view
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                // Load and play the video from the beginning when the player is ready
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }
}

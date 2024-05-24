package com.example.task5_1_itube;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuActivity extends AppCompatActivity {

    // UI elements
    private EditText youtubeLinkEditText; // EditText for entering YouTube link
    private Button playButton; // Button to play the video
    private Button addToPlaylistButton; // Button to add the video to the playlist
    private Button myPlaylistButton; // Button to view the playlist
    private UserDatabaseHelper dbHelper; // Database helper to manage user data
    private final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"; // Regex pattern for YouTube URLs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // Set the activity's layout

        // Initialize UI components
        youtubeLinkEditText = findViewById(R.id.linkTextView); // Input field for YouTube link
        playButton = findViewById(R.id.playButton); // Button to play video
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton); // Button to add video to playlist
        myPlaylistButton = findViewById(R.id.myPlaylistButton); // Button to view playlist

        // Initialize the database helper
        dbHelper = new UserDatabaseHelper(this);

        // Set the listener for the "Add to Playlist" button
        addToPlaylistButton.setOnClickListener(v -> {
            // Get the YouTube link entered by the user
            String youtubeLink = youtubeLinkEditText.getText().toString().trim();

            // Check if the link is not empty
            if (!youtubeLink.isEmpty()) {
                // Add the link to the playlist in the database
                boolean isAdded = dbHelper.addLinkToPlaylist(youtubeLink);

                // Show a toast message based on the success or failure of the operation
                if (isAdded) {
                    Toast.makeText(this, "Added to Playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add to Playlist", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Prompt the user to enter a YouTube link if the input is empty
                Toast.makeText(this, "Please enter a YouTube link", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the listener for the "Play" button
        playButton.setOnClickListener(v -> {
            // Get the YouTube link entered by the user
            String youtubeLink = youtubeLinkEditText.getText().toString().trim();

            // Check if the link is not empty
            if (!youtubeLink.isEmpty()) {
                // Extract the video ID from the link
                String videoId = extractVideoIdFromUrl(youtubeLink);

                // Check if a valid video ID was found
                if (videoId != null && !videoId.isEmpty()) {
                    // Create an intent to start VideoPlayActivity
                    Intent intent = new Intent(MenuActivity.this, VideoPlayActivity.class);
                    // Pass the video ID to the next activity
                    intent.putExtra("VIDEO_ID", videoId);
                    // Start VideoPlayActivity
                    startActivity(intent);
                } else {
                    // Notify the user if the YouTube link is invalid
                    Toast.makeText(MenuActivity.this, "Invalid YouTube Link", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Prompt the user to enter a YouTube link if the input is empty
                Toast.makeText(this, "Please enter a YouTube link", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the listener for the "My Playlist" button
        myPlaylistButton.setOnClickListener(v -> {
            // Create an intent to start MyPlaylistActivity
            Intent intent = new Intent(MenuActivity.this, MyPlaylistActivity.class);
            // Start MyPlaylistActivity
            startActivity(intent);
        });
    }

    /**
     * Extracts the video ID from a YouTube URL.
     *
     * @param url The YouTube URL.
     * @return The extracted video ID, or null if not found.
     */
    private String extractVideoIdFromUrl(String url) {
        // Remove the protocol and domain from the URL
        String cleanedUrl = youTubeLinkWithoutProtocolAndDomain(url);

        // Patterns to match possible video ID formats
        final String[] videoIdPatterns = {
                "\\?vi?=([^&]*)", // Pattern for URLs with vi or v parameter
                "watch\\?.*v=([^&]*)", // Pattern for watch URLs
                "(?:embed|vi?)/([^/?]*)", // Pattern for embed or vi URLs
                "^([A-Za-z0-9\\-]*)" // Pattern for short URLs
        };

        // Iterate over patterns to find a match
        for (String pattern : videoIdPatterns) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(cleanedUrl);

            // Return the video ID if a match is found
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        // Return null if no match is found
        return null;
    }

    /**
     * Removes the protocol and domain from a YouTube URL.
     *
     * @param url The YouTube URL.
     * @return The URL without protocol and domain.
     */
    private String youTubeLinkWithoutProtocolAndDomain(String url) {
        // Compile the regex pattern to match protocol and domain
        Pattern pattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = pattern.matcher(url);

        // If the pattern matches, remove the protocol and domain
        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }

        // Return the original URL if no match is found
        return url;
    }
}

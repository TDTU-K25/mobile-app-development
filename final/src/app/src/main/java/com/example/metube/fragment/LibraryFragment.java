package com.example.metube.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.metube.R;
import com.example.metube.SubLibraryActivity;
import com.example.metube.SubscriptionActivity;
import com.example.metube.model.Playlist;
import com.example.metube.model.Video;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private Button btnPlaylist;
    private Button btnHistory;
    private Button btnFavorite;
    private Button btnSubscribedChannel;
    SharedPreferences sharedPref;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID;
    DocumentReference userRef;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        btnFavorite = (Button) view.findViewById(R.id.btn_video_liked);
        btnHistory = (Button) view.findViewById(R.id.btn_video_watched);
        btnPlaylist = (Button) view.findViewById(R.id.btn_playlist);
        btnSubscribedChannel = (Button) view.findViewById(R.id.btn_subscription);

        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubLibraryActivity.class);
                intent.putExtra("title", "playlist");
                startActivity(intent);
            }
        });

        btnSubscribedChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubLibraryActivity.class);
                intent.putExtra("title", "history");
                startActivity(intent);
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubLibraryActivity.class);
                intent.putExtra("title", "favorite");
                startActivity(intent);
            }
        });

        List<Video> videosWatched = new ArrayList<>();
        List<Video> videosLiked = new ArrayList<>();
        List<Playlist> playlists = new ArrayList<>();
        List<String> videosID = new ArrayList<>();


        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_login_key), MODE_PRIVATE);
        userID = (sharedPref.getString(getString(R.string.saved_account_uid_key), ""));
        userRef = firestore.collection("Users").document(userID);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                if (userTask.isSuccessful()) {
                    DocumentSnapshot userDocument = userTask.getResult();
                    if (userDocument.exists()) {
                        List<String> watchedVideosID = (List<String>) userDocument.get("watched");
                        List<String> likedVideosID = (List<String>) userDocument.get("likedVideos");

                        if (watchedVideosID != null && likedVideosID != null) {
                            List<Task<List<DocumentSnapshot>>> allTasks = new ArrayList<>();

                            // Task for watched videos
                            Task<List<DocumentSnapshot>> watchedVideosTask = fetchVideos(watchedVideosID);
                            allTasks.add(watchedVideosTask);

                            // Task for liked videos
                            Task<List<DocumentSnapshot>> likedVideosTask = fetchVideos(likedVideosID);
                            allTasks.add(likedVideosTask);

                            Task<List<Object>> allVideosTasks = Tasks.whenAllSuccess(allTasks);

                            allVideosTasks.addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                                @Override
                                public void onComplete(@NonNull Task<List<Object>> task) {
                                    if (task.isSuccessful()) {
                                        List<Object> results = task.getResult();

                                        List<DocumentSnapshot> watchedVideoSnapshots = (List<DocumentSnapshot>) results.get(0);
                                        List<DocumentSnapshot> likedVideoSnapshots = (List<DocumentSnapshot>) results.get(1);

                                        // Process watched videos
                                        List<Video> videosWatched = processVideoSnapshots(watchedVideoSnapshots);

                                        // Process liked videos
                                        List<Video> videosLiked = processVideoSnapshots(likedVideoSnapshots);

                                        // Replace fragments or perform other actions as needed
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.fragment_container_video_watched, new ListHorizontalFragment(videosWatched));
                                        transaction.replace(R.id.fragment_container_video_liked, new ListHorizontalFragment(videosLiked));
                                        transaction.commit();
                                    } else {
                                        Toast.makeText(getActivity(), "Error getting videos.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Error getting user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    private Task<List<DocumentSnapshot>> fetchVideos(List<String> videoIds) {
        List<Task<DocumentSnapshot>> videoTasks = new ArrayList<>();

        for (String videoId : videoIds) {
            DocumentReference videoRef = firestore.collection("Videos").document(videoId);
            Task<DocumentSnapshot> videoTask = videoRef.get();
            videoTasks.add(videoTask);
        }

        return Tasks.whenAllSuccess(videoTasks);
    }

    private Task<List<DocumentSnapshot>> fetchPlaylist(List<String> playlistIds) {
        List<Task<DocumentSnapshot>> playlistTasks = new ArrayList<>();

        for (String playlistId : playlistIds) {
            DocumentReference playlistRef = firestore.collection("Playlists").document(playlistId);
            Task<DocumentSnapshot> playlistTask = playlistRef.get();
            playlistTasks.add(playlistTask);
        }

        return Tasks.whenAllSuccess(playlistTasks);
    }

    private List<Video> processVideoSnapshots(List<DocumentSnapshot> videoSnapshots) {
        List<Video> videos = new ArrayList<>();

        for (DocumentSnapshot videoSnapshot : videoSnapshots) {
            if (videoSnapshot.exists()) {
                Video video = new Video(
                        videoSnapshot.getId(),
                        videoSnapshot.getString("title"),
                        videoSnapshot.getLong("views").intValue(),
                        new Date(2023, 10, 10),
                        new Date(2023, 10, 10),
                        videoSnapshot.getString("url"),
                        videoSnapshot.getString("thumbnail")
                );
                videos.add(video);
            }
        }

        return videos;
    }

    private List<Playlist> processPlaylistSnapshots(List<DocumentSnapshot> playlistSnapshots) {
        List<Playlist> playlists = new ArrayList<>();

        for (DocumentSnapshot playlistSnapshot : playlistSnapshots) {
            if (playlistSnapshot.exists()) {
                Playlist playlist = new Playlist(
                        playlistSnapshot.getString("userID"),
                        playlistSnapshot.getString("title"),
                        playlistSnapshot.getBoolean("state"),
                        playlistSnapshot.getDate("createAt"),
                        playlistSnapshot.getLong("numOfVideo").intValue(),
                        playlistSnapshot.getString("thumbnail"),
                        (List<String>) playlistSnapshot.get("videos")
                );
                playlists.add(playlist);
            }
        }

        return playlists;
    }
}
package com.example.metube;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.Interface.DownloadCallback;
import com.example.metube.adapter.CommentAdapter;
import com.example.metube.adapter.VideoAdapter;
import com.example.metube.model.Comment;
import com.example.metube.model.Video;
import com.example.metube.utils.TimeUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.List;

public class VideoDetailActivity extends AppCompatActivity implements CommentAdapter.OnCommentClickListener, DownloadCallback, VideoAdapter.OnVideoClickListener {
    final static String VIDEOS = "Videos";
    final static String USERS = "Users";
    final static String COMMENTS = "Comments";
    Button btnSubscribe;
    TextView tvDownload;
    TextView tvViews;
    EditText etComment;
    Button btnSendComment;
    ProgressBar progressBar;
    ImageView ivLike;
    TextView tvShare;
    RecyclerView rv, rvComments;
    VideoAdapter adapter;
    CommentAdapter commentAdapter;
    VideoView vv;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String videoId;
    String videoUrl;
    String channelId;
    TextView tvPublishedTime;
    TextView tvTitle;
    ImageView ivChannelAvatar;
    TextView tvChannelName;
    TextView tvNumberOfSubscribers;
    boolean isAddComment = true; // true: add comment, false: edit comment
    String userID;

    public static void download(String downloadUrl, File parent, DownloadCallback callback) {
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.connect();

            String fileName = getFileName(downloadUrl, conn);

            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[10 * 1024];
            int size;

            FileOutputStream stream = new FileOutputStream(new File(parent, fileName));

            while ((size = is.read(buffer)) > 0) {
                stream.write(buffer, 0, size);
            }

            callback.onComplete();

            stream.close();
            is.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(String fileURL, HttpURLConnection httpConn) {
        String fileName = "";
        String disposition = httpConn.getHeaderField("Content-Disposition");

        if (disposition != null) {
            // extracts file name from header field
            String[] parts = disposition.split("'");
            fileName = parts[parts.length - 1];
        } else {
            // extracts file name from URL
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        }
        return fileName;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_login_key), MODE_PRIVATE);
        userID = sharedPref.getString(getString(R.string.saved_account_uid_key), "");

        tvShare = findViewById(R.id.tv_share);
        vv = findViewById(R.id.vv);
        rv = findViewById(R.id.rv_related_videos);
        rvComments = findViewById(R.id.rv_comments);
        progressBar = findViewById(R.id.progress_bar);
        etComment = findViewById(R.id.et_comment);
        btnSendComment = findViewById(R.id.btn_send_comment);
        tvDownload = findViewById(R.id.tv_download);
        ivLike = findViewById(R.id.iv_like);
        btnSubscribe = findViewById(R.id.btn_subscribe);
        tvViews = findViewById(R.id.tv_views);
        tvPublishedTime = findViewById(R.id.tv_published_time);
        tvTitle = findViewById(R.id.tv_title);
        ivChannelAvatar = findViewById(R.id.iv_channel_avatar);
        tvChannelName = findViewById(R.id.tv_channel_name);
        tvNumberOfSubscribers = findViewById(R.id.tv_number_subscribers);


        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");

        // Subscribe/Unsubscribe channel
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection(USERS).document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> subscribedChannels = (List<String>) documentSnapshot.get("subscribedChannels");
                        String textNotification;
                        if (subscribedChannels.contains(channelId)) {
                            subscribedChannels.remove(channelId);
                            btnSubscribe.setText("Subscribe");
                            textNotification = "Unsubscribe channel";
                        } else {
                            subscribedChannels.add(channelId);
                            btnSubscribe.setText("Unsubscribe");
                            textNotification = "Subscribe channel";
                        }
                        firestore.collection(USERS).document(userID).update("subscribedChannels", subscribedChannels).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(VideoDetailActivity.this, textNotification, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        // Like/Unlike video
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection(USERS).document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> likedVideos = (List<String>) documentSnapshot.get("likedVideos");
                        String textNotification;
                        if (likedVideos.contains(videoId)) {
                            likedVideos.remove(videoId);
                            ivLike.setImageResource(R.drawable.thumb_up_fill0_wght400_grad0_opsz24);
                            textNotification = "Unlike video";
                        } else {
                            likedVideos.add(videoId);
                            ivLike.setImageResource(R.drawable.thumb_down_fill0_wght400_grad0_opsz24);
                            textNotification = "Like video";
                        }
                        firestore.collection(USERS).document(userID).update("likedVideos", likedVideos).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(VideoDetailActivity.this, textNotification, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        // Comment
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddComment) {
                    String content = etComment.getText().toString();
                    // upload to firestore
                    Comment comment = new Comment(videoId, userID, content, new Date(System.currentTimeMillis()));
                    firestore.collection(VIDEOS).document(videoId).collection(COMMENTS).add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            documentReference.update("id", documentReference.getId());
                            Toast.makeText(VideoDetailActivity.this, "Comment sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VideoDetailActivity.this, "Failed to send comment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Share
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoDetailActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_share, null);
                EditText etShareUrl = layout.findViewById(R.id.et_share_url);
                ImageView ivShareUrlCopy = layout.findViewById(R.id.iv_share_url_copy);

                firestore.collection(VIDEOS).document(videoId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String url = documentSnapshot.get("url").toString();
                        etShareUrl.setText(url);
                        ivShareUrlCopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("url", url);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(VideoDetailActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setView(layout);
                builder.create().show();
            }
        });

        // Get user data and set to view
        firestore.collection(USERS).document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> likedVideos = (List<String>) documentSnapshot.get("likedVideos");
                if (likedVideos.contains(videoId)) {
                    ivLike.setImageResource(R.drawable.thumb_down_fill0_wght400_grad0_opsz24);
                } else {
                    ivLike.setImageResource(R.drawable.thumb_up_fill0_wght400_grad0_opsz24);
                }
            }
        });

        // Get video data and set to view
        firestore.collection(VIDEOS).document(videoId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                channelId = documentSnapshot.get("channelId").toString();
                String url = documentSnapshot.get("url").toString();
                videoUrl = url;
                Uri uri = Uri.parse(url);
                vv.setVideoURI(uri);

                tvTitle.setText(documentSnapshot.get("title").toString());
                tvPublishedTime.setText(TimeUtil.getTimeDistance(documentSnapshot.getDate("publishDate")));

                // Get channel data and set to view
                firestore.collection(USERS).document(channelId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Glide.with(VideoDetailActivity.this).load(documentSnapshot.get("avatar").toString()).into(ivChannelAvatar);
                        tvChannelName.setText(documentSnapshot.get("name").toString());
                        List<String> subscribedChannels = (List<String>) documentSnapshot.get("subscribedChannels");
                        String numberSubscribers = "";
                        if (subscribedChannels != null) {
                            numberSubscribers = subscribedChannels.size() + " subs";
                        } else {
                            numberSubscribers = "0 subs";
                        }
                        tvNumberOfSubscribers.setText(numberSubscribers);
                    }
                });

                MediaController mediaController = new MediaController(vv.getContext());
                vv.setMediaController(mediaController);
                mediaController.setAnchorView(vv);

                vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        progressBar.setVisibility(ProgressBar.GONE);
                        updateVideoViews();
                        updateVideoWatched();
                    }
                });

                vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        // if video is rendered or buffered (the same)
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                            return true;
                        }
                        return false;
                    }
                });

            }
        });

        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File parent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            download(videoUrl, parent, VideoDetailActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }
        });

        setUpRecyclerViewComments();
        setUpRecyclerView();
    }

    public void updateVideoViews() {
        DocumentReference videoRef = firestore.collection(VIDEOS).document(videoId);
        videoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long views = (long) documentSnapshot.get("views");
                videoRef.update("views", views + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvViews.setText(views + 1 + " views");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VideoDetailActivity.this, "Failed to update views", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void updateVideoWatched() {
        DocumentReference userRef = firestore.collection(USERS).document(userID);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> videoIDs = (List<String>) documentSnapshot.get("watched");
                if (!videoIDs.contains(videoId)) {
                    videoIDs.add(videoId);
                    userRef.update("watched", videoIDs).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
                }
            }
        });

    }

    private void setUpRecyclerViewComments() {
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvComments.setLayoutManager(linearLayoutManager);

        // set adapter
        Query query = firestore.collection("Videos").document(videoId).collection(COMMENTS);
        // simply get the data from query to the adapter
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, Comment.class).build();
        commentAdapter = new CommentAdapter(options, this);
        rvComments.setAdapter(commentAdapter);
    }

    private void setUpRecyclerView() {
        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        // set adapter
        Query query = firestore.collection("Videos");
        // simply get the data from query to the adapter
        FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>().setQuery(query, Video.class).build();
        adapter = new VideoAdapter(options, this, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onEditCommentClick(String cmtId) {
        firestore.collection(VIDEOS).document(videoId).collection(COMMENTS).document(cmtId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Comment comment = documentSnapshot.toObject(Comment.class);
                if (comment.getUserId().equals(userID)) {
                    isAddComment = false;
                    firestore.collection(VIDEOS).document(videoId).collection(COMMENTS).document(cmtId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            etComment.setText(documentSnapshot.get("content").toString());
                        }
                    });
                    btnSendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isAddComment) {
                                firestore.collection(VIDEOS).document(videoId).collection(COMMENTS).document(cmtId).update("content", etComment.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(VideoDetailActivity.this, "Comment updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            isAddComment = true;
                        }
                    });
                } else {
                    Toast.makeText(VideoDetailActivity.this, "You can only edit your own comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDeleteCommentClick(String cmtId) {
        firestore.collection(VIDEOS).document(videoId).collection(COMMENTS).document(cmtId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Comment comment = documentSnapshot.toObject(Comment.class);
                if (comment.getUserId().equals(userID)) {
                    firestore.collection(VIDEOS).document(videoId).collection(COMMENTS).document(cmtId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(VideoDetailActivity.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(VideoDetailActivity.this, "You can only delete your own comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        commentAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
        commentAdapter.stopListening();
    }

    @Override
    public void onComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoDetailActivity.this, "Download completed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVideoClick(String id) {
        Intent intent = new Intent(this, VideoDetailActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
        finish();
    }
}
package com.teamnova.bongapp.FetchBooks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamnova.bongapp.R;
import com.teamnova.bongapp.Timer.BooksVideo;
import com.teamnova.bongapp.Timer.ReadingTimer;

import java.util.List;

/**
 * Created by Bhavya Arora on 1/10/2018.
 */

public class FetchBooksAdapter extends RecyclerView.Adapter<FetchBooksAdapter.MyViewHolder> {

    private Context mContext;
    private List<book> bookList;

    //last item show on screen
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, publisher;
        public ImageView thumbnail;
        public Button infoBtn, videoBtn, readingBtn, likeBtn;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            author = (TextView) view.findViewById(R.id.count);
            publisher = (TextView) view.findViewById(R.id.publisher);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            infoBtn = (Button) view.findViewById(R.id.info);
            videoBtn = (Button) view.findViewById(R.id.video);
            readingBtn = (Button) view.findViewById(R.id.readingTimer);
            likeBtn = (Button) view.findViewById(R.id.likeIt);
        }
    }


    public FetchBooksAdapter(Context mContext, List<book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int itemPosition = parent.indexOfChild(view);
//                book returnedBook = bookList.get(itemPosition);
//                String infoUrl = returnedBook.getInfoUrl();
//                //Intent intent = new Intent(Intent.ACTION_VIEW);
//                //intent.setData(Uri.parse(infoUrl));
//
//                Intent intentUrl = new Intent(mContext,bookListView.class);
//                intentUrl.putExtra("infoUrl", infoUrl);
//                mContext.startActivity(intentUrl);
//                //browser chooser
//                //Intent.createChooser(intent, "Choose Broser");
//            }
//        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final book returnedBook = bookList.get(position);
        // loading Book cover using Glide library
        Glide.with(mContext)
            .load(returnedBook.getImageUrl())
            .into(holder.thumbnail);
        // loading Book Title, author, publisher
        holder.title.setText(returnedBook.getTitle());
        holder.author.setText(returnedBook.getAuthor());
        holder.publisher.setText(returnedBook.getPublisher());

        //상세정보
        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "상세 정보", Toast.LENGTH_SHORT).show();
                String infoUrl = returnedBook.getInfoUrl();
                Intent intentUrl = new Intent(mContext, bookListView.class);
                intentUrl.putExtra("infoUrl", infoUrl);
                mContext.startActivity(intentUrl);
                //browser chooser
                //Intent.createChooser(intent, "Choose Broser");
            }
        });


        //관련 영상
        holder.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BooksVideo.class);
                mContext.startActivity(intent);
            }
        });

        //독서타이머
        holder.readingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = returnedBook.getTitle();
                String authors = returnedBook.getAuthor();
                String imageUrl = returnedBook.getImageUrl();
                String publisher = returnedBook.getPublisher();
                boolean isFindBook = false;
                if(title.length() > 0){
                  isFindBook = true;
                }
                Intent intent = new Intent(mContext, ReadingTimer.class);
                intent.putExtra("title", title);
                intent.putExtra("authors", authors);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("publisher", publisher);
                intent.putExtra("isFindBook", isFindBook);
                mContext.startActivity(intent);
            }
        });


        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "좋아요", Toast.LENGTH_SHORT).show();
            }
        });
        //아이템 생성 될때 애니메이션 처리
        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    //새로 보여지는 아이템 뷰라면 애니메이션을 해줍니다.
    private void setAnimation(View viewToAnimation, int position) {
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimation.startAnimation(animation);
            lastPosition = position;
        }
    }
}


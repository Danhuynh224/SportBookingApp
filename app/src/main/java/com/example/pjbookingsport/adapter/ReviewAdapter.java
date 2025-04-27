package com.example.pjbookingsport.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjbookingsport.R;
import com.example.pjbookingsport.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_review, parent, false);
        return new ReviewViewHolder(view);
    }


    @Override
    public int getItemCount() {
        if (reviewList != null) {
            return reviewList.size();
        }
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.tvNameUser.setText(review.getUser().getFullName());
        holder.tvDateReview.setText(review.getCreatedAt().toString());
        holder.ratingValue.setRating(review.getRating());

        makeExpandableText(holder.tvContentReview, review.getComment(), 2);

    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameUser, tvDateReview, tvContentReview;
        AppCompatRatingBar ratingValue;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameUser = itemView.findViewById(R.id.tv_name_user);
            tvDateReview = itemView.findViewById(R.id.tv_date_review);
            tvContentReview = itemView.findViewById(R.id.tv_content_review);
            ratingValue = itemView.findViewById(R.id.ratingValue);

        }
    }

    private void makeExpandableText(TextView textView, String fullText, int maxLines) {
        textView.setText(fullText);

        textView.post(() -> {
            Layout layout = textView.getLayout();
            if (layout != null) {
                if (layout.getLineCount() > maxLines) {
                    int endOfLastLine = layout.getLineEnd(maxLines - 1);
                    String trimmedText = fullText.substring(0, endOfLastLine).trim();

                    String expandText = "... Xem thêm";
                    SpannableString spannableString = new SpannableString(trimmedText + expandText);

                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            makeCollapsableText(textView, fullText, maxLines);
                        }

                        @Override
                        public void updateDrawState(@NonNull TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(Color.parseColor("#0056BD"));
                            ds.setUnderlineText(false);
                        }
                    };

                    spannableString.setSpan(clickableSpan, spannableString.length() - expandText.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    textView.setText(spannableString);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    // Text ngắn dưới maxLines thì show full
                    textView.setText(fullText);
                }
            }
        });
    }

    private void makeCollapsableText(TextView textView, String fullText, int maxLines) {
        String collapseText = " Thu gọn";
        SpannableString spannableString = new SpannableString(fullText + collapseText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                makeExpandableText(textView, fullText, maxLines);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#0056BD"));
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, spannableString.length() - collapseText.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }



}

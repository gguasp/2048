/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.a2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Implements a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class WordListAdapter extends RecyclerView.Adapter<com.example.a2048.WordListAdapter.WordViewHolder> {
    private WordListOpenHelper mDB;

    /**
     *  Custom view holder with a text view and two buttons.
     */
    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView puntuation;
        public final TextView username;
        public final TextView date;
        Button edit_button;
        Button delete_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            puntuation = (TextView) itemView.findViewById(R.id.word);
            username = (TextView) itemView.findViewById(R.id.usernameTextView);
            date = (TextView) itemView.findViewById(R.id.dateTextView);
            edit_button = (Button)itemView.findViewById(R.id.edit_button);
            delete_button = (Button)itemView.findViewById(R.id.delete_button);
        }
    }

    private final LayoutInflater mInflater;
    Context mContext;

    public WordListAdapter(Context context, WordListOpenHelper DB) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mDB=DB;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.ranking_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WordViewHolder holder, final int position) {
        final WordItem current = mDB.query(position);
        holder.puntuation.setText(current.getPuntuation());
        holder.username.setText(current.getUsername());
        holder.date.setText(current.getDate());
        holder.edit_button.setOnClickListener(new MyButtonOnClickListener(
                current.getmId(), current.getPuntuation()) {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, EditWordActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                ((Activity) mContext).startActivityForResult(
                        intent, RankingActivity.WORD_EDIT);
                notifyDataSetChanged();
            }
        });

        holder.delete_button.setOnClickListener(new MyButtonOnClickListener( current.getmId(), current.getPuntuation()) {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDB.delete(current.getmId());
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }
}



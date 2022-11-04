package com.example.handoutlms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter2 extends RecyclerView.Adapter<UsersAdapter2.ViewHolder> {

    private Context mContext;
    private List<Users> mChat;

    public UsersAdapter2(Context mContext, List<Users> mChat){
        this.mChat = mChat;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public UsersAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_chat, parent, false);
        return new UsersAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter2.ViewHolder holder, int position) {

//        Chat chat = mChat.get(position);
        Users users = mChat.get(position);

        holder.username.setText(users.getFullname());
        holder.description.setText(users.getInstitution());
        if(users.getStatus().equals("online")){
            holder.img_on.setVisibility(View.VISIBLE);
            holder.img_off.setVisibility(View.GONE);
            holder.onoff.setText("online");
            holder.onoff.setTextColor(Color.parseColor("#05df29"));
        }
        if(users.getStatus().equals("offline")){
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.VISIBLE);
            holder.onoff.setText("offline");
            holder.onoff.setTextColor(Color.parseColor("#bfbfbf"));
        }
        holder.myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to the chat page
                Intent j = new Intent(mContext, ChatMessagingPage.class);
                j.putExtra("key", users.getId());
                j.putExtra("id", users.getId());
                j.putExtra("name", users.getFullname());
                j.putExtra("email", users.getEmail());
                j.putExtra("institution", users.getInstitution());
//                j.putExtra("senderKey", users.getId());
                mContext.startActivity(j);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView description;
        public TextView onoff;
        public CircleImageView img_on;
        public CircleImageView img_off;
        LinearLayout myList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            onoff = itemView.findViewById(R.id.txtonoff);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            myList = itemView.findViewById(R.id.myList);

        }
    }

}

package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.banqu.samsung.music.R;

import java.util.ArrayList;

public class MsgBoxAdaptor extends RecyclerView.Adapter<MsgBoxAdaptor.ViewHolder> {

    ArrayList<Msg> msgs;

    public MsgBoxAdaptor(Context context) {
        Log.i("MSGTEST","New");
        msgs = new ArrayList<>();
        Msg a = new Msg();
        a.label = "车联助手";
        a.title = "暂无信息";
        a.text = "请注意安全驾驶！";
        a.icon = context.getDrawable(R.mipmap.ic_launcher_round);
        msgs.add(a);
    }

    @NonNull
    @Override
    public MsgBoxAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.msgbox_line, parent, false);
        MsgBoxAdaptor.ViewHolder viewHolder = new MsgBoxAdaptor.ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView msg_icon;
        public TextView msg_label;
        public TextView msg_title;
        public TextView msg_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg_icon = (ImageView) itemView.findViewById(R.id.msg_icon);
            msg_label = (TextView) itemView.findViewById(R.id.msg_label);
            msg_title = (TextView) itemView.findViewById(R.id.msg_title);
            msg_text = (TextView) itemView.findViewById(R.id.msg_text);
        }

        @Override
        public void onClick(View view) {
            Log.i("MSGTEST","Click");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MsgBoxAdaptor.ViewHolder holder, int position) {
        Log.i("MSGTEST","Bind");
        holder.msg_label.setText(msgs.get(position).label);
        holder.msg_title.setText(msgs.get(position).title);
        holder.msg_text.setText(msgs.get(position).text);
        holder.msg_icon.setImageDrawable(msgs.get(position).icon);
    }

    @Override
    public int getItemCount() {
        Log.i("MSGTEST",""+msgs.size());
        return msgs.size();
    }


    public void update(ArrayList<Msg>msgs)
    {
        this.msgs.clear();
        this.msgs.addAll(msgs);
        Log.i("MSGTEST",""+msgs.size());
        notifyDataSetChanged();
    }
}

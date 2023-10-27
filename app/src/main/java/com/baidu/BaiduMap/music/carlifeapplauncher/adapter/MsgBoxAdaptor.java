package com.baidu.BaiduMap.music.carlifeapplauncher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.BaiduMap.music.R;

import java.util.ArrayList;

public class MsgBoxAdaptor extends RecyclerView.Adapter<MsgBoxAdaptor.ViewHolder> {

    ArrayList<Msg> msgs;
    //    WindowManager wm;
//    Insets insets;
    int iconsize;
    int fontsize;
    int fontcolor;
    Context context;

    public MsgBoxAdaptor(Context context) {
        Log.i("MSGTEST", "New");
        this.context = context;
        msgs = new ArrayList<>();
        Msg a = new Msg();
        a.label = "车联助手";
        a.title = "暂无信息";
        a.text = "请注意安全驾驶！";
        a.icon = context.getDrawable(R.mipmap.ic_launcher_round);
        msgs.add(a);

        iconsize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("msg_box_icon_size", "120"));
        fontsize = Common.spTopx(context, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("msg_box_font_size", "12")));
        fontcolor = context.getResources().getColor(R.color.OneUI_Primary, context.getTheme());
        //        Toast.makeText(context,"size:"+fontsize,Toast.LENGTH_LONG).show();

//        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
//        WindowInsets windowInsets = windowMetrics.getWindowInsets();
//        insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
//
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
            Log.i("MSGTEST", "Click");
        }
    }

    private void getsp(String text, TextView textView) {

        textView.setText(text, TextView.BufferType.EDITABLE);
        Spannable sp = (Spannable) textView.getText();
        sp.setSpan(new AbsoluteSizeSpan(fontsize), 0, textView.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //设置字体大小
        sp.setSpan(new ForegroundColorSpan(fontcolor), 0, textView.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        ;
        textView.setText(sp);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgBoxAdaptor.ViewHolder holder, int position) {
        Log.i("MSGTEST", "Bind");
        String label = msgs.get(position).label;
        String title = msgs.get(position).title;
        String text = msgs.get(position).text;


        getsp(label, holder.msg_label);
        getsp(title, holder.msg_title);
        getsp(text, holder.msg_text);


        holder.msg_icon.setImageDrawable(msgs.get(position).icon);

        ViewGroup.LayoutParams lp2 = holder.msg_icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        holder.msg_icon.setLayoutParams(lp2);
    }

    @Override
    public int getItemCount() {
        Log.i("MSGTEST", "" + msgs.size());
        return msgs.size();
    }


    public void update(ArrayList<Msg> newmsgs) {
        if (newmsgs != null && newmsgs.size() != 0) {
            this.msgs.clear();
            this.msgs.addAll(newmsgs);
//            Log.i("MSGTEST", "" + newmsgs.size());
            notifyDataSetChanged();
        }
    }


}

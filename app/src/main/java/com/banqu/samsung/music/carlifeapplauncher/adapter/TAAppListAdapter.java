package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.banqu.samsung.music.R;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

public class TAAppListAdapter extends RecyclerView.Adapter<TAAppListAdapter.ViewHolder> {

    private int mode;
    private List<String> favoList;
    private List<AppInfo> appsList;
    private Context c;
    private SharedPreferences sharedPreferences;
    private int launcher_icon_size;
    private TouchAssistant taobj;

    public TAAppListAdapter(Context c, TouchAssistant taobj,int size) {
        //basic jobs
        this.taobj = taobj;
        this.c = c;
        this.appsList = new ArrayList<AppInfo>();
        this.favoList =  Common.get_favorite_list(c);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        this.launcher_icon_size =size;
        PackageManager pm = c.getPackageManager();
        for (String pkg : favoList) {
            AppInfo app = new AppInfo();
            try {
                ApplicationInfo temp_info = pm.getApplicationInfo(pkg, 0);
                app.label = temp_info.loadLabel(pm);
                app.packageName = temp_info.packageName;
                app.icon = temp_info.loadIcon(pm);
                appsList.add(app);
            } catch (Exception e) {

            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public View itemView;
        public View.OnClickListener vocl;

        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.ta_cell_img);
            vocl = this;
            this.itemView = itemView;
            itemView.setOnClickListener(vocl);
            itemView.setFocusable(false);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    taobj.resetTimer();
                    return false;
                }
            });
            itemView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    // Handles each of the expected events.
                    switch (dragEvent.getAction()) {

                        case DragEvent.ACTION_DRAG_STARTED:
                            // Determines if this View can accept the dragged data.
                            if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                // As an example of what your application might do, applies a blue color tint
                                // to the View to indicate that it can accept data.
//                                ()view.setColorFilter(Color.BLUE);
//                                view.setBackgroundColor(Color.BLUE);
                                // Invalidate the view to force a redraw in the new tint.
                                view.invalidate();
                                // Returns true to indicate that the View can accept the dragged data.
                                return true;
                            }
                            // Returns false to indicate that, during the current drag and drop operation,
                            // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                            return false;

                        case DragEvent.ACTION_DRAG_ENTERED:
                            // Applies a green tint to the View.
//                            ((ImageView)v).setColorFilter(Color.GREEN);
//                            view.setBackgroundColor(Color.GREEN);
                            // Invalidates the view to force a redraw in the new tint.
                            //view.invalidate();

                            // Returns true; the value is ignored.
                            return true;

                        case DragEvent.ACTION_DRAG_LOCATION:

                            // Ignore the event.
                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            // Resets the color tint to blue.
//                            view.setBackgroundColor(Color.BLUE);
                            // Invalidates the view to force a redraw in the new tint.
                            //view.invalidate();

                            // Returns true; the value is ignored.
                            return true;

                        case DragEvent.ACTION_DROP:

                            // Gets the item containing the dragged data.
                            ClipData.Item item = dragEvent.getClipData().getItemAt(0);

                            // Gets the text data from the item.
                            CharSequence dragData = item.getText();

                            // Displays a message containing the dragged data.
//                            Toast.makeText(c, "Dragged data is: from index " + dragData + " to index " + getAdapterPosition(), Toast.LENGTH_LONG).show();

                            // Turns off any color tints.
//                            view.setBackgroundColor(Color.TRANSPARENT);

                            switchLocation(Integer.valueOf(dragData.toString()), getAdapterPosition());
                            // Invalidates the view to force a redraw.
                            // view.invalidate();

                            // Returns true. DragEvent.getResult() will return true.
                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            // Turns off any color tinting.
//                            view.setBackgroundColor(Color.TRANSPARENT);

                            // Invalidates the view to force a redraw.
//                            view.invalidate();

                            // Does a getResult(), and displays what happened.
//                            if (dragEvent.getResult()) {
//                                Toast.makeText(c, "The drop was handled.", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(c, "The drop didn't work.", Toast.LENGTH_LONG).show();
//                            }
//                            notifyDataSetChanged();
                            // Returns true; the value is ignored.
                            return true;

                        // An unknown action type was received.
                        default:
                            Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.");
                            break;
                    }

                    return false;

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.i("LongClick", "LongClick");
                    ClipData.Item item = new ClipData.Item(getAdapterPosition() + "");
                    //ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
//                    ClipData dragData = new ClipData(
//                            (CharSequence) view.getTag(),
//                            new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN },
//                            item);
                    ClipData dragData = new ClipData(getAdapterPosition() + "",
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                            item);
                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(itemView);
                    view.startDragAndDrop(dragData,  // The data to be dragged
                            myShadow,  // The drag shadow builder
                            null,      // No need to use local data
                            0          // Flags (not currently used, set to 0)
                    );
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            taobj.resetTimer();
            int pos = getAdapterPosition();
            Intent launchIntent = c.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).packageName.toString());
            c.startActivity(launchIntent);
            //Toast.makeText(v.getContext(), appsList.get(pos).label.toString(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onBindViewHolder(TAAppListAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        //获取数据
        Drawable appIcon = appsList.get(i).icon;

        ImageView imageView = viewHolder.img;
        imageView.setImageDrawable(appIcon);
        LinearLayout.LayoutParams imglp = (LinearLayout.LayoutParams) imageView.getLayoutParams();


            imglp.height = launcher_icon_size;
            imglp.width = launcher_icon_size;


        imageView.setLayoutParams(imglp);
        imageView.setBackgroundColor(Color.WHITE);
        Common.setBgRadiusWithCutOut(imageView,(int)(launcher_icon_size*0.05),(int)(launcher_icon_size*0.35));

    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }


    @Override
    public TAAppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.ta_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    private ArrayList<String> get_favorite_list() {
        ArrayList<String> temp = new ArrayList<String>();
        try {
            ObjectInputStream oss = new ObjectInputStream(c.openFileInput("FullAppList"));
            temp.addAll((ArrayList<String>) oss.readObject());
            oss.close();
            return temp;
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    private void switchLocation(int targetA, int targetB) {
        ArrayList<String> temp = new ArrayList<String>();
        //read from storage
        try {
            ObjectInputStream oss = new ObjectInputStream(c.openFileInput("FullAppList"));
            temp.addAll((ArrayList<String>) oss.readObject());
            oss.close();
        } catch (Exception e) {

        }
        //do
        if (temp.size() > targetA && temp.size() > targetB && temp.size() >= 2) {
            String stringB = temp.get(targetB);
            temp.set(targetB, temp.get(targetA));
            temp.set(targetA, stringB);
            try {
                ObjectOutputStream oss = new ObjectOutputStream(c.openFileOutput("FullAppList", c.MODE_PRIVATE));
                oss.writeObject(temp);
            } catch (Exception e) {

            }
            favoList = get_favorite_list();
            appsList = new ArrayList<AppInfo>();
            PackageManager pm = c.getPackageManager();
            for (String pkg : favoList) {
                AppInfo app = new AppInfo();
                try {
                    ApplicationInfo temp_info = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
                    app.label = pm.getApplicationLabel(temp_info);
                    app.packageName = pkg;
                    app.icon = pm.getApplicationIcon(temp_info);
                    appsList.add(app);
                } catch (Exception e) {

                }
            }
            notifyItemChanged(targetA);
            notifyItemChanged(targetB);
        }
    }


    public static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable object.
        private Drawable shadow;
        private ImageView icon;

        // Constructor
        public MyDragShadowBuilder(View v) {
            // Stores the View parameter.
            super(v);

            icon = (ImageView) v.findViewById(R.id.ta_cell_img);
//            shadow = icon.getDrawable().;
            // Creates a draggable image that fills the Canvas provided by the system.
//            shadow = new ColorDrawable(Color.LTGRAY);
            shadow = icon.getDrawable().getConstantState().newDrawable();
        }

        // Defines a callback that sends the drag shadow dimensions and touch point
        // back to the system.
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {

            // Defines local variables
            int width, height;

            // Set the width of the shadow to half the width of the original View.
//            width =  getView().getWidth();
            width = icon.getHeight();
            // Set the height of the shadow to half the height of the original View.
//            height = getView().getHeight();
            height = icon.getHeight();

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the
            // same as the Canvas that the system provides. As a result, the drag shadow
            // fills the Canvas.
            shadow.setBounds(0, 0, width, height);

            // Set the size parameter's width and height values. These get back to the
            // system through the size parameter.
            size.set(width, height);

            // Set the touch point's position to be in the middle of the drag shadow.
            touch.set(width / 2, height / 2);
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system
        // constructs from the dimensions passed to onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {
            // Draw the ColorDrawable on the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }
}
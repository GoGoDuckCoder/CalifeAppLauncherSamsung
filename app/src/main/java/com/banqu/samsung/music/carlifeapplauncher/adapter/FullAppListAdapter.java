package com.banqu.samsung.music.carlifeapplauncher.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.banqu.samsung.music.R;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

public class FullAppListAdapter extends RecyclerView.Adapter<FullAppListAdapter.ViewHolder> {

    private int mode;
    private List<String> favoList;
    private List<AppInfo> appsList;
    private Context c;
    private SharedPreferences sharedPreferences;
    private int launcher_icon_size;
    private int launcher_font_size;
    private boolean showlabel;
    private int voh;

    public FullAppListAdapter(Context c, int mode, int voh) {
        //basic jobs
        this.voh = voh;
        this.c = c;
        this.mode = mode;
        this.appsList = new ArrayList<AppInfo>();
        this.favoList = Common.get_favorite_list(c);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        this.launcher_icon_size = Integer.parseInt(sharedPreferences.getString("launcher_icon_size", "120"));
        this.launcher_font_size = Integer.parseInt(sharedPreferences.getString("launcher_font_size", "30"));
        if (mode == FullAppListAdapterMode.mode_edit) {
            this.showlabel = true;
        } else {
            this.showlabel = sharedPreferences.getBoolean("showlabel", true);
        }

        //jobs per mode
        if (mode == FullAppListAdapterMode.mode_favorite || mode == FullAppListAdapterMode.ta_favorite || mode == FullAppListAdapterMode.mode_deeplink) {
            for (String pkg : favoList) {
                try {
                    AppInfo app = Common.loadAppInfo(c,pkg);
                    appsList.add(app);
                } catch (Exception e) {
//                    AppListAdapter.getInstance(c).remove_from_favorite_list(pkg);
                }
            }
        }
        if (mode == FullAppListAdapterMode.mode_fakestart) {
            favoList.clear();
            favoList.addAll(Common.get_adapt_list(c));

            for (String pkg : favoList) {
                try {
                    AppInfo app = Common.loadAppInfo(c,pkg);
                    appsList.add(app);
                } catch (Exception e) {
                }
            }
        }
        if (mode == FullAppListAdapterMode.mode_edit) {
            appsList.addAll(Common.getAllApps(c));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public ImageView img;
        public Switch favoswitch;
        public View itemView;
        public View.OnClickListener vocl;
        public RadioGroup rdog;

        //This is the subclass ViewHolder which simply
        //'holds the views' for us to show on each row
        public ViewHolder(View itemView) {
            super(itemView);
            //Finds the views from our row.xml
            textView = (TextView) itemView.findViewById(R.id.text);
            img = (ImageView) itemView.findViewById(R.id.img);
            favoswitch = (Switch) itemView.findViewById(R.id.isfavo);
            rdog = (RadioGroup) itemView.findViewById(R.id.fakestart_options);
            vocl = this;
            this.itemView = itemView;
            itemView.setFocusable(false);
            itemView.setOnClickListener(vocl);

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
                            Log.i("Asdasdasd", "onDrag: "+Integer.valueOf(dragData.toString())+" "+getAdapterPosition());
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
            if (mode == FullAppListAdapterMode.mode_favorite||mode==FullAppListAdapterMode.mode_deeplink) {
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

        }

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();
//

//
//            Intent i = new Intent("huawei.intent.action.hicar.MEDIA",null);
//            i.setPackage(appsList.get(pos).packageName.toString());
//            List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
//            ResolveInfo info = null;
//            for (ResolveInfo ri : allApps) {
//                info = ri;
//                break;
//            }

            if (mode == FullAppListAdapterMode.mode_deeplink) {
//                ((Activity)context).finish();
                FakeStart.StartUsingDeepLink(context,appsList.get(pos).packageName.toString());
            } else {
                FakeStart.Start(context, appsList.get(pos).packageName.toString());
            }
//            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appsList.get(pos).packageName.toString());
//            context.startActivity(launchIntent);
//            Intent launchIntent = new Intent("huawei.intent.action.hicar.MEDIA");
//            launchIntent.addFlags(0x10104000);
//
//            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
//            try {
//                context.startActivity(launchIntent);
//                Log.i("launch", "no exception ");
//            } catch (Exception e) {
//                Log.i("launch", "exception ");
//            }


//            launchIntent.setAction("samsung.intent.action.carlink.kit");
//            launchIntent.addCategory("vivo.intent.categoty.carlink.kit");
            //Toast.makeText(v.getContext(), appsList.get(pos).label.toString(), Toast.LENGTH_LONG).show();
        }

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(FullAppListAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        //获取数据
        String appLabel = appsList.get(i).label.toString();
        String appPackage = appsList.get(i).packageName.toString();
        Drawable appIcon = appsList.get(i).icon;
        //获取控件 设置数据
        TextView textView = viewHolder.textView;
        if (showlabel) {
            textView.setText(appLabel, TextView.BufferType.EDITABLE);
            Spannable sp = (Spannable) textView.getText();
            sp.setSpan(new AbsoluteSizeSpan(launcher_font_size), 0, textView.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //设置字体大小
            sp.setSpan(new ForegroundColorSpan(c.getColor(R.color.text_color)),0,textView.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(sp);

        } else {
            textView.setVisibility(View.GONE);
        }

        ImageView imageView = viewHolder.img;

//        BitmapDrawable bd = (BitmapDrawable) appIcon;
//        Bitmap smallBitmap = Bitmap.createBitmap( bd.getBitmap(),(int)(launcher_icon_size*0.05),(int)(launcher_icon_size*0.05),launcher_icon_size,launcher_icon_size);


//        imageView.setImageBitmap(smallBitmap);

        imageView.setImageDrawable(appIcon);
        LinearLayout.LayoutParams imglp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        imglp.height = launcher_icon_size;
        imglp.width = launcher_icon_size;
        imageView.setLayoutParams(imglp);
//        imageView.setBackgroundColor(Color.WHITE);
        Common.setBgRadiusWithCutOut(imageView, (int) (launcher_icon_size * 0.05), (int) (launcher_icon_size * 0.35));

        switch (mode) {
            case FullAppListAdapterMode.mode_favorite:
            case FullAppListAdapterMode.mode_deeplink:
            case FullAppListAdapterMode.mode_select:
                viewHolder.itemView.setOnClickListener(viewHolder.vocl);
                viewHolder.favoswitch.setVisibility(View.GONE);
                viewHolder.rdog.setVisibility(View.GONE);
                break;
            case FullAppListAdapterMode.mode_edit:
                viewHolder.itemView.setOnClickListener(null);
                viewHolder.favoswitch.setVisibility(View.VISIBLE);
                viewHolder.rdog.setVisibility(View.GONE);
                viewHolder.favoswitch.setOnCheckedChangeListener(null);
                if (!favoList.isEmpty() && favoList.contains(appPackage)) {
                    viewHolder.favoswitch.setChecked(true);
                } else {
                    viewHolder.favoswitch.setChecked(false);
                }
                viewHolder.favoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            Toast.makeText(c, "已添加" + appLabel, Toast.LENGTH_SHORT).show();
                            Common.add_to_favorite_list_new(c,appPackage);
                            favoList.add(appPackage);
                            notifyItemChanged(i);
                            //notifyDataSetChanged();
                        } else {
                            Toast.makeText(c, "已删除" + appLabel, Toast.LENGTH_SHORT).show();
                            Common.remove_from_favorite_list_new(c,appPackage);
                            favoList.remove(appPackage);
                            notifyItemChanged(i);
                            //notifyDataSetChanged();
                        }
                    }
                });
                break;
            case FullAppListAdapterMode.mode_fakestart:
                viewHolder.itemView.setOnClickListener(null);
                viewHolder.favoswitch.setVisibility(View.GONE);
                viewHolder.rdog.setOnCheckedChangeListener(null);
                Map<String, String> temp = new HashMap<>();
                //start
                try {
                    ObjectInputStream oss = new ObjectInputStream(c.openFileInput("FakeStartConfig"));
                    temp = (Map<String, String>) oss.readObject();
                    oss.close();
                } catch (Exception e) {

                }
                if (temp.containsKey(appPackage)) {
                    switch (temp.get(appPackage)) {
                        case "default":
                            ((RadioButton) viewHolder.rdog.findViewById(R.id.default_mode)).setChecked(true);
                            break;
                        case "vivo":
                            ((RadioButton) viewHolder.rdog.findViewById(R.id.vivo_mode)).setChecked(true);
                            break;
                        case "samsung":
                            ((RadioButton) viewHolder.rdog.findViewById(R.id.samsung_mode)).setChecked(true);
                            break;
                        case "huawei":
                            ((RadioButton) viewHolder.rdog.findViewById(R.id.huawei_mode)).setChecked(true);
                            break;
                        case "ucar":
                            ((RadioButton) viewHolder.rdog.findViewById(R.id.ucar_mode)).setChecked(true);
                            break;
                    }
                } else {
                    //default mode
                    ((RadioButton) viewHolder.rdog.findViewById(R.id.default_mode)).setChecked(true);
                }


                viewHolder.rdog.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        String mode = "default";
                        switch (checkedId) {
                            case R.id.default_mode:
                                mode = "default";
                                break;
                            case R.id.vivo_mode:
                                mode = "vivo";
                                break;
                            case R.id.samsung_mode:
                                mode = "samsung";
                                break;
                            case R.id.huawei_mode:
                                mode = "huawei";
                                break;
                            case R.id.ucar_mode:
                                mode = "ucar";
                                break;
                        }
                        change_fakestart_list(appPackage, mode);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }


    @Override
    public FullAppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //This is what adds the code we've written in here to our target view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (voh == 0) {
            View view = inflater.inflate(R.layout.row, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else {
            View view = inflater.inflate(R.layout.rowhor, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    private void switchLocation(int targetA, int targetB) {
            Common.switchLocation(c,targetA,targetB);

            favoList = Common.get_favorite_list(c);
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

    public static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable object.
        private Drawable shadow;
        private ImageView icon;

        // Constructor
        public MyDragShadowBuilder(View v) {
            // Stores the View parameter.
            super(v);

            icon = (ImageView) v.findViewById(R.id.img);
//            shadow = icon.getDrawable().;
            // Creates a draggable image that fills the Canvas provided by the system.
//            shadow = new ColorDrawable(Color.LTGRAY);
            shadow = icon.getDrawable().getConstantState().newDrawable();
//            shadow = ;
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


    ////

    private boolean change_fakestart_list(String pkgname, String mode) {
        Map<String, String> temp = new HashMap<>();
        //read from storage
        try {
            ObjectInputStream oss = new ObjectInputStream(c.openFileInput("FakeStartConfig"));
            temp = (Map<String, String>) oss.readObject();
            oss.close();
        } catch (Exception e) {

        }
        //do
        if (temp.containsKey(pkgname)) {
            temp.replace(pkgname, mode);
        } else {
            temp.put(pkgname, mode);
        }
        try {
            ObjectOutputStream oss = new ObjectOutputStream(c.openFileOutput("FakeStartConfig", c.MODE_PRIVATE));
            oss.writeObject(temp);
        } catch (Exception e) {
            return false;
        }
        //write
        return true;
    }


    private List<AppInfo> appsList_bak;
    private boolean search_mode = false;

    public void search(String s) {
        if (!search_mode) {
//            Log.i("SEARCH", "SEARCH MODE ON BACKUP");
            appsList_bak = new ArrayList<AppInfo>();
            appsList_bak.addAll(appsList);
            search_mode = true;
        }
        if (s.equals("")) {
//            Log.i("SEARCH", "SEARCH MODE OFF RETRIEVE");
            search_off();
            return;
        }
        ArrayList<AppInfo> searchresult = new ArrayList<AppInfo>();
        for (AppInfo info : appsList_bak) {
            if (info.label.toString().contains(s)) {
//                Log.i("SEARCH", "FOUND ADD:" + info.label);
                searchresult.add(info);
            }
        }

        appsList.clear();
        // notifyDataSetChanged();
        appsList.addAll(searchresult);
//        Log.i("SEARCH","FOUND RESULT:"+appsList.size());
//        this.notifyDataSetChanged();

    }

    public void search_off() {
        search_mode = false;
        appsList.clear();
        appsList.addAll(appsList_bak);
//        notifyDataSetChanged();
    }
//    private boolean remove_from_fakestart_list(String pkgname) {
//        ArrayList<String> temp = new ArrayList<String>();
//        //read
//        try {
//
//            ObjectInputStream oss = new ObjectInputStream(c.openFileInput("FullAppList"));
//            temp.addAll((ArrayList<String>) oss.readObject());
//            oss.close();
//        } catch (Exception e) {
//
//        }
//        //do
//        if (temp.contains(pkgname)) {
//            temp.remove(pkgname);
//            try {
//                ObjectOutputStream oss = new ObjectOutputStream(c.openFileOutput("FullAppList", c.MODE_PRIVATE));
//                oss.writeObject(temp);
//            } catch (Exception e) {
//                return false;
//            }
//        }
//        //write
//        return true;
//    }
}
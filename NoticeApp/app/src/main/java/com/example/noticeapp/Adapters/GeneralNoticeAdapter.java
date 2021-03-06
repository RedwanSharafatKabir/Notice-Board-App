package com.example.noticeapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeapp.ModelClasses.StoreGeneralNoticeData;
import com.example.noticeapp.NoticeAndNotification.ParticularNoticeActivity;
import com.example.noticeapp.R;

import java.util.ArrayList;

public class GeneralNoticeAdapter extends RecyclerView.Adapter<GeneralNoticeAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreGeneralNoticeData> storeGeneralNoticeData;

    public GeneralNoticeAdapter(Context c, ArrayList<StoreGeneralNoticeData> p) {
        context = c;
        storeGeneralNoticeData = p;
    }

    @NonNull
    @Override
    public GeneralNoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralNoticeAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.general_notice_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralNoticeAdapter.MyViewHolder holder, int position) {
        String departmentName = "General_Notice";
        String noticeTitle = storeGeneralNoticeData.get(position).getNoticeTitle();
        String noticeDate = storeGeneralNoticeData.get(position).getNoticeDate();
        String noticeDay = storeGeneralNoticeData.get(position).getNoticeDay();
        String noticeTime = storeGeneralNoticeData.get(position).getNoticeTime();
        String noticeDetails = storeGeneralNoticeData.get(position).getNoticeDetails();

        holder.textView.setText(noticeTitle+"!");
        holder.textView1.setText(" "+noticeDate+", "+noticeDay);
        holder.textView2.setText(" "+noticeTime);

        holder.nextToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(activity, ParticularNoticeActivity.class);
                intent.putExtra("noticeTitleKey", noticeTitle);
                intent.putExtra("noticeDateKey", noticeDate);
                intent.putExtra("noticeDayKey", noticeDay);
                intent.putExtra("noticeTimeKey", noticeTime);
                intent.putExtra("noticeDetailsKey", noticeDetails);
                intent.putExtra("dept_Key", departmentName);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeGeneralNoticeData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView;
        ImageView nextToDetails;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.titleId);
            textView1 = itemView.findViewById(R.id.dateId);
            textView2 = itemView.findViewById(R.id.timeId);

            nextToDetails = itemView.findViewById(R.id.nextToDetailsGeneralId);
        }
    }
}

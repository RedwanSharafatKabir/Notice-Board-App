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

import com.example.noticeapp.ModelClasses.StoreDepartmentNoticeData;
import com.example.noticeapp.NoticeAndNotification.ParticularNoticeActivity;
import com.example.noticeapp.R;
import java.util.ArrayList;

public class DepartmentNoticeAdapter extends RecyclerView.Adapter<DepartmentNoticeAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreDepartmentNoticeData> storeDepartmentNoticeData;

    public DepartmentNoticeAdapter(Context c, ArrayList<StoreDepartmentNoticeData> p) {
        context = c;
        storeDepartmentNoticeData = p;
    }

    @NonNull
    @Override
    public DepartmentNoticeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepartmentNoticeAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.department_notice_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentNoticeAdapter.MyViewHolder holder, int position) {
        String departmentName = storeDepartmentNoticeData.get(position).getDepartmentName();
        String noticeTitle = storeDepartmentNoticeData.get(position).getNoticeTitle();
        String noticeDate = storeDepartmentNoticeData.get(position).getNoticeDate();
        String noticeDay = storeDepartmentNoticeData.get(position).getNoticeDay();
        String noticeTime = storeDepartmentNoticeData.get(position).getNoticeTime();
        String noticeDetails = storeDepartmentNoticeData.get(position).getNoticeDetails();

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
        return storeDepartmentNoticeData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView;
        ImageView nextToDetails;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.titleDeptId);
            textView1 = itemView.findViewById(R.id.dateDeptId);
            textView2 = itemView.findViewById(R.id.timeDeptId);

            nextToDetails = itemView.findViewById(R.id.nextToDetailsId);
        }
    }
}

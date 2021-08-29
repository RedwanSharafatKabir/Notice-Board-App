package com.example.noticeapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeapp.DepartmentsNotice.ParticularNoticePage;
import com.example.noticeapp.ModelClasses.StoreGeneralNoticeData;
import com.example.noticeapp.R;

import java.util.ArrayList;

public class GeneralNoticeAdapter extends RecyclerView.Adapter<GeneralNoticeAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreGeneralNoticeData> storeGeneralNoticeData;
//    DatabaseReference databaseReference;

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
        String noticeDate = storeGeneralNoticeData.get(position).getNoticeDate();
        String noticeDay = storeGeneralNoticeData.get(position).getNoticeDay();
        String noticeTime = storeGeneralNoticeData.get(position).getNoticeTime();
        String noticeDetails = storeGeneralNoticeData.get(position).getNoticeDetails();

        holder.textView1.setText(" "+noticeDate+", "+noticeDay);
        holder.textView2.setText(" "+noticeTime);
        holder.textView3.setText(" "+noticeDetails);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(activity, ParticularNoticePage.class);

                intent.putExtra("noticeDateKey", noticeDate);
                intent.putExtra("noticeDayKey", noticeDay);
                intent.putExtra("noticeTimeKey", noticeTime);
                intent.putExtra("noticeDetailsKey", noticeDetails);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeGeneralNoticeData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

//            databaseReference = FirebaseDatabase.getInstance().getReference("Place Images");

            textView1 = itemView.findViewById(R.id.dateId);
            textView2 = itemView.findViewById(R.id.timeId);
            textView3 = itemView.findViewById(R.id.detailsId);
        }
    }
}

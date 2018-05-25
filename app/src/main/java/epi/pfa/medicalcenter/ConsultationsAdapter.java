package epi.pfa.medicalcenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by squall on 18/11/2017.
 */

public class ConsultationsAdapter extends RecyclerView.Adapter<ConsultationsAdapter.ViewHolder> {

    private ArrayList<Consultation> mList;
    private Context mContext;
    private String token;

    public ConsultationsAdapter(ArrayList<Consultation> mList, Context context, String token){
        this.mList = mList;
        this.mContext = context;
        this.token = token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consultation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsultationsAdapter.ViewHolder holder, final int position) {
        final String date = mList.get(position).getDate().substring(0, Math.min(mList.get(position).getDate().length(), 10));
        final String time = mList.get(position).getTime().substring(11,16);
        holder.date.setText(date + " "+ time);
        holder.gotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ShowConsultationActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("patient",String.valueOf(mList.get(position).getPatient()));
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("description",mList.get(position).getDescription());
                intent.putExtra("token",token);
                mContext.startActivity(intent);

            }
        });

    }


        @Override
        public int getItemCount() {
            return mList.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public ImageView gotoBtn;
        public ViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.serviceName);
            gotoBtn = (ImageView) view.findViewById(R.id.tool);
        }
    }
}

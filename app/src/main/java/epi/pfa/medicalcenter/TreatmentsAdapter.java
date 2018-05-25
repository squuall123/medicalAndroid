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

public class TreatmentsAdapter extends RecyclerView.Adapter<TreatmentsAdapter.ViewHolder> {

    private ArrayList<Treatment> mList;
    private Context mContext;
    private String token;

    public TreatmentsAdapter(ArrayList<Treatment> mList, Context context, String token){
        this.mList = mList;
        this.mContext = context;
        this.token = token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TreatmentsAdapter.ViewHolder holder, final int position) {
        holder.date.setText(mList.get(position).getTitre());
        holder.gotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, howTreatmentActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("token",token);
                intent.putExtra("titre",mList.get(position).getTitre());
                intent.putExtra("contenu",mList.get(position).getContenu());
                intent.putExtra("patientId",mList.get(position).getPatientId());
                intent.putExtra("doctorId",mList.get(position).getDoctorid());
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

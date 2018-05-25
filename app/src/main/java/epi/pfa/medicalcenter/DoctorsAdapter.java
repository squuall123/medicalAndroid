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

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private ArrayList<Doctor> mList;
    private Context mContext;
    private String token;
    //private Intent intent;

    public DoctorsAdapter(ArrayList<Doctor> mList, Context context, String token){
        this.mList = mList;
        mContext = context;
        this.token = token;
        //intent = new Intent(mContext,ServiceActivity.class);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorsAdapter.ViewHolder holder, final int position) {

        holder.name.setText(mList.get(position).getName());
        holder.gotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("DEBUG", String.valueOf(mList.get(position).getId()));
                Intent intent = new Intent(mContext, DoctorActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("id",String.valueOf(mList.get(position).getId()));
                intent.putExtra("name", mList.get(position).getName());
                intent.putExtra("email", mList.get(position).getEmail());
                intent.putExtra("phone",mList.get(position).getPhone());
                intent.putExtra("specialite", mList.get(position).getSpecialite());
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
        public TextView name;
        public ImageView gotoBtn;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.doctorName);
            gotoBtn = (ImageView) view.findViewById(R.id.tool);
        }
    }
}

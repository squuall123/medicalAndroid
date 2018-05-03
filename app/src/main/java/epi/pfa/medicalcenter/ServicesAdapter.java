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

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private ArrayList<Service> mList;
    private Context mContext;
    private String token;
    //private Intent intent;

    public ServicesAdapter(ArrayList<Service> mList, Context context, String token){
        this.mList = mList;
        mContext = context;
        this.token = token;
        //intent = new Intent(mContext,ServiceActivity.class);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServicesAdapter.ViewHolder holder, final int position) {

        holder.name.setText(mList.get(position).getName());
        holder.gotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ServiceActivity.class);
                //intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("id",String.valueOf(mList.get(position).getId()));
                intent.putExtra("nom", mList.get(position).getName());
                intent.putExtra("description", mList.get(position).getDescription());
                intent.putExtra("num_tel",mList.get(position).getPhone());
                intent.putExtra("token",token);
                mContext.startActivity(intent);

                //Log.i("DEBUG", String.valueOf(mList.get(position).getId()));
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
            name = (TextView) view.findViewById(R.id.serviceName);
            gotoBtn = (ImageView) view.findViewById(R.id.tool);
        }
    }
}

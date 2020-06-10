package a24.mohit.expensemanagement;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mohit on 11/27/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    ArrayList<DataProvider> dataProviders = new ArrayList<>();
    public DataAdapter(ArrayList<DataProvider> dataProviders1){
        this.dataProviders = dataProviders1;
    }
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout,parent,false);
        DataViewHolder dataViewHolder = new DataViewHolder(view);
        return dataViewHolder;

    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        DataProvider dataProvider = dataProviders.get(position);
        holder.tvmainexpense.setText(dataProvider.getMainexpense());
        holder.tvsubexpense.setText(dataProvider.getSubexpense());
        holder.tvlimit.setText(dataProvider.getLimit());



    }

    @Override
    public int getItemCount() {
        return dataProviders.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder{

        public CardView mCardView;
           TextView tvmainexpense,tvsubexpense,tvlimit;
        public DataViewHolder(View itemView) {
            super(itemView);

            tvmainexpense = (TextView) itemView.findViewById(R.id.tvmainexpense);
            tvsubexpense = (TextView) itemView.findViewById(R.id.tvsubexpense);
            tvlimit = (TextView) itemView.findViewById(R.id.tvlimit);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                }
            });
        }
    }
}

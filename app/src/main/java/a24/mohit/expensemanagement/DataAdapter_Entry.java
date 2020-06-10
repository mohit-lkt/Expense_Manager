package a24.mohit.expensemanagement;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by mohit on 11/27/2017.
 */

public class DataAdapter_Entry extends RecyclerView.Adapter<DataAdapter_Entry.DataViewHolder> {
    ArrayList<DataProvider_Entry> dataProviders = new ArrayList<>();
    public DataAdapter_Entry(ArrayList<DataProvider_Entry> dataProviders1){
        this.dataProviders = dataProviders1;
    }
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_entry,parent,false);
        DataViewHolder dataViewHolder = new DataViewHolder(view);
        return dataViewHolder;

    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {
        DataProvider_Entry dataProvider = dataProviders.get(position);
        holder.tvmainExpense.setText(dataProvider.getMainExpense());
        holder.tvsubExpense.setText(dataProvider.getSubExpense());
        holder.tvreceiptNo.setText(dataProvider.getReceiptNo());
        holder.tvDate.setText(dataProvider.getDate_entry());
        holder.tvfromHead.setText(dataProvider.getFromHead());
        holder.tvAmount.setText(dataProvider.getAmount());
        holder.tvRemark.setText(dataProvider.getRemark());


        final String amount = holder.tvAmount.getText().toString();
        final String remark = holder.tvRemark.getText().toString();

        String mainExpense = holder.tvmainExpense.getText().toString();


        if(mainExpense.equals("-")){
            holder.btnfill.setVisibility(VISIBLE);
        }


       holder.btnfill.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(holder.btnfill.getContext(),ExpenseEntry.class);
               i.putExtra("amount",amount);
               i.putExtra("remark",remark);

               holder.btnfill.getContext().startActivity(i);
           }
       });

    }

    @Override
    public int getItemCount() {
        return dataProviders.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder{

        public CardView mCardView;
           TextView tvreceiptNo,tvDate,tvfromHead,tvmainExpense,tvsubExpense,tvAmount,tvRemark;
           Button btnfill;
        public DataViewHolder(View itemView) {
            super(itemView);

            tvmainExpense = (TextView) itemView.findViewById(R.id.tvmainExpense2);
            tvsubExpense = (TextView) itemView.findViewById(R.id.tvsubExpense2);
            tvreceiptNo = (TextView) itemView.findViewById(R.id.tvReceiptNo_entry2);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate2);
            tvfromHead = (TextView) itemView.findViewById(R.id.tvfromHead2);
            tvAmount = (TextView) itemView.findViewById(R.id.tvamount2);
            tvRemark = (TextView) itemView.findViewById(R.id.tvremark2);
            btnfill = (Button)itemView.findViewById(R.id.btnfill);


        }
    }
}

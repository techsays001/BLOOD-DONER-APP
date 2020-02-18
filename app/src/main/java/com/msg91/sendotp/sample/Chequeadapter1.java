package com.msg91.sendotp.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Chequeadapter1 extends RecyclerView.Adapter<Chequeadapter1.ProductViewHolder> {
    Intent i;
    SharedPreferences sh;
    EditText empid;
    private Context mCtx;
    private List<Cheque1> productList;

    public Chequeadapter1(Context mCtx, List<Cheque1> productList) {
        sh = Objects.requireNonNull(mCtx).getSharedPreferences("data", MODE_PRIVATE);
        this.mCtx = mCtx;
        this.productList = productList;
        // sh=mCtx.getSharedPreferences("Official1",MODE_PRIVATE);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_allbloodreq, null);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final Cheque1 cheque = productList.get(position);

        holder.name.setText(cheque.getUser1());
        holder.blood.setText(cheque.getUser2());
        holder.date.setText(cheque.getUser3());
        holder.city.setText(cheque.getUser4());
        holder.phone.setText(cheque.getUser5());



holder.call.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {



        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +cheque.getUser5()));
        mCtx.startActivity(intent);


    }
});

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder {


        TextView name,blood,date,city,phone,call;


        public ProductViewHolder(View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.rqname);
          blood = itemView.findViewById(R.id.rqblood);
           phone= itemView.findViewById(R.id.rqdate);
            date = itemView.findViewById(R.id.rqcity);
          city = itemView.findViewById(R.id.rqph);

      call = itemView.findViewById(R.id.rqcall);


        }


    }

//    public void filterList1(ArrayList<Cheque> filteredList1) {
//        productList = filteredList1;
//        notifyDataSetChanged();
//    }

}

package com.msg91.sendotp.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Chequeadapter3 extends RecyclerView.Adapter<Chequeadapter3.ProductViewHolder> {
    Intent i;
    SharedPreferences sh;
    EditText empid;
    private Context mCtx;
    private List<Cheque3> productList;

    public Chequeadapter3(Context mCtx, List<Cheque3> productList) {
        sh = Objects.requireNonNull(mCtx).getSharedPreferences("data", MODE_PRIVATE);
        this.mCtx = mCtx;
        this.productList = productList;
        // sh=mCtx.getSharedPreferences("Official1",MODE_PRIVATE);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_own, null);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final Cheque3 cheque = productList.get(position);

        holder.name.setText(cheque.getUser1());
        holder.blood.setText(cheque.getUser2());
        holder.date.setText(cheque.getUser3());
        holder.city.setText(cheque.getUser4());


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder {


        TextView name,blood,date,city;
Button rr;
        ImageView imageView;


        public ProductViewHolder(View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.onname);
          blood = itemView.findViewById(R.id.onblood);
            date = itemView.findViewById(R.id.ondate);
          city = itemView.findViewById(R.id.oncity);



        }


    }

//    public void filterList1(ArrayList<Cheque> filteredList1) {
//        productList = filteredList1;
//        notifyDataSetChanged();
//    }

}

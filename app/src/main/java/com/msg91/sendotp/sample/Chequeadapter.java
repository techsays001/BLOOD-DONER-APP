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

public class Chequeadapter extends RecyclerView.Adapter<Chequeadapter.ProductViewHolder> {
    Intent i;
    SharedPreferences sh;
    EditText empid;
    private Context mCtx;
    private List<Cheque> productList;

    public Chequeadapter(Context mCtx, List<Cheque> productList) {
        sh = Objects.requireNonNull(mCtx).getSharedPreferences("data", MODE_PRIVATE);
        this.mCtx = mCtx;
        this.productList = productList;
        // sh=mCtx.getSharedPreferences("Official1",MODE_PRIVATE);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_c1, null);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final Cheque cheque = productList.get(position);

        Picasso.get().load(cheque.getUser1()).into(holder.image);
        holder.blood.setText(cheque.getUser2());
        holder.phone.setText(cheque.getUser3());
        holder.name.setText(cheque.getUser4());




holder.phonecall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {



        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +cheque.getUser3()));
        mCtx.startActivity(intent);


    }
});

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }



    class ProductViewHolder extends RecyclerView.ViewHolder {


        TextView blood ,name,phone,phonecall;
        ImageView image;


        public ProductViewHolder(View itemView) {
            super(itemView);


          blood = itemView.findViewById(R.id.bloodblood);
            name= itemView.findViewById(R.id.bloodnames);
           phone= itemView.findViewById(R.id.bloodphone);
            image = itemView.findViewById(R.id.bloddimag1);
           phonecall = itemView.findViewById(R.id.pchonecall);



        }


    }

    public void filterList1(ArrayList<Cheque> filteredList1) {
        productList = filteredList1;
        notifyDataSetChanged();
    }

}

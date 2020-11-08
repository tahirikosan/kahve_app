package com.tahir.kahveapp.ui.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList, Context context){
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {

        Order currentItem = orderList.get(position);
        holder.tvItemName.setText(currentItem.getOrderName());
        holder.tvItemPrice.setText(currentItem.getOrderPrice() + " ₺");
        Picasso.get().load(currentItem.getOrderImageUrl()).fit().centerCrop().into(holder.ivItemPicture);
        
        if(currentItem.getOrderStatus().equals("prepare")){
            holder.ivOrderStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_prepare));
        }else {
            holder.ivOrderStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_ready));
        }

    

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    class OrderHolder extends RecyclerView.ViewHolder{
        private ImageView ivItemPicture;
        private TextView tvItemName;
        private TextView tvItemPrice;
        private ImageView ivOrderStatus;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            ivItemPicture = itemView.findViewById(R.id.iv_picture);
            tvItemName = itemView.findViewById(R.id.tv_name);
            tvItemPrice = itemView.findViewById(R.id.tv_price);
            ivOrderStatus = itemView.findViewById(R.id.iv_order_status);

        }
    }


}


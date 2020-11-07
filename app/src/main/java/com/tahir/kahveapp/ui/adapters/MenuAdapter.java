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
import com.tahir.kahveapp.data.models.Menu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private OnItemClickListener mListener;
    private List<Menu> menuList;

    public MenuAdapter(List<Menu> menuList){
        this.menuList = menuList;
    }


    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {

        Menu currentItem = menuList.get(position);
        holder.tvItemName.setText(currentItem.getItemName());
        holder.tvItemPrice.setText(currentItem.getItemPrice() + " TL");

        Picasso.get().load(currentItem.getItemImageUrl()).fit().centerCrop().into(holder.ivItemPicture);

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }


    class MenuHolder extends RecyclerView.ViewHolder{
        private ImageView ivItemPicture;
        private TextView tvItemName;
        private TextView tvItemPrice;
        private CardView cvItem;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);

            ivItemPicture = itemView.findViewById(R.id.iv_picture);
            tvItemName = itemView.findViewById(R.id.tv_name);
            tvItemPrice = itemView.findViewById(R.id.tv_price);
            cvItem = itemView.findViewById(R.id.cv_item);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onClickMenuItem(position, cvItem);
                        }
                    }
                }
            });

        }
    }




    public interface OnItemClickListener{
        void onClickMenuItem(int position, CardView cvItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}


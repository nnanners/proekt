package com.example.proekt;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


class pAdapter extends RecyclerView.Adapter<pAdapter.parkViewHolder> {

    class parkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView parkName;
        public TextView parkCity;
        public ImageView parkPic;
        public Button greenbtn;
        public Button redbtn;

        public parkViewHolder(final View itemView) {
            super(itemView);
            parkName = (TextView) itemView.findViewById(R.id.parkname);
            parkCity = (TextView) itemView.findViewById(R.id.parkinfo1);
            parkPic = (ImageView) itemView.findViewById(R.id.parkpicture);
            greenbtn = (Button) itemView.findViewById(R.id.greenbtn);
            redbtn = (Button) itemView.findViewById(R.id.redbtn);
            greenbtn.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), ReservationConfirmation.class);
                i.putExtra("city", city);
                i.putExtra("date", date);
                i.putExtra("hour", hour);
                i.putExtra("user", user);
                i.putExtra("park", parkName.getText());

                v.getContext().startActivity(i);

            });
            redbtn = (Button) itemView.findViewById(R.id.redbtn);
            redbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "број на зафатени места", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }


    DBHelper mDB;
    private int parkrowlayout;
    private Context parkContext;
    pAdapter.RecyclerViewClickListener listener;
    String city;
    String date;
    String hour;
    String user;
    int free;

    public pAdapter(Context parkContext, DBHelper db,  int rowLayout, pAdapter.RecyclerViewClickListener listener, String city, String date, String hour, String user){
        this.parkrowlayout = rowLayout;
        this.parkContext = parkContext;
        this.listener = listener;
        mDB = db;
        this.city = city;
        this.date = date;
        this.hour = hour;
        this.user = user;

    }

    @NonNull
    @Override
    public pAdapter.parkViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(parkrowlayout, parent, false);
        return new parkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(parkViewHolder viewHolder,int position) {


        Park current = mDB.querypark(position);

        if(current.getParkCity().equals(city)) {
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            viewHolder.parkName.setText(current.getParkName());


            int red = mDB.numberResAtDateTime(date, hour, String.valueOf(viewHolder.parkName.getText()));
            int green = current.getParkSpaces();
            viewHolder.greenbtn.setText(String.valueOf(green-red));
            viewHolder.redbtn.setText(String.valueOf(red));

        }
        else {
            viewHolder.itemView.setVisibility(View.GONE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }


        viewHolder.parkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                Toast.makeText(parkContext, tv.getText(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    @Override
    public int getItemCount(){
        return (int) mDB.countpark();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}

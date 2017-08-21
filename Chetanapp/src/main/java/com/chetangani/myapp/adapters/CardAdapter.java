package com.chetangani.myapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.fragments.cards.AddCardFragment;
import com.chetangani.myapp.fragments.cards.AllCards;
import com.chetangani.myapp.fragments.cards.CardDetails;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;

/**
 * Created by Chetan Gani on 4/21/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    Context context;
    ArrayList<CardDetails> arrayList = new ArrayList<>();
    FunctionCalls functionCalls = new FunctionCalls();
    AllCards cardfragment;
    Database database;

    public CardAdapter(Context context, ArrayList<CardDetails> arrayList, AllCards addCardFragment, Database database) {
        this.context = context;
        this.arrayList = arrayList;
        this.cardfragment = addCardFragment;
        this.database = database;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allcardsview, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardDetails cardDetails = arrayList.get(position);
        String card_number = cardDetails.getCard_number();
        holder.tv_cardnumber.setText(functionCalls.showcardnumber(card_number));
        holder.tv_cardexpiry.setText(cardDetails.getCard_expiry());
        holder.tv_cardcvv.setText(cardDetails.getCard_cvv());
        holder.tv_cardname.setText(cardDetails.getCard_name());
        byte[] decodedString = Base64.decode(cardDetails.getBank_image(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.im_banklogo.setImageBitmap(decodedByte);
        String digit1 = card_number.toString().substring(0,1);
        String digit2 = card_number.toString().substring(0,2);
        if (digit1.equals("4")) {
            holder.im_cardlogo.setImageResource(R.drawable.visacard_logo);
        }
        else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0) {
            holder.im_cardlogo.setImageResource(R.drawable.mastercard_logo);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener,
            MenuItem.OnMenuItemClickListener {
        TextView tv_cardnumber, tv_cardexpiry, tv_cardcvv, tv_cardname;
        ImageView im_banklogo, im_cardlogo;

        public CardViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            tv_cardnumber = (TextView) itemView.findViewById(R.id.cv_cardnumber);
            tv_cardexpiry = (TextView) itemView.findViewById(R.id.cv_expiry);
            tv_cardcvv = (TextView) itemView.findViewById(R.id.cv_cvv);
            tv_cardname = (TextView) itemView.findViewById(R.id.cv_cardname);
            im_banklogo = (ImageView) itemView.findViewById(R.id.cv_banklogo);
            im_cardlogo = (ImageView) itemView.findViewById(R.id.cv_cardlogo);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            CardDetails cardDetails = arrayList.get(pos);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int pos = getAdapterPosition();
            CardDetails cardDetails = arrayList.get(pos);
            switch (item.getItemId()) {
                case 1:
                    AddCardFragment fragment = new AddCardFragment();
                    Bundle bundle = new Bundle();
                    FragmentTransaction fragmentTransaction = cardfragment.getActivity().getSupportFragmentManager().beginTransaction();
                    bundle.putString("id", cardDetails.getCard_id());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container_main, fragment).addToBackStack(null).commit();
                    break;

                case 2:
                    Cursor delete = database.deletecarddetails(cardDetails.getCard_id());
                    delete.moveToNext();
                    arrayList.remove(pos);
                    notifyItemRemoved(pos);
                    break;
            }
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(menu.NONE,2,2,"Delete");
            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
    }
}

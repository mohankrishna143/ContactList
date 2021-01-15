package com.android.mycontactlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;

import jagerfield.mobilecontactslibrary.Contact.Contact;
import jagerfield.mobilecontactslibrary.ElementContainers.NumberContainer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private Context context;
    private final ArrayList<Contact> noteList;

    public ContactListAdapter(Context context, ArrayList<Contact> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        Contact value = noteList.get(position);
        holder.tv_name.setText(value.getFirstName() + " " + value.getLastName());
        if(value.getType()!=null){
            holder.tvType.setVisibility(View.VISIBLE);
        }else{
            holder.tvType.setVisibility(View.GONE);
        }
        if(value!=null&&value.getNumbers().size()>0) {
            LinkedList<NumberContainer> numberList = value.getNumbers();
            holder.tv_number.setText(numberList.get(0).elementValue());
            holder.tv_number.setVisibility(View.VISIBLE);
        }else{
            holder.tv_number.setVisibility(View.GONE);
        }

        if(value!=null&&value.getEmails().size()>0) {
            holder.tv_email.setText(value.getEmails().get(0).getEmail());
            holder.tv_email.setVisibility(View.VISIBLE);
        }else{
            holder.tv_email.setVisibility(View.GONE);
        }
        holder.tvType.setText(value.getType());
        String imageUri = value.getPhotoUri();
        Glide.with(context)
                .load(imageUri)
                .error(R.drawable.person)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.iv_contact_Image);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView tv_name;
        public final TextView tvType,tv_number,tv_email;
        ImageView iv_contact_Image;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tvType = (TextView) view.findViewById(R.id.tv_type);
            tv_number= (TextView) view.findViewById(R.id.tv_no);
            tv_email= (TextView) view.findViewById(R.id.tv_email);
            iv_contact_Image= (ImageView) view.findViewById(R.id.iv_contact_Image);
        }
    }
}

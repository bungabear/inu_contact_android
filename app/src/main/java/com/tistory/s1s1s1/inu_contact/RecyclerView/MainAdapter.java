//package com.tistory.s1s1s1.inu_contact.RecyclerView;
//
//import android.content.Context;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.tistory.s1s1s1.inu_contact.Contact;
//import com.tistory.s1s1s1.inu_contact.MainActivity;
//import com.tistory.s1s1s1.inu_contact.R;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2016-12-24.
// */
//
//public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
//    Context mContext;
//    ArrayList<String> parts;
//
//    public MainAdapter(Context c, ArrayList<String> parts) {
//        this.mContext = c;
//        this.parts = parts;
//    }
//
//    @Override
//    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, null);
//        MainAdapter.ViewHolder viewHolder = new MainAdapter.ViewHolder(itemLayoutView);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.main_item_tv.setText(parts.get(position));
//        holder.main_item_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, holder.main_item_tv.getText(), Toast.LENGTH_SHORT).show();
//                if(MainActivity.actionbar_et_search.isFocused()){
//                    MainActivity.actionbar_et_search.clearFocus();
//                }
//
//                InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);
//
//                ArrayList<Contact> contacts = MainActivity.dbHelper.getContact(holder.main_item_tv.getText().toString());
//                ContactAdapter contactAdapter = new ContactAdapter(mContext, contacts);
//                String part = parts.get(position);
//                if(part.length()>=13){
//                    part = part.substring(0, 12)+"...";
//                }
//                MainActivity.actionbar_tv_title.setText(part);
//                MainActivity.main_rv.removeAllViews();
//                MainActivity.main_rv.setAdapter(contactAdapter);
//                MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//                MainActivity.rv_level=1;
//            }
//        });
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView main_item_tv;
//        public RelativeLayout main_item_rl;
//
//        public ViewHolder(View itemLayoutView) {
//            super(itemLayoutView);
//            main_item_tv = (TextView) itemLayoutView.findViewById(R.id.main_item_tv);
//            main_item_rl = (RelativeLayout) itemLayoutView.findViewById(R.id.main_item_rl);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return parts.size();
//    }
//}
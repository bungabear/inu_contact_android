package com.tistory.s1s1s1.inu_contact.RecyclerView;

/**
 * Created by Administrator on 2016-12-24.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tistory.s1s1s1.inu_contact.Contact;
import com.tistory.s1s1s1.inu_contact.MainActivity;
import com.tistory.s1s1s1.inu_contact.R;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Contact> Contacts;

    public ContactAdapter(Context c, ArrayList<Contact> Contacts) {
        this.mContext = c;
        this.Contacts = Contacts;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_new, null);
        ContactAdapter.ViewHolder viewHolder = new ContactAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contact Contact = Contacts.get(position);
        if(Contact.getDpart().equals("") && Contact.getName().equals("")){
            //부서
            holder.contact_item_tv_dpart.setVisibility(View.GONE);
            holder.contact_item_tv_name.setText(Contact.getPart());
            holder.contact_item_tv_number.setVisibility(View.GONE);
//            holder.contact_item_tv_email.setVisibility(View.INVISIBLE);
            holder.contact_item_iv_call.setVisibility(GONE);
            holder.contact_item_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.actionbar_et_search.isFocused()){
                        MainActivity.actionbar_et_search.clearFocus();
                    }

                    String part = holder.contact_item_tv_name.getText().toString();

                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);

                    ArrayList<Contact> Contacts = MainActivity.dbHelper.getContact(part);
                    ContactAdapter ContactAdapter = new ContactAdapter(mContext, Contacts);
                    if(part.length()>=13){
                        part = part.substring(0, 12)+"...";
                    }
                    MainActivity.actionbar_tv_title.setText(part);
                    MainActivity.main_rv.removeAllViews();
                    MainActivity.main_rv.setAdapter(ContactAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                    MainActivity.rv_level=1;
//                    MainActivity.actionbar_et_search.setText("");
                }
            });
        } else {
            //사람
            holder.contact_item_tv_dpart.setVisibility(View.VISIBLE);
            holder.contact_item_tv_number.setVisibility(View.VISIBLE);
//            holder.contact_item_tv_email.setVisibility(View.VISIBLE);
            holder.contact_item_iv_call.setVisibility(View.VISIBLE);
            holder.contact_item_tv_dpart.setText(Contact.getDpart() + " / " + Contact.getPosition());
            holder.contact_item_tv_name.setText(Contact.getName());
            holder.contact_item_tv_number.setText(Contact.getPhone());
//            holder.contact_item_tv_email.setText(Contact.getEmail());
            if (Contact.getPhone().trim().length() <= 1) {
                holder.contact_item_iv_call.setVisibility(GONE);
//                holder.contact_item_iv_add.setVisibility(GONE);
            }
//            holder.contact_item_iv_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String name = Contact.getName();
//                    String number = Contact.getPhone();
//                    String email = Contact.getEmail();
//                    String part = Contact.getPart();
//                    String dpart = Contact.getDpart();
//
//                    ArrayList <ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//
//                    ops.add(ContentProviderOperation.newInsert(
//                            ContactsContact.RawContacts.CONTENT_URI)
//                            .withValue(ContactsContact.RawContacts.ACCOUNT_TYPE, null)
//                            .withValue(ContactsContact.RawContacts.ACCOUNT_NAME, null)
//                            .build());
//
//                    if(name!=null){
//
//                    }
//                }
//            });
            holder.contact_item_iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(mContext, holder.contact_item_tv_number.getText(), Toast.LENGTH_SHORT).show();

                    if (MainActivity.actionbar_et_search.isFocused()) {
                        MainActivity.actionbar_et_search.clearFocus();
                    }

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.mContext2);
                    dialog.setTitle("전화걸기").setMessage(Contact.getPart() + " / " + Contact.getName() + "(" + Contact.getPhone() + ")로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE);

                                    if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                        //권한 없음
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.contact_item_tv_number.getText()));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        try {
                                            mContext.startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //권한 있음
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.contact_item_tv_number.getText()));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        try {
                                            mContext.startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.BLACK);
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.BLACK);


                }
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contact_item_tv_dpart, contact_item_tv_name, contact_item_tv_number;
//        public TextView contact_item_tv_email;
        public ImageView contact_item_iv_call;
        public RelativeLayout contact_item_rl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            contact_item_iv_call = (ImageView) itemLayoutView.findViewById(R.id.contact_item_iv_call);
            contact_item_tv_name = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_name);
            contact_item_tv_number = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_number);
            contact_item_tv_dpart = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_dpart);
//            contact_item_tv_email = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_email);
            contact_item_rl = (RelativeLayout) itemLayoutView.findViewById(R.id.contact_item_rl);
        }
    }

    @Override
    public int getItemCount() {
        return Contacts.size();
    }
}
package com.tistory.s1s1s1.inu_contract.RecyclerView;

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
import android.widget.Toast;

import com.tistory.s1s1s1.inu_contract.Contract;
import com.tistory.s1s1s1.inu_contract.MainActivity;
import com.tistory.s1s1s1.inu_contract.R;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Contract> contracts;

    public ContractAdapter(Context c, ArrayList<Contract> contracts) {
        this.mContext = c;
        this.contracts = contracts;
    }

    @Override
    public ContractAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contract_item, null);
        ContractAdapter.ViewHolder viewHolder = new ContractAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contract contract = contracts.get(position);
        if(contract.getDpart().equals("") && contract.getName().equals("")){
            holder.contract_item_tv_dpart.setVisibility(View.INVISIBLE);
            holder.contract_item_tv_name.setText(contract.getPart());
            holder.contract_item_tv_number.setVisibility(View.INVISIBLE);
            holder.contract_item_tv_email.setVisibility(View.INVISIBLE);
            holder.contract_item_iv_call.setVisibility(GONE);
            holder.contract_item_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.actionbar_et_search.isFocused()){
                        MainActivity.actionbar_et_search.clearFocus();
                    }

                    String part = holder.contract_item_tv_name.getText().toString();

                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);

                    ArrayList<Contract> contracts = MainActivity.dbHelper.getContract(part);
                    ContractAdapter contractAdapter = new ContractAdapter(mContext, contracts);
                    MainActivity.actionbar_tv_title.setText(part);
                    MainActivity.main_rv.removeAllViews();
                    MainActivity.main_rv.setAdapter(contractAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                    MainActivity.rv_level=1;
                    MainActivity.actionbar_et_search.setText("");
                }
            });
        } else {
            holder.contract_item_tv_dpart.setVisibility(View.VISIBLE);
            holder.contract_item_tv_number.setVisibility(View.VISIBLE);
            holder.contract_item_tv_email.setVisibility(View.VISIBLE);
            holder.contract_item_iv_call.setVisibility(View.VISIBLE);
            holder.contract_item_tv_dpart.setText(contract.getDpart() + " / " + contract.getPosition());
            holder.contract_item_tv_name.setText(contract.getName());
            holder.contract_item_tv_number.setText(contract.getPhone());
            holder.contract_item_tv_email.setText(contract.getEmail());
            if (contract.getPhone().trim().length() <= 1)
                holder.contract_item_iv_call.setVisibility(GONE);
            holder.contract_item_iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(mContext, holder.contract_item_tv_number.getText(), Toast.LENGTH_SHORT).show();

                    if (MainActivity.actionbar_et_search.isFocused()) {
                        MainActivity.actionbar_et_search.clearFocus();
                    }

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.mContext2);
                    dialog.setTitle("전화걸기").setMessage(contract.getPart() + " / " + contract.getName() + "(" + contract.getPhone() + ")로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE);

                                    if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                        //권한 없음
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.contract_item_tv_number.getText()));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        try {
                                            mContext.startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //권한 있음
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.contract_item_tv_number.getText()));
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
        public TextView contract_item_tv_dpart, contract_item_tv_name, contract_item_tv_number, contract_item_tv_email;
        public ImageView contract_item_iv_call;
        public RelativeLayout contract_item_rl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            contract_item_iv_call = (ImageView) itemLayoutView.findViewById(R.id.contract_item_iv_call);
            contract_item_tv_name = (TextView) itemLayoutView.findViewById(R.id.contract_item_tv_name);
            contract_item_tv_number = (TextView) itemLayoutView.findViewById(R.id.contract_item_tv_number);
            contract_item_tv_dpart = (TextView) itemLayoutView.findViewById(R.id.contract_item_tv_dpart);
            contract_item_tv_email = (TextView) itemLayoutView.findViewById(R.id.contract_item_tv_email);
            contract_item_rl = (RelativeLayout) itemLayoutView.findViewById(R.id.contract_item_rl);
        }
    }

    @Override
    public int getItemCount() {
        return contracts.size();
    }
}
package com.ZuWay.atapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCartListAdapter extends RecyclerView.Adapter<MyCartListAdapter.Holder> {

    private Context context;
    private List<Productmodel> cartModelList;
    private MyCartListAdapter.CategoryAdapterCallback callback;
    Integer i;
    String customer_id, pid, cart_id;
    String editqut, type;
    String para_str2;

    public MyCartListAdapter(Context context, List<Productmodel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public MyCartListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyCartListAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private MyCartListAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        MyCartListAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.mycart_item_layout, viewGroup, false);
        viewHolder = new MyCartListAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartListAdapter.Holder holder, final int position) {

        final Productmodel model = cartModelList.get(position);

        holder.item_name.setText(model.getProduct_name());
        holder.item_price.setText(model.getProduct_disprice());
        holder.item_origiprice.setText(" ₹ " + model.getProduct_oldprice() + ".00");
        holder.item_save.setText(" ₹ " + model.getProduct_saveprice() + ".00");
        holder.item_size.setText(model.getProduct_size());
        holder.item_color.setText(model.getProduct_color());
        holder.item_stock.setText(model.getProduct_stack());
        holder.item_dis.setText(model.getProduct_dis());

        holder.tv_qty.setText(model.getProduct_qty());

        String url = model.getProduct_img();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.list_gray)
                .into(holder.item_image);

        customer_id = BSession.getInstance().getUser_id(context);

        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                holder.delet.startAnimation(animFadein);

                cart_id = cartModelList.get(position).getCart_id();

                final Map<String, String> params = new HashMap<>();

                String para_str3 = "?user_id=" + customer_id;
                String para_str4 = "&cart_id=" + cart_id;

                String baseUrl = ProductConfig.removecart + para_str3 + para_str4;

                StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                removeItem(position);

                            } else {
                                Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("Error", error.toString());

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsObjRequest);
            }
        });


        holder.editqut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                holder.editqut.startAnimation(animFadein);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.layout_editqut, null);

                final EditText qyutt = alertLayout.findViewById(R.id.et_qut);
                final TextView typee = alertLayout.findViewById(R.id.type);

                final Button btn = alertLayout.findViewById(R.id.sub_btn);
                final ImageView close = alertLayout.findViewById(R.id.close);


                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                alert.setView(alertLayout);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                        btn.startAnimation(animFadein);

                        editqut = qyutt.getText().toString();

                        typee.setText(editqut);

                        type = typee.getText().toString();
                        dialog.cancel();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                        close.startAnimation(animFadein);
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });

        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pid = cartModelList.get(position).getPid();
                final String _stringVal;
                Log.d("src", "Increasing value...");
                i = Integer.valueOf(cartModelList.get(position).getProduct_qty());
                i = i + 1;
                _stringVal = String.valueOf(i);
                holder.tv_qty.setText(_stringVal);
                cartModelList.get(position).setProduct_qty(_stringVal);


                final Map<String, String> params = new HashMap<>();

                String para_str3 = "?user_id=" + customer_id;
                String para_str1 = "&product_id=" + pid;
                para_str2 = "&quantity=" + "1";

                /*if(type!=null){
                    if(type.equalsIgnoreCase("0")){
                        para_str2 = "&quantity=" + "1";
                    }
                }
               else {
                    para_str2="&quantity=" +type;
                }*/

                String baseUrl = ProductConfig.incart + para_str3 + para_str1 + para_str2;

                StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                holder.tv_qty.setText(jsonResponse.getString("quantity"));
                                holder.item_price.setText(jsonResponse.getString("total"));
                            } else {
                                Toast.makeText(context, "Cart Add Failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("Error", error.toString());

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsObjRequest);


            }
        });

        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = cartModelList.get(position).getPid();
                String _stringVal;
                Log.d("src", "Decreasing value...");
                i = Integer.valueOf(cartModelList.get(position).getProduct_qty());
                if (i > 0) {
                    i = i - 1;
                    _stringVal = String.valueOf(i);
                    holder.tv_qty.setText(_stringVal);
                    cartModelList.get(position).setProduct_qty(_stringVal);

                    final Map<String, String> params = new HashMap<>();


                    String para_str3 = "?user_id=" + customer_id;
                    String para_str1 = "&product_id=" + pid;
                    String para_str2 = "&quantity=" + "1";


                    String baseUrl = ProductConfig.subcart + para_str3 + para_str1 + para_str2;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    holder.tv_qty.setText(jsonResponse.getString("quantity"));
                                    holder.item_price.setText(jsonResponse.getString("total"));


                                } else {
                                    Toast.makeText(context, "Cart sub Failed", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.e("Error", error.toString());

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(jsObjRequest);


                } else {
                    Log.d("src", "Value can't be less than 0");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartModelList == null ? 0 : cartModelList.size();
    }

    public void setCallback(MyCartListAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public void removeItem(int position) {
        cartModelList.remove(position);
        notifyItemRemoved(position);
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView item_name, item_dis, item_stock, item_price, item_origiprice, item_save, item_size, item_color, item_dd, item_dch, tv_plus, tv_qty, tv_add, tv_minus;
        ImageView item_image;
        CardView cardView;
        ImageView delet;
        LinearLayout editqut;

        private Holder(View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.mc_product_name);
            item_image = itemView.findViewById(R.id.mc_product_image);
            item_price = itemView.findViewById(R.id.mc_product_price);
            item_origiprice = itemView.findViewById(R.id.mc_product_priceorgi);
            item_size = itemView.findViewById(R.id.mc_product_size);
            item_color = itemView.findViewById(R.id.mc_product_color);
            item_stock = itemView.findViewById(R.id.mc_product_stock);
            item_dis = itemView.findViewById(R.id.mc_product_dis);
            item_save = itemView.findViewById(R.id.mc_product_save);
            delet = itemView.findViewById(R.id.mc_product_delet);
            cardView = itemView.findViewById(R.id.cv_mccard);
            item_dis = itemView.findViewById(R.id.mc_product_dis);
            item_stock = itemView.findViewById(R.id.mc_product_stock);
            editqut = itemView.findViewById(R.id.qutity_edit);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_plus = itemView.findViewById(R.id.tv_plus);
            tv_add = itemView.findViewById(R.id.tv_add);
            tv_minus = itemView.findViewById(R.id.tv_minus);


        }
    }
}



package com.estrategiamovilmx.sales.farmacia.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.model.Cheeses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;




/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoListFragment extends Fragment {
    private LinearLayout cart;
    private RecyclerView rv;
    private SimpleStringRecyclerViewAdapter mAdapter;
    public PhotoListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_list, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerview);
        cart = (LinearLayout) getActivity().findViewById(R.id.cart);
        setupRecyclerView(rv);
        return rv;
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        StaggeredGridLayoutManager llmg = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        llmg.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmg);
        mAdapter = new SimpleStringRecyclerViewAdapter(getActivity(),
                getRandomSublist(Cheeses.sCheeseStrings, 30),this);
        recyclerView.setAdapter(mAdapter);
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public  void addToCart(String url,SimpleStringRecyclerViewAdapter.ViewHolder holder){
        // Scaling
        int fromXscale = 1;
        int fromYscale = 1;
        int toXscale = 0;
        int toYscale = 0;
        Animation scale = new ScaleAnimation(fromXscale, toXscale, fromYscale, toYscale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
// 1 second duration
        scale.setDuration(1000);
// Moving up

        int[] originalPos = new int[2];
        holder.itemView.getLocationOnScreen(originalPos);
//or view.getLocationOnScreen(originalPos)
        float fromX = originalPos[0];
        float fromY = originalPos[1];
        float toX = cart.getX();
        float toY = cart.getY();
        Animation slideUp = new TranslateAnimation(fromX, toX, fromY, toY);
// 1 second duration
        slideUp.setDuration(1000);
// Animation set to join both scaling and moving
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillEnabled(true);
        animSet.addAnimation(scale);
        animSet.addAnimation(slideUp);
// Launching animation set
        holder.itemView.startAnimation(animSet);
    }
    /**********/
    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;
        private PhotoListFragment fragment;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<String> items,PhotoListFragment fragment) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.fragment = fragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_layout, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if (fragment!=null){

                         fragment.addToCart("url image",holder);
                     }
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(Cheeses.getRandomCheeseDrawable())
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

}

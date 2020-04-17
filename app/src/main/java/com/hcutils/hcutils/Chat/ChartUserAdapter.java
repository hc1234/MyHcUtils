package com.hcutils.hcutils.Chat;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.hcutils.hcutils.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChartUserAdapter extends BaseRecyclerViewAdapter<ChartUserAdapter.ChartViewHolder> {

    private List<String> mList = new ArrayList<>();
    private Map<String, ChartUserBean> mMap = new LinkedHashMap<>();
    MyItemOclick myItemOclick;
    public interface MyItemOclick{
        void item();
    }
    public void setItemOclick(MyItemOclick myItemOclick){
        this.myItemOclick=myItemOclick;
    }


    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_content_userlist_item, parent, false);
        return new ChartViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ChartViewHolder holder, int position) {
//        holder.mScreenLayout.setVisibility(View.GONE);
        holder.mVideoLayout.setVisibility(View.GONE);
        if (mList.isEmpty()) {
            return;
        }
        ChartUserBean item = mMap.get(mList.get(position));
        //fill surface
        //如果没有surface则不显示
        holder.mSurfaceContainer.removeAllViews();
//        holder.mScreenSurfaceContainer.removeAllViews();
        if (item == null) {
            return;
        }
        if (item.mCameraSurface != null) {
            holder.mVideoLayout.setVisibility(View.VISIBLE);
            //如果老的surfaceview还在之前的viewtree中，需要先移除
            ViewParent parent = item.mCameraSurface.getParent();
            if (parent != null) {
                if (parent instanceof FrameLayout) {
                    ((FrameLayout) parent).removeAllViews();
                }
                holder.mSurfaceContainer.removeAllViews();
            }
            holder.mSurfaceContainer.addView(item.mCameraSurface,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        }
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(myItemOclick!=null){
                    myItemOclick.item();
                }
                return false;
            }
        });



        if (item.mScreenSurface != null) {
            //如果老的surfaceview还在之前的viewtree中，需要先移除
//            holder.mScreenLayout.setVisibility(View.VISIBLE);
            ViewParent parent = item.mScreenSurface.getParent();
            if (parent != null) {
                if (parent instanceof FrameLayout) {
                    ((FrameLayout) parent).removeAllViews();
                }
//                holder.mScreenSurfaceContainer.removeAllViews();
            }
//            holder.mScreenSurfaceContainer.addView(item.mScreenSurface,
//                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }

//        holder.mVideoFlip.setOnCheckedChangeListener(null);
//        holder.mVideoFlip.setChecked(item.mIsCameraFlip);
//
//        holder.mScreenFlip.setOnCheckedChangeListener(null);
//        holder.mScreenFlip.setChecked(item.mIsScreenFlip);

//        holder.mVideoFlip.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (mOnSubConfigChangeListener != null) {
//                mOnSubConfigChangeListener.onFlipView(item.mUserId, AliRtcConstants.CAMERA,isChecked);
//            }
//        });


//        holder.mScreenFlip.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (mOnSubConfigChangeListener != null) {
//                mOnSubConfigChangeListener.onFlipView(item.mUserId, AliRtcConstants.SCREEN,isChecked);
//            }
//        });

//        holder.mVideoMediaInfo.setOnClickListener(v -> {
//            if (mOnSubConfigChangeListener != null) {
//                mOnSubConfigChangeListener.onShowVideoInfo(item.mUserId,AliRtcConstants.CAMERA);
//            }
//        });

//        holder.mScreenMediaInfo.setOnClickListener(v -> {
//            if (mOnSubConfigChangeListener != null) {
//                mOnSubConfigChangeListener.onShowVideoInfo(item.mUserId,AliRtcConstants.SCREEN);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<ChartUserBean> list, boolean notify) {
        mList.clear();
        mMap.clear();
        for (ChartUserBean item : list) {
            mList.add(item.mUserId);
            mMap.put(item.mUserId, item);
        }
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void addData(ChartUserBean data, boolean notify) {
        mList.add(data.mUserId);
        mMap.put(data.mUserId, data);
        if (notify) {
            notifyItemInserted(mList.size() - 1);
        }
    }

    public void removeData(String uid, boolean notify) {
        int index = mList.indexOf(uid);
        if (index < 0) {
            return;
        }
        mList.remove(uid);
        mMap.remove(uid);
        if (notify) {
            notifyItemRemoved(index);
        }
    }

    public void updateData(ChartUserBean data, boolean notify) {
        if (mList.contains(data.mUserId)) {
            int index = mList.indexOf(data.mUserId);
            mMap.put(data.mUserId, data);
            if (notify) {
                notifyItemChanged(index);
            }
        } else {
            addData(data, notify);
        }
    }

    public ChartUserBean createDataIfNull(String uid) {
        ChartUserBean ret;
        if (TextUtils.isEmpty(uid) || (ret = mMap.get(uid)) == null) {
            ret = new ChartUserBean();
        }
        return ret;
    }

    public boolean containsUser(String uid) {
        if (!mList.isEmpty() && mList.contains(uid)) {
            return true;
        }
        return false;
    }

    public static class ChartViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mVideoLayout;
        public FrameLayout mSurfaceContainer;

        public ChartViewHolder(View itemView) {
            super(itemView);
            mVideoLayout = itemView.findViewById(R.id.chart_content_userlist_item_video_layout);
            mSurfaceContainer = itemView.findViewById(R.id.chart_content_userlist_item_surface_container);
        }

    }




}

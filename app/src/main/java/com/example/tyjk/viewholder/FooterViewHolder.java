package com.example.tyjk.viewholder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tyjk.activity.R;
import com.example.tyjk.adapter.BaseRecyclerViewAdapter;

/**
 * Created by tyjk on 2017/10/16.
 */
public class  FooterViewHolder extends MyRVViewHolder {

    private BaseRecyclerViewAdapter mAdapter;
    private TextView mFooterTextView;
    private ProgressBar mProgressBar;

    public FooterViewHolder(View itemView) {
        super(itemView);
        mFooterTextView = itemView.findViewById(R.id.tv_footer);
        mProgressBar = itemView.findViewById(R.id.footer_progressbar);
        mFooterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter != null && mAdapter.getLoadState() == BaseRecyclerViewAdapter.STATE_FAILURE) {
                    mFooterTextView.setText("加载中···");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAdapter.reload();
                    mAdapter.setLoadState(BaseRecyclerViewAdapter.STATE_LOADING);
                }
            }
        });
    }

    public void setMessage(String msg) {
        mFooterTextView.setText(msg);
    }

    public void setProgressBarVisible(int visible) {
        mProgressBar.setVisibility(visible);
    }

    public void bindAdapter(BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    public int getHolderType() {
        return 1;
    }


}

package movieapp.android.come.movieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class detailsAdapter extends BaseAdapter {
    private final int ORANGE = 0xFFFF3300;
    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;
    private int layoutResourceId;


    private ArrayList<trailerModel> mTrailerModel = new ArrayList<>();
    private ArrayList<reviewModel> mReviewModel = new ArrayList<>();

    public detailsAdapter(Context mContext, ArrayList<trailerModel> mTrailerModel,ArrayList<reviewModel> mReviewModel) {
        Log.v("number of rows", mTrailerModel.size() + "");
        Log.v("number of rows", mReviewModel.size() + "");
        //super(mContext, layoutResourceId, mDetailModel);
        this.mContext = mContext;
        this.mTrailerModel = mTrailerModel;
        this.mReviewModel=mReviewModel;
        //this.layoutResourceId = layoutResourceId;
    }

    private static final int VIEW_TYPE_TRAILER = 0;
    private static final int VIEW_TYPE_REVIEW = 1;
    private static final int VIEW_TYPE_COUNT = 2;








    @Override
    public int getCount() {
        return (mTrailerModel.size()+mReviewModel.size());
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    String getType(int position){

         if(position<mTrailerModel.size())
            return "trailer";
        else
            return "review";

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Choose the layout type
        View v=convertView;

        int viewType = layoutResourceId;


        // TODO: Determine layoutId from viewType
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    if (getType(position) == "trailer") {
                        final trailerModel item = mTrailerModel.get(position);
                        v = inflater.inflate(R.layout.trailer_item, parent, false);
                        //TextView trailerTextView = new TextView(mContext);

                        LinearLayout trailer_Loayout = (LinearLayout) v.findViewById(R.id.trailer_layout);
                        LinearLayout lin = new LinearLayout(mContext);
                        lin.setOrientation(LinearLayout.HORIZONTAL);

                        //       trailerModel trailerObj:mTrailerModel
                        ImageView trailerImage = new ImageView(mContext);
                        TextView trailerTextView = new TextView(mContext);
                       // trailerTextView.setTextColor(15);
                        trailerTextView.setTextColor(Color.RED);
                        trailerImage.setImageResource(R.drawable.mpic);
                        trailerImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                        String name = item.getName();
                        final String key = item.getKey();
                        trailerTextView.setText(name);
                        Log.i("nameee of object ", name);
                        lin.addView(trailerImage);

                        lin.addView(trailerTextView);

                        trailer_Loayout.addView(lin);
                        lin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
                                mContext.startActivity(intent);

                            }
                        });
                        return v;

                    }else if(getType(position)=="review"){
                        reviewModel  item2=mReviewModel.get(position-mTrailerModel.size());
                        v = inflater.inflate(R.layout.review_item, parent, false);
                        LinearLayout reviewL= (LinearLayout) v.findViewById(R.id.review_layout);
                        LinearLayout review_item_Layout=new LinearLayout(mContext);
                        review_item_Layout.setOrientation(LinearLayout.VERTICAL);

                        //reviewModel reviewObj:mReviewModel
                        TextView authorTextView = new TextView(mContext);
                        authorTextView.setTextSize(25);
                        authorTextView.setGravity(Gravity.CENTER);
                        authorTextView.setTextColor(ORANGE);
                        TextView contentTextView = new TextView(mContext);
                        contentTextView.setTextSize(18);
                        authorTextView.setText(item2.getAuthor());
                        String  alContent=item2.getContent();
                        if(alContent.length()>300)
                        {
                            String  content=alContent.substring(0,300)+"see More ...";
                            contentTextView.setText(content);
                        }else
                        {
                            contentTextView.setText(item2.getContent());
                        }

                        review_item_Layout.addView(authorTextView);
                        review_item_Layout.addView(contentTextView);
                        reviewL.addView(review_item_Layout);

                        return v;

                    }



                   return  v;
                }

    static class ViewHolder {
        ImageView trailerImage;
        TextView trailerTextView;
        TextView authorTextView;
        TextView contentTextView;


    }
}

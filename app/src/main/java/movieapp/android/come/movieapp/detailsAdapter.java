package movieapp.android.come.movieapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detailsAdapter extends ArrayAdapter<GridItem> {

    //private final ColorMatrixColorFilter grayscaleFilter;
    private Context mContext;

    private ArrayList<reviewModel> mDetailModel = new ArrayList<>();

    public detailsAdapter(Context mContext,int flag, ArrayList<reviewModel> mDetailModel) {
        super(mContext,flag,mDetailModel);
        this.mContext = mContext;
        this.mDetailModel = mDetailModel;
    }

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_TRAILER = 1;
    private static final int VIEW_TYPE_REVIEW = 2;
    private static final int VIEW_TYPE_COUNT = 3;


    @Override
    public int getItemViewType(int position) {

        if(position==0)
        {
            return VIEW_TYPE_HEADER;
        }else if(position==1)
        {
            return VIEW_TYPE_TRAILER;
        }else
        {
            return VIEW_TYPE_REVIEW;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mDetailModel
     */

    public void setGridData(ArrayList<reviewModel> mDetailModel) {
        this.mDetailModel = mDetailModel;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position,Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        ViewHolder holder=new ViewHolder();

        reviewModel item = mDetailModel.get(position);

        View view = null;
        // TODO: Determine layoutId from viewType
        switch (viewType) {
            case VIEW_TYPE_HEADER: {
                layoutId = R.layout.details_header;
                // header xml  define elements
                 view = LayoutInflater.from(context).inflate(layoutId, parent, false);
                holder.titleTextView = (TextView) view.findViewById(R.id.DetailedTitle);
                holder.imageView = (ImageView) view.findViewById(R.id.DetailedImage);
                holder.puplarityTextView = (TextView) view.findViewById(R.id.DetailedPoularity);
                holder.releaseDateTextView = (TextView) view.findViewById(R.id.ReleaseDate);
                holder.overViewTextView = (TextView) view.findViewById(R.id.DetailedOverview);
                //seting data in header xml
                //define
                //reviewModel item = mDetailModel.get(position);


                holder.titleTextView.setText(Html.fromHtml(item.getTitle()));
                Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
                holder.puplarityTextView.setText(item.getPopularity());
                holder.releaseDateTextView.setText(item.getReleaseDate());
                holder.overViewTextView.setText(item.getOverview());



                break;
            }
            case VIEW_TYPE_TRAILER: {
                layoutId = R.layout.trailer_item;
                // trailer xml
                 view = LayoutInflater.from(context).inflate(layoutId, parent, false);
                holder.trailerTextView = (TextView) view.findViewById(R.id.trailer_item_text);
                //seting trailer row  data
                holder.trailerTextView.setText(item.getName());

                break;
            }
            case  VIEW_TYPE_REVIEW:{
                layoutId = R.layout.review_item;
                 view = LayoutInflater.from(context).inflate(layoutId, parent, false);
                holder.authorTextView = (TextView) view.findViewById(R.id.review_author_text);
                holder.contentTextView = (TextView) view.findViewById(R.id.review_content_text);

                holder.authorTextView.setText(item.getAuthor());
                holder.contentTextView.setText(item.getContent());
                break;
            }
            default:
            {
                holder= (ViewHolder) view.getTag();
            }
        }
        view.setTag(holder);


        return view;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        TextView puplarityTextView;
        TextView releaseDateTextView;
        TextView overViewTextView;
        TextView trailerTextView;
        TextView authorTextView;
        TextView contentTextView;


    }
}
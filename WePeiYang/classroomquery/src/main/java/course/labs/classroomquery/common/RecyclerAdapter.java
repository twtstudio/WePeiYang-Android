package course.labs.classroomquery.common;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import course.labs.classroomquery.Model.ClassroomBean;
import course.labs.classroomquery.R;

/**
 * Created by asus on 2017/1/23.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<IViewHolder> {

    private List<ClassroomBean> mItems;
    private ClassroomBean nowItem;
    private final int isRrefreshing = 1;
    private final int notRrefreshing = 0;
    private final int ITEM_TYPE_HOME = 0;
    private final int ITEM_TYPE_COLLECT = 1;
    private int type;
    private int loadState;
    private boolean isSetUpdate;
    private Context mContext;
    private boolean isInPeiyang;
    public RecyclerAdapter(Context context,int type) {
        mItems = new ArrayList<>();
        loadState = 0;
        this.mContext = context;
        this.type = type;
        isSetUpdate = true;
    }
    public void setXiaoqu(boolean isInBeiyangyuan){
        isInPeiyang = isInBeiyangyuan;
    }



    public void append(List<ClassroomBean> images) {
        int positionStart = mItems.size();
        int itemCount = images.size();
        mItems.addAll(images);
        isSetUpdate = true;
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }
    }
    public void addAll(Collection<ClassroomBean> list){
        for(ClassroomBean bean:list){
            boolean isExist = false;
            for(ClassroomBean item:mItems){
                if(bean.equals(item)){
                    isExist = true;
                }
            }
            if(!isExist){
                mItems.add(bean);
            }
        }
        notifyDataSetChanged();
        //mItems.addAll(list);
    }
    public void remove(int position) {
        mItems.remove(position);
        isSetUpdate = true;
        notifyItemRemoved(position);
    }

    public void clear(){
        mItems.clear();
        isSetUpdate = true;
        notifyDataSetChanged();
    }

    public ClassroomBean getItem(int powsition){
        return mItems.get(powsition);
    }
    public  void add(ClassroomBean item){

        for(ClassroomBean bean:mItems) {
            if(bean.equals(item)){
                return;
            }
        }
        mItems.add(item);
        isSetUpdate = true;
        notifyDataSetChanged();

    }
    public void addFront(ClassroomBean item){
        isSetUpdate = true;
        mItems.add(0,item);
        notifyDataSetChanged();
    }

    @Override
    public IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("Adapter","diao yong");
        CardView imageView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final ViewHolder holder = new ViewHolder(imageView);
        return holder;




    }
    @Override
    public void onBindViewHolder(IViewHolder holder, final int position) {
            nowItem = mItems.get(position);

            View itemLayout = (View) holder.itemView;

            final TextView classRoomName =(TextView) itemLayout.findViewById(R.id.classroomItem);
            final TextView xiaoqu = (TextView)itemLayout.findViewById(R.id.xiaoquItem) ;
            final TextView time = (TextView)itemLayout.findViewById(R.id.time);
            final Button button = (Button)itemLayout.findViewById(R.id.clickButton);
            final ImageView image1 = (ImageView)itemLayout.findViewById(R.id.image1);
            final ImageView image2 = (ImageView)itemLayout.findViewById(R.id.image2);
            final ImageView image3 = (ImageView)itemLayout.findViewById(R.id.image3);
            final ImageView image4 = (ImageView)itemLayout.findViewById(R.id.image4);
            final ImageView image5 = (ImageView)itemLayout.findViewById(R.id.image5);
           // final ImageView image6 = (ImageView)itemLayout.findViewById(R.id.image6);

            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.INVISIBLE);
            image4.setVisibility(View.INVISIBLE);
            image5.setVisibility(View.INVISIBLE);
          //  image6.setVisibility(View.INVISIBLE);

            classRoomName.setText(nowItem.getRoomNumber());
            button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   System.out.println("On click");
                   collectAnddisconnect(position);
               }
            });
           //设置校区


           //是否收藏
           if(nowItem.isCollected()){
               button.setBackground(mContext.getResources().getDrawable(R.drawable.classroomicon_collected));
           }
          else{
               button.setBackground(mContext.getResources().getDrawable(R.drawable.classroom_icon_uncollected));
           }
         //如果是在查询界面
           if(type==ITEM_TYPE_HOME) {
               if(isInPeiyang){
                   xiaoqu.setText("北洋园校区");

               }
               else{
                   xiaoqu.setText("卫津路校区");
               }
               time.setVisibility(View.VISIBLE);
               if(isSetUpdate) {
                   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                   Date timeShow = new Date(System.currentTimeMillis());

                   time.setText(formatter.format(timeShow));
               }
               //if (nowItem.getState().equals("空闲")) {
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_empty));
              // } else if (nowItem.getState().equals("即将下课")) {
              /*     image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_willbeempty));
               } else if(nowItem.getState().equals("即将上课")){
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_willhaveclass));
               }
               else{
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_inclass));
               }*/
               int cnt = 0;//其他条件的个数
               Date curDate = new Date(System.currentTimeMillis());
               int month = curDate.getMonth();
               int date = curDate.getDate();

               if (month >= 12 || month <= 2 || (month == 11 && date > 15) || (month == 3 && date < 15)) {
                   if (nowItem.isHasHeat()) {
                       cnt++;
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_heat));
                   }

               } else {
                   if (nowItem.isHasAC()) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_airconditioning));

                   } else {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_fan));
                   }
               }

               if (nowItem.isNearToliet()) {
                   if (cnt == 1) {
                       image3.setVisibility(View.VISIBLE);
                       image3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_toilet));
                   }
                   if (cnt == 0) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_toilet));
                   }
                   cnt++;
               }
               if (nowItem.isHasElectricity()) {

                   if (cnt == 0) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroomtag_electricityt));
                   } else if (cnt == 2) {
                       image4.setVisibility(View.VISIBLE);
                       image4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroomtag_electricityt));
                   } else {
                       image3.setVisibility(View.VISIBLE);
                       image3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroomtag_electricityt));
                   }
                   cnt++;
               }

               if (nowItem.isNearWater()) {
                   if (cnt == 0) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   } else if (cnt == 1) {
                       image3.setVisibility(View.VISIBLE);
                       image3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   } else if (cnt == 2) {
                       image4.setVisibility(View.VISIBLE);
                       image4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   } else {
                       image5.setVisibility(View.VISIBLE);
                       image5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   }
                   cnt++;
               }
           }
           //如果是收藏界面
           else{
               int cnt = 0;//其他条件的个数
               time.setVisibility(View.VISIBLE);
               System.out.println("楼号"+nowItem.getNumber());
               if(nowItem.getNumber()>=31){
                   xiaoqu.setText("北洋园校区");
               }
               else{
                   xiaoqu.setText("卫津路校区");
               }
               if(isSetUpdate) {
                   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                   Date timeShow = new Date(System.currentTimeMillis());

                   time.setText(formatter.format(timeShow));
               }

              /* if (!nowItem.isOccupied()) {
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_empty));
                   if(nowItem.isWillBeOccupied()){
                       image2.setVisibility(View.VISIBLE);
                       cnt++;
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_willhaveclass));
                   }
               }
               else{
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_inclass));
                   if(!nowItem.isWillBeOccupied()){
                       cnt++;
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_willbeempty));
                   }
               }*/
               if (nowItem.getState().equals("空闲")) {
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_empty));
               } else if (nowItem.getState().equals("即将下课")) {
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_willbeempty));
               } else if(nowItem.getState().equals("即将上课")){
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_willhaveclass));
               }
               else{
                   image1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_inclass));
               }

               Date curDate = new Date(System.currentTimeMillis());
               int month = curDate.getMonth();
               int date = curDate.getDate();
               if (month >= 12 || month <= 2 || (month == 11 && date > 15) || (month == 3 && date < 15)) {
                   if (nowItem.isHasHeat()) {
                       if(cnt==0) {
                           image2.setVisibility(View.VISIBLE);
                           image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_heat));
                       }

                       cnt++;
                   }

               } else {
                   if (nowItem.isHasAC()) {
                      if(cnt==0) {
                          image2.setVisibility(View.VISIBLE);
                          image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_airconditioning));
                      }

                       cnt++;
                   } else {
                       if(cnt==0) {
                           image2.setVisibility(View.VISIBLE);
                           image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_fan));
                       }



                       cnt++;
                   }
               }

               if (nowItem.isNearToliet()) {
                   if (cnt == 0) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_toilet));
                   }
                   if (cnt == 1) {
                       image3.setVisibility(View.VISIBLE);
                       image3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_toilet));
                   }


                   cnt++;
               }
               if (nowItem.isHasElectricity()) {

                   if (cnt == 0) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroomtag_electricityt));
                   }
                   if (cnt == 1) {
                       image3.setVisibility(View.VISIBLE);
                       image3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroomtag_electricityt));
                   }
                   if (cnt == 2) {
                       image4.setVisibility(View.VISIBLE);
                       image4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroomtag_electricityt));
                   }


                   cnt++;
               }

               if (nowItem.isNearWater()) {
                   if (cnt == 0) {
                       image2.setVisibility(View.VISIBLE);
                       image2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   } else if (cnt == 1) {
                       image3.setVisibility(View.VISIBLE);
                       image3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   } else if (cnt == 2) {
                       image4.setVisibility(View.VISIBLE);
                       image4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   } else if(cnt==3){
                       image5.setVisibility(View.VISIBLE);
                       image5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classroom_tag_yinshuiji));
                   }

                   cnt++;
               }
           }

           /* try {
                Bitmap bitmap = nowItem.getBitmap();
                imageView.setBackgroundColor(mContext.getResources().getColor(R.color.BurlyWood));
                imageView.setImageBitmap(bitmap);
            }
            catch (NullPointerException E){
                E.printStackTrace();
                // imageView.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.twt));

            }*/


    }
    public void collectAnddisconnect(int position){
        isSetUpdate = false;
        if(type==ITEM_TYPE_HOME){
            mItems.get(position).setCollected(!( mItems.get(position).isCollected()));
            notifyItemChanged(position);
            if(mItems.get(position).isCollected()){
                CollectMotionCotroller motionCotroller = new CollectMotionCotroller() {
                    @Override
                    public void onError() {
                        Toast.makeText(mContext,"收藏失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext() {
                       Toast.makeText(mContext,"收藏成功",Toast.LENGTH_SHORT).show();
                    }
                };
                CollectMotionPresenter mPresenter = new CollectMotionPresenter(mContext,motionCotroller);
                mPresenter.collect(mItems.get(position).getRoomNumber());
            }
            else{
                CollectMotionCotroller motionCotroller = new CollectMotionCotroller() {
                    @Override
                    public void onError() {
                        Toast.makeText(mContext,"取消收藏失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext() {
                        Toast.makeText(mContext,"取消收藏成功",Toast.LENGTH_SHORT).show();
                    }
                };
                CollectMotionPresenter mPresenter = new CollectMotionPresenter(mContext,motionCotroller);
                mPresenter.cancelCollect(mItems.get(position).getRoomNumber());
            }
        }
        else{
            CancelCollectController motionCotroller = new CancelCollectController() {
                @Override
                public void onError() {
                       Toast.makeText(mContext,"取消收藏失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(int data) {
                        System.out.println(data);
                        Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();

                }
            };
            CancelCollectPresenter mPresenter = new CancelCollectPresenter(mContext,motionCotroller);
            mPresenter.cancelCollect(mItems.get(position).getRoomNumber());

            mItems.remove(position);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setLoadState(int state){
        this.loadState = state;
    }

   static class ViewHolder extends IViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}


package com.qingmang.market.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;


/**
 * Created by Administrator on 2017/6/16.
 */

public class CanvasUtils
{
      public static Bitmap roundImage(Bitmap bitmap)
      {
          Bitmap barrageBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
          Canvas canvas = new Canvas(barrageBitmap);
          Paint paint = new Paint();
          paint.setAntiAlias(true);
          paint.setColor(Color.BLACK);
          paint.setStyle(Paint.Style.FILL);
          canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);


          paint.reset();
          paint.setAntiAlias(true);
          paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
          canvas.drawBitmap(bitmap, 0, 0, paint);
          return barrageBitmap;
      }

    /**
     * 描边效果文字
     * @param canvas
     * @param text  文字
     * @param x    canvas X点
     * @param y     canvas Y点
     * @param textSize  文字大小sp
     * @param textColor  文字颜色
     * @param strokeColor  描边颜色
     * @param isBold  字体是否加粗
     * @param isStroke 字体是否描边
     * @return 返回文字大小数组  index :0宽 1高
     */
    public static float[] drawText(Context mContext,Canvas canvas, Paint paint,String text, int x, int y,boolean isStroke,boolean isBold,int textColor,int textSize,int strokeColor)
    {
        paint.setTextSize(sp2px(mContext, textSize));
        paint.setDither(true);  //防抖动
        int alpha=paint.getAlpha();
        if(isBold)
        {
            paint.setFakeBoldText(true);  //字体加粗
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            paint.setTypeface(font); //字体加粗 上边那个有时候无效
        }
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        float baseLineY = y - (fontMetrics.bottom-fontMetrics.top)/4- fontMetrics.top;
        if(isStroke)
        {
            // 自定义描边效果
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(ContextCompat.getColor(mContext, strokeColor));
            paint.setStrokeWidth(3);
            paint.setAlpha(alpha);
            canvas.drawText(text, x, baseLineY, paint);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        paint.setColor(ContextCompat.getColor(mContext, textColor));
        paint.setAlpha(alpha);
        canvas.drawText(text, x, baseLineY , paint);


        float[] textWH = new float[2];
        textWH[0] = textRect.width();
        textWH[1] = textRect.height();
        return textWH;
    }


    private static int sp2px(Context context, int sp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp,
                context.getResources().getDisplayMetrics());
    }

    public  static Bitmap bitmapScale(Bitmap bm, int newWidth, int newHeight)
    {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
    }

    public static void paintReset(Paint paint, int alpha)
    {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setAlpha(255-alpha);
    }

//    public Bitmap createBitmap( Bitmap headBitmap, String name, String msg, int level,boolean orV)
//    {
//
//
//        //画板宽
//        int viewWidth = CommonUtils.getScreenWidth(AppController.getInstance());
//        //画板高
//        int viewHeight = diameter + diameter / 3;
//        //创建画板
//        Bitmap barrageBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
//        viewHeight=diameter;
//
//        Canvas canvas = new Canvas(barrageBitmap);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
//        //创建画笔
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);//锯齿效果
//        //等级头像框
//        //Bitmap borderBp = CanvasUtils.bitmapScale(getBitmapById(getLevImageShapeId(level)), diameter, diameter);
//
//        //等级控件 取图片ID
//        Bitmap levelBp = CanvasUtils.bitmapScale(getBitmapById(LevelView.getLevelRoundImage(level)), levelWidth, levelHeight);
//        //Bitmap levelBp = levelBitmap;
//
//
//        //画头像
//        canvas.drawBitmap(CanvasUtils.bitmapScale(CanvasUtils.roundImage(headBitmap), diameter, diameter), 2, viewHeight - diameter, paint);
//
//        //画头像边框
//        paint.setStrokeWidth(2);
//        paint.setColor(ContextCompat.getColor(mContext, R.color.white));
//        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(diameter / 2+1, viewHeight - diameter / 2, diameter / 2 , paint);
//        paint.reset();
//        paint.setAntiAlias(true);//锯齿效果
//
//        // canvas.drawBitmap(borderBp, 0, viewHeight - borderBp.getHeight(), paint);
//
//        //  画头像角标
//        if(orV)
//        {
//            Bitmap cornerBp = CanvasUtils.bitmapScale(getBitmapById(R.drawable.v_28x28), cornerWidth, cornerHeight);
//            canvas.drawBitmap(cornerBp, diameter - diameter / 3, viewHeight - cornerHeight, paint);
//        }
//
//        CanvasUtils.paintReset(paint, 0);
//        //画名字
//        float nameSize[] = CanvasUtils.drawText(mContext, canvas, paint, name,
//                diameter + 10,
//                viewHeight - diameter + 6,
//                false,
//                true,
//                NAME_TEXT_COLOR, NAME_SIZE, NAME_TEXT_STROKE_COLOR);
//        //画内容
//        CanvasUtils.paintReset(paint, 0);
//        float textSize[] = CanvasUtils.drawText(mContext, canvas, paint, msg,
//                diameter + 10,
//                viewHeight - CommonUtils.dp2px(mContext, 10),
//                false,
//                false,
//                TEXT_COLOR, TEXT_SIZE, 0);
//
//        //画等级控件
//        //  canvas.drawBitmap(levelBp, diameter/2-levelBp.getWidth()/2 , diameter + diameter / 3-levelBp.getHeight()-15, paint);
//        canvas.drawBitmap(levelBp, diameter + 10 + nameSize[0] +10 , viewHeight - diameter + 6, paint);
//
//
//        CanvasUtils.paintReset(paint, 0);
//        paint.setStyle(Paint.Style.FILL);//充满
//        paint.setColor(Color.BLACK);
//        paint.setAlpha(65);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
//        RectF oval3 = new RectF(diameter / 2, viewHeight - CommonUtils.dp2px(mContext, 13), diameter + textSize[0] + 25, viewHeight);
//        //画文字背景
//        canvas.drawRoundRect(oval3, 60, 60, paint);//第二个参数是x半径，第三个参数是y半径
//
//
//        //剪切弹幕图片，把多余的部分去掉
//        float nameWidth = diameter + 10 + nameSize[0] +10 +levelBp.getWidth() + 1;
//        float textWidth = diameter + textSize[0] + 20;
//        viewWidth = (int) (nameWidth > textWidth ? nameWidth : textWidth) + 5;
//
//        return Bitmap.createBitmap(barrageBitmap, 0, 0, viewWidth, diameter + diameter / 3);
//    }
}

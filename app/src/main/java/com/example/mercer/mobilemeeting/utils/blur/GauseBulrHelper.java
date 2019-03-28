package com.example.mercer.mobilemeeting.utils.blur;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * @author lizhenya
 *
 * @time 16/5/28
 *
 * @类描述：
 */
public class GauseBulrHelper {
    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 7;

    /**
     * @方法描述：模糊图片
     * @author lizhenya
     * @param context
     *            上下文
     * @param bitmap
     *            待模糊的图片
     * @return 模糊后的图片
     */
    public static Bitmap blur(Context context, Bitmap bitmap) {
        return blur(context, bitmap, BLUR_RADIUS, BITMAP_SCALE, true);
    }

    /**
     * @方法描述：图片进行模糊
     * @author lizhenya
     * @param context
     *            上下文
     * @param bitmap
     *            待模糊的图片
     * @param canReuseInBitmap
     *            原始Bitmap是否还会使用（如果原始图片还会再次使用则参数设为true，否则会报出java.lang.
     *            IllegalStateException异常，一般情况下设为true防止程序出错）
     * @return 模糊后的图片
     */
    public static Bitmap blur(Context context, Bitmap bitmap,
                              boolean canReuseInBitmap) {
        return blur(context, bitmap, BLUR_RADIUS, BITMAP_SCALE,
                canReuseInBitmap);
    }

    /**
     *
     * @方法描述：
     * @author lizhenya
     * @param context
     *            上下文
     * @param bitmap
     *            待模糊的图片
     * @param blur_radius
     *            模糊半径（1~25的正整数）
     * @param canReuseInBitmap
     *            原始Bitmap是否还会使用（如果原始图片还会再次使用则参数设为true，否则会报出java.lang.
     *            IllegalStateException异常，一般情况下设为true防止程序出错）
     * @return 模糊后的图片
     */
    public static Bitmap blur(Context context, Bitmap bitmap, int blur_radius,
                              boolean canReuseInBitmap) {
        return blur(context, bitmap, blur_radius, BITMAP_SCALE,
                canReuseInBitmap);
    }

    /**
     *
     * @方法描述：
     * @author lizhenya
     * @param context
     *            上下文
     * @param bitmap
     *            待模糊的图片
     * @param bitmapScale
     *            图片的压缩因子
     * @param canReuseInBitmap
     *            原始Bitmap是否还会使用（如果原始图片还会再次使用则参数设为true，否则会报出java.lang.
     *            IllegalStateException异常，一般情况下设为true防止程序出错）
     * @return 模糊后的图片
     */
    public static Bitmap blur(Context context, Bitmap bitmap,
                              float bitmapScale, boolean canReuseInBitmap) {
        return blur(context, bitmap, BLUR_RADIUS, bitmapScale, canReuseInBitmap);
    }

    public static Bitmap blur(Context context, Bitmap bitmap, int blur_radius,
                              float bitmapScale, boolean canReuseInBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return RSBlur(context, bitmap, bitmapScale, blur_radius);
        } else {
            return doBlur(bitmap, blur_radius, canReuseInBitmap);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap RSBlur(Context context, Bitmap bitmap,
                                float bitmap_scale, int blur_radius) {
        // 先对图片进行压缩然后再blur
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap,
                Math.round(bitmap.getWidth() * bitmap_scale),
                Math.round(bitmap.getHeight() * bitmap_scale), false);
        // 创建空的Bitmap用于输出
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        // ①、初始化Renderscript
        RenderScript rs = RenderScript.create(context);
        // ②、Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs,
                Element.U8_4(rs));
        // ③、native层分配内存空间
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // ④、设置blur的半径然后进行blur
        theIntrinsic.setRadius(blur_radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        // ⑤、拷贝blur后的数据到java缓冲区中
        tmpOut.copyTo(outputBitmap);
        // ⑥、销毁Renderscript
        rs.destroy();
        bitmap.recycle();

        return outputBitmap;
    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius,
                                boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}


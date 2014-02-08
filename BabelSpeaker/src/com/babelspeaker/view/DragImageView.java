package com.babelspeaker.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class DragImageView extends ImageView {

    private Activity mActivity;

    private int screen_W, screen_H;// �ɼ���Ļ�Ŀ�߶�?

    private int bitmap_W, bitmap_H;// ��ǰͼƬ���?

    private int MAX_W, MAX_H, MIN_W, MIN_H;// ����ֵ

    private int current_Top, current_Right, current_Bottom, current_Left;// ��ǰͼƬ�����������?

    private int start_Top = -1, start_Right = -1, start_Bottom = -1, start_Left = -1;// ��ʼ��Ĭ��λ��.

    private int start_x, start_y, current_x, current_y;// ����λ��

    private float beforeLenght, afterLenght;// ���������?

    private float scale_temp;// ���ű���

    private enum MODE {
        NONE, DRAG, ZOOM

    };

    private MODE mode = MODE.NONE;// Ĭ��ģʽ

    private boolean isControl_V = false;// ��ֱ���?

    private boolean isControl_H = false;// ˮƽ���?

    private ScaleAnimation scaleAnimation;// ���Ŷ���

    private boolean isScaleAnim = false;// ���Ŷ���

    private MyAsyncTask myAsyncTask;// �첽����

    /** ���췽�� **/
    public DragImageView(Context context) {
        super(context);
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /** �ɼ���Ļ���?**/
    public void setScreen_W(int screen_W) {
        this.screen_W = screen_W;
    }

    /** �ɼ���Ļ�߶� **/
    public void setScreen_H(int screen_H) {
        this.screen_H = screen_H;
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /***
     * ������ʾͼƬ
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        /** ��ȡͼƬ���?**/
        bitmap_W = bm.getWidth();
        bitmap_H = bm.getHeight();

        MAX_W = bitmap_W * 3;
        MAX_H = bitmap_H * 3;

        MIN_W = bitmap_W / 2;
        MIN_H = bitmap_H / 2;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (start_Top == -1) {
            start_Top = top;
            start_Left = left;
            start_Bottom = bottom;
            start_Right = right;
        }

    }

    /***
     * touch 事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /** 处理单点、多点触�?**/
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            onTouchDown(event);
            break;
        // 多点触摸
        case MotionEvent.ACTION_POINTER_DOWN:
            onPointerDown(event);
            break;

        case MotionEvent.ACTION_MOVE:
            onTouchMove(event);
            break;
        case MotionEvent.ACTION_UP:
            mode = MODE.NONE;
            break;

        // 多点松开
        case MotionEvent.ACTION_POINTER_UP:
            mode = MODE.NONE;
            /** 执行缩放还原 **/
            if (isScaleAnim) {
                doScaleAnim();
            }
            break;
        }

        return true;
    }

    /** 按下 **/
    void onTouchDown(MotionEvent event) {
        mode = MODE.DRAG;

        current_x = (int) event.getRawX();
        current_y = (int) event.getRawY();

        start_x = (int) event.getX();
        start_y = current_y - this.getTop();

    }

    /** 两个手指 只能放大缩小 **/
    private void onPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mode = MODE.ZOOM;
            beforeLenght = getDistance(event);// ��ȡ����ľ���?
        }
    }

    /** 移动的处�?**/
    private void onTouchMove(MotionEvent event) {
        int left = 0, top = 0, right = 0, bottom = 0;
        /** 处理拖动 **/
        if (mode == MODE.DRAG) {

            /** 在这里要进行判断处理，防止在drag时�?越界 **/

            /** 获取相应的l，t,r ,b **/
            left = current_x - start_x;
            right = current_x + this.getWidth() - start_x;
            top = current_y - start_y;
            bottom = current_y - start_y + this.getHeight();

            /** 水平进行判断 **/
            if (isControl_H) {
                if (left >= 0) {
                    left = 0;
                    right = this.getWidth();
                }
                if (right <= screen_W) {
                    left = screen_W - this.getWidth();
                    right = screen_W;
                }
            } else {
                left = this.getLeft();
                right = this.getRight();
            }
            /** 垂直判断 **/
            if (isControl_V) {
                if (top >= 0) {
                    top = 0;
                    bottom = this.getHeight();
                }

                if (bottom <= screen_H) {
                    top = screen_H - this.getHeight();
                    bottom = screen_H;
                }
            } else {
                top = this.getTop();
                bottom = this.getBottom();
            }
            if (isControl_H || isControl_V)
                this.setPosition(left, top, right, bottom);

            current_x = (int) event.getRawX();
            current_y = (int) event.getRawY();

        }
        /** 处理缩放 **/
        else if (mode == MODE.ZOOM) {

            afterLenght = getDistance(event);// 获取两点的距�?

            float gapLenght = afterLenght - beforeLenght;// 变化的长�?

            if (Math.abs(gapLenght) > 5f) {
                scale_temp = afterLenght / beforeLenght;// 求的缩放的比�?

                this.setScale(scale_temp);

                beforeLenght = afterLenght;
            }
        }

    }

    // 获取两点的距�?
    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return FloatMath.sqrt(x * x + y * y);
    }

    /** ʵ�ִ����϶� **/
    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }

    /** 处理缩放 **/
    private void setScale(float scale) {
        int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
        int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// 获取缩放垂直距离

        // 放大
        if (scale > 1) {
//        if (scale > 1 && this.getWidth() <= MAX_W) {
            current_Left = this.getLeft() - disX;
            current_Top = this.getTop() - disY;
            current_Right = this.getRight() + disX;
            current_Bottom = this.getBottom() + disY;

            this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
            /***
             * 此时因为考虑到对称，�?��只做�?��判断就可以了�?
             */
            if (current_Top <= 0 && current_Bottom >= screen_H) {
                // Log.e("jj", "屏幕高度=" + this.getHeight());
                isControl_V = true;// �?��垂直监控
            } else {
                isControl_V = false;
            }
            if (current_Left <= 0 && current_Right >= screen_W) {
                isControl_H = true;// �?��水平监控
            } else {
                isControl_H = false;
            }

        }
        // 缩小
        else if (scale < 1) {
//        else if (scale < 1 && this.getWidth() >= MIN_W) {
            current_Left = this.getLeft() + disX;
            current_Top = this.getTop() + disY;
            current_Right = this.getRight() - disX;
            current_Bottom = this.getBottom() - disY;
            /***
             * 在这里要进行缩放处理
             */
            // 上边越界
            if (isControl_V && current_Top > 0) {
                current_Top = 0;
                current_Bottom = this.getBottom() - 2 * disY;
                if (current_Bottom < screen_H) {
                    current_Bottom = screen_H;
                    isControl_V = false;// 关闭垂直监听
                }
            }
            // 下边越界
            if (isControl_V && current_Bottom < screen_H) {
                current_Bottom = screen_H;
                current_Top = this.getTop() + 2 * disY;
                if (current_Top > 0) {
                    current_Top = 0;
                    isControl_V = false;// 关闭垂直监听
                }
            }

            // 左边越界
            if (isControl_H && current_Left >= 0) {
                current_Left = 0;
                current_Right = this.getRight() - 2 * disX;
                if (current_Right <= screen_W) {
                    current_Right = screen_W;
                    isControl_H = false;// 关闭
                }
            }
            // 右边越界
            if (isControl_H && current_Right <= screen_W) {
                current_Right = screen_W;
                current_Left = this.getLeft() + 2 * disX;
                if (current_Left >= 0) {
                    current_Left = 0;
                    isControl_H = false;// 关闭
                }
            }

            if (isControl_H || isControl_V) {
                this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
            } else {
                this.setFrame(current_Left, current_Top, current_Right, current_Bottom);
                isScaleAnim = true;// �?��缩放动画
            }

        }

    }

    /***
     * ���Ŷ�������
     */
    public void doScaleAnim() {
        myAsyncTask = new MyAsyncTask(screen_W, this.getWidth(), this.getHeight());
        myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        myAsyncTask.execute();
        isScaleAnim = false;// �رն���
    }

    /***
     * ������������
     */
    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        private int screen_W, current_Width, current_Height;

        private int left, top, right, bottom;

        private float scale_WH;// ��ߵı���?

        /** ��ǰ��λ������ **/
        public void setLTRB(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        private float STEP = 8f;// ����

        private float step_H, step_V;// ˮƽ��������ֱ����

        public MyAsyncTask(int screen_W, int current_Width, int current_Height) {
            super();
            this.screen_W = screen_W;
            this.current_Width = current_Width;
            this.current_Height = current_Height;
            scale_WH = (float) current_Height / current_Width;
            step_H = STEP;
            step_V = scale_WH * STEP;
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (current_Width <= screen_W) {

                left -= step_H;
                top -= step_V;
                right += step_H;
                bottom += step_V;

                current_Width += 2 * step_H;

                left = Math.max(left, start_Left);
                top = Math.max(top, start_Top);
                right = Math.min(right, start_Right);
                bottom = Math.min(bottom, start_Bottom);
                Log.e("jj", "top=" + top + ",bottom=" + bottom + ",left=" + left + ",right=" + right);
                onProgressUpdate(new Integer[] { left, top, right, bottom });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFrame(values[0], values[1], values[2], values[3]);
                }
            });

        }

    }

}

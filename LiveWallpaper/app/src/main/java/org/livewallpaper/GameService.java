package org.livewallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * GameService runs in background and update the shown wallpaper.
 */
public class GameService extends WallpaperService {

    /**
     * Key of the prefs file.
     */
    public static final String SHARED_PREFS_NAME = "livewallpaper";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new GameEngine();
    }

    /**
     * GameEngine handling drawing and manage game logic.
     */
    class GameEngine extends Engine implements
            SharedPreferences.OnSharedPreferenceChangeListener {

        /**  */
        private final Handler mHandler = new Handler();
        /**  */
        private float mTouchX = -1;
        /**  */
        private float mTouchY = -1;
        /**  */
        private final Paint mPaint = new Paint();
        /**   */
        private final Runnable mDrawPattern = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        /**  */
        private boolean mVisible;
        /**  */
        private SharedPreferences mPreferences;
        /**  */
        private Rect mRectFrame;
        /**  */
        private boolean mHorizontal = false;
        /**  */
        private int mFrameCounter = 0;
        /**  */
        private boolean mMotion = true;
        /**  */
        private String mShape = "smpte";
        /**  */
        Player player;
        /**  */
        Enemy enemy;
        /**  */
        Grid grid;
        /**  */
        Canvas canvas;

        /**
         * Main constructor
         */
        GameEngine() {
            final Paint paint = mPaint;
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            mPreferences = GameService.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
            mPreferences.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(mPreferences, null);
            initFrameParams();

            // Set window width and height
            float windowSizeX = mRectFrame.width();
            float windowSizeY = mRectFrame.height();


            player = new Player(windowSizeX, windowSizeY, 100, 50, .7f);


            enemy = new Enemy(windowSizeX, windowSizeY, 100, 100, .1f);
            grid = new Grid(windowSizeX, windowSizeY);
        }

        /**
         * Listens to changed settings
         *
         * @param prefs the new preferences
         * @param key   the changed key
         */
        public void onSharedPreferenceChanged(SharedPreferences prefs,
                                              String key) {

        }


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawPattern);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawPattern);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            Log.d(GameEngine.class.getSimpleName(), String.format("entered onSurfaceChanged(format: %d, width: %d, height: %d)", format, width, height));
            super.onSurfaceChanged(holder, format, width, height);

            initFrameParams();
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.d(GameEngine.class.getSimpleName(), String.format("entered onSurfaceCreated()"));
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.d(GameEngine.class.getSimpleName(), String.format("entered onSurfaceDestroyed()"));
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawPattern);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            drawFrame();
        }

        /**
         * Store the position of the touch event so we can use it for drawing
         * later
         *
         * @param event the motion event
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();

            } else {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }

        /**
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    //update
                    updateAll();
                    //draw
                    drawAll(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            mHandler.removeCallbacks(mDrawPattern);
            if (mVisible) {
                mHandler.postDelayed(mDrawPattern, 1000 / 25);
            }
        }

        /**
         * Update game objects.
         */
        void updateAll() {

            if (mTouchX >= 0) {
                player.changePosX(mTouchX);
            }
            player.update();
            enemy.update();
        }

        /**
         * Draw game objects and background on the canvas.
         *
         * @param c the canvas
         */
        void drawAll(Canvas c) {
            refreshAll(c);
            drawBackground(c);
            grid.draw(c);
            player.draw(c);
            enemy.draw(c);
        }

        /**
         * Clear the canvas.
         *
         * @param c the canvas
         */
        void refreshAll(Canvas c) {
            c.save();

            //to refresh the background?
            c.drawColor(0xff000000);
            c.restore();
        }

        /**
         * Draw the background on a given canvas.
         *
         * @param c the canvas
         */
        void drawBackground(Canvas c) {
            c.drawARGB(255, 0, 0, 10);
        }

        /**
         * Update the frame parameters.
         */
        void initFrameParams() {
            DisplayMetrics metrics = new DisplayMetrics();
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            display.getMetrics(metrics);

            mRectFrame = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);


            int rotation = display.getOrientation();
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
                mHorizontal = false;
            else
                mHorizontal = true;

        }
    }

}


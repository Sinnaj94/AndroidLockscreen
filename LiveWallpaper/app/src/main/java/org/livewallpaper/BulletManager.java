package org.livewallpaper;

import java.util.ArrayList;

/**
 * Created by jeff on 19/05/16.
 */
public class BulletManager extends ArrayList<Bullet> {

    private int maxBullets = Constants.BULLET_MAX_ELEMENTS;

    public BulletManager(int maxBullets){
        this.maxBullets = maxBullets;
        init();
    }

    public void init(){
        for(int i = 0; i < maxBullets; i++){
            this.add(new Bullet());
        }
    }

    public void shootBullet(float posX, float posY) {
        for(int i = 0; i < maxBullets; i++){
            Bullet b = get(i);
            if(!b.active){
                b.fire(posX,posY);
                return;
            }
        }
    }

    public void update() {
        for(int i = 0; i < maxBullets; i++){
            Bullet b = get(i);
            if(b.active) {
                b.update();
            }
        }
    }
}
